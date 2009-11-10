package com.cedarsoft.file;

import com.cedarsoft.FileName;
import com.cedarsoft.FileTypeRegistry;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class Directory {
  @NotNull
  private final List<FileName> files = new ArrayList<FileName>();

  public void addFile( @NotNull FileName entry ) {
    this.files.add( entry );
  }

  @NotNull
  public List<? extends FileName> getFiles() {
    return Collections.unmodifiableList( files );
  }

  @NotNull
  public static Directory read( @NotNull File baseDir, @NotNull FileTypeRegistry fileTypeRegistry ) {
    if ( !baseDir.isDirectory() ) {
      throw new IllegalArgumentException( "Invalid base dir <" + baseDir.getAbsolutePath() + '>' );
    }

    Directory directory = new Directory();

    for ( File file : baseDir.listFiles( ( FileFilter ) FileFileFilter.FILE ) ) {
      FileName fileName = fileTypeRegistry.parseFileName( file.getName() );
      directory.addFile( fileName );
    }

    return directory;
  }
}
