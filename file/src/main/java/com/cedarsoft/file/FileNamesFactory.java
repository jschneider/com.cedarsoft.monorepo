package com.cedarsoft.file;

import org.apache.commons.io.filefilter.FileFileFilter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileFilter;

/**
 *
 */
public class FileNamesFactory {
  private FileNamesFactory() {
  }

  @NotNull
  public static FileNames create( @NotNull File baseDir, @NotNull FileTypeRegistry fileTypeRegistry ) {
    File[] files = listFiles( baseDir );
    return create( files, fileTypeRegistry );
  }

  @NotNull
  public static FileNames create( @NotNull File[] sourceFiles, @NotNull FileTypeRegistry fileTypeRegistry ) {
    FileNames fileNames = new FileNames();

    for ( File file : sourceFiles ) {
      FileName fileName = fileTypeRegistry.parseFileName( file.getName() );
      fileNames.add( fileName );
    }

    return fileNames;
  }

  @NotNull
  public static BaseNameAwareFileNames createBaseNameAware( @NotNull File baseDir, @NotNull FileTypeRegistry fileTypeRegistry ) {
    return createBaseNameAware( listFiles( baseDir ), fileTypeRegistry );
  }

  public static BaseNameAwareFileNames createBaseNameAware( @NotNull File[] sourceFiles, @NotNull FileTypeRegistry fileTypeRegistry ) {
    BaseNameAwareFileNames report = new BaseNameAwareFileNames();

    for ( File sourceFile : sourceFiles ) {
      FileName fileName = fileTypeRegistry.parseFileName( sourceFile.getName() );
      report.add( fileName );
    }

    return report;
  }

  @NotNull
  private static File[] listFiles( @NotNull File baseDir ) {
    if ( !baseDir.isDirectory() ) {
      throw new IllegalArgumentException( "Invalid base dir <" + baseDir.getAbsolutePath() + '>' );
    }

    File[] files = baseDir.listFiles( ( FileFilter ) FileFileFilter.FILE );
    return files;
  }

}
