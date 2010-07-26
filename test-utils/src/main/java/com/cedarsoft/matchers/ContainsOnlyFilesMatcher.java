package com.cedarsoft.matchers;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class ContainsOnlyFilesMatcher extends BaseMatcher<File> {
  @NotNull
  @NonNls
  private final List<String> filePaths;

  public ContainsOnlyFilesMatcher( @NotNull @NonNls String... relativeFilePaths ) {
    filePaths = new ArrayList<String>( Arrays.asList( relativeFilePaths ) );
  }

  @Override
  public boolean matches( Object o ) {
    File dir = ( File ) o;
    if ( !( ( File ) o ).isDirectory() ) {
      return false;
    }

    Collection<? extends File> files = FileUtils.listFiles( dir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE );
    if ( files.size() != filePaths.size() ) {
      return false;
    }

    //Create the set with the expected files
    Set<? extends File> expected = createExepectedSet( dir );


    for ( File file : files ) {
      System.out.println( "file = " + file );

      if ( !expected.contains( file ) ) {
        return false;
      }
    }

    return true;
  }

  @NotNull
  private Set<? extends File> createExepectedSet( @NotNull File baseDir ) {
    Set<File> expected = new HashSet<File>();
    for ( String filePath : filePaths ) {
      expected.add( new File( baseDir, filePath ) );
    }
    return expected;
  }

  @Override
  public void describeTo( Description description ) {
    description.appendText( "contains only files " + filePaths );
  }

  @NotNull
  public List<? extends String> getFilePaths() {
    return Collections.unmodifiableList( filePaths );
  }

  @NotNull
  public static Matcher<File> containsOnlyFiles( @NotNull @NonNls String... relativeFilePaths ) {
    return new ContainsOnlyFilesMatcher( relativeFilePaths );
  }

  @NotNull
  @NonNls
  public static String toTree( @NotNull File dir ) {
    return ContainsFileMatcher.toTree( dir );
  }
}
