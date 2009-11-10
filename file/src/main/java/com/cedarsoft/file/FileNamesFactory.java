package com.cedarsoft.file;

import com.google.inject.Inject;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;

/**
 *
 */
public class FileNamesFactory {
  @NotNull
  private final FileTypeRegistry fileTypeRegistry;

  @Inject
  public FileNamesFactory( @NotNull FileTypeRegistry fileTypeRegistry ) {
    this.fileTypeRegistry = fileTypeRegistry;
  }

  @NotNull
  public FileNames create( @NotNull File baseDir ) {
    File[] files = listFiles( baseDir );
    return create( files );
  }

  @NotNull
  public FileNames create( @NotNull File[] sourceFiles ) {
    return create( Arrays.asList( sourceFiles ) );
  }

  public FileNames create( @NotNull Iterable<? extends File> sourceFiles ) {
    FileNames fileNames = new FileNames();

    for ( File file : sourceFiles ) {
      FileName fileName = fileTypeRegistry.parseFileName( file.getName() );
      fileNames.add( fileName );
    }

    return fileNames;
  }

  @NotNull
  public BaseNameAwareFileNames createBaseNameAware( @NotNull File baseDir ) {
    return createBaseNameAware( listFiles( baseDir ) );
  }

  @NotNull
  public BaseNameAwareFileNames createBaseNameAware( @NotNull File[] sourceFiles ) {
    return createBaseNameAware( Arrays.asList( sourceFiles ) );
  }

  @NotNull
  public BaseNameAwareFileNames createBaseNameAware( @NotNull Iterable<? extends File> sourceFiles ) {
    BaseNameAwareFileNames report = new BaseNameAwareFileNames();

    for ( File sourceFile : sourceFiles ) {
      FileName fileName = fileTypeRegistry.parseFileName( sourceFile.getName() );
      report.add( fileName );
    }

    return report;
  }

  @NotNull
  private File[] listFiles( @NotNull File baseDir ) {
    if ( !baseDir.isDirectory() ) {
      throw new IllegalArgumentException( "Invalid base dir <" + baseDir.getAbsolutePath() + '>' );
    }

    File[] files = baseDir.listFiles( ( FileFilter ) FileFileFilter.FILE );
    return files;
  }

}
