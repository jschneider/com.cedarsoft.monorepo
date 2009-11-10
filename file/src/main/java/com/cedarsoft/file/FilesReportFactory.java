package com.cedarsoft.file;

import com.cedarsoft.file.FileName;
import com.cedarsoft.file.FileTypeRegistry;
import com.cedarsoft.provider.InputStreamFromFileProvider;
import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 *
 */
public class FilesReportFactory {
  @NotNull
  private final FileTypeRegistry fileTypeRegistry;

  @Inject
  public FilesReportFactory( @NotNull FileTypeRegistry fileTypeRegistry ) {
    this.fileTypeRegistry = fileTypeRegistry;
  }

  @NotNull
  public FilesReport createReport( @NotNull Iterable<? extends File> sourceFiles ) {
    FilesReport report = new FilesReport();

    for ( File sourceFile : sourceFiles ) {
      FileName fileName = fileTypeRegistry.parseFileName( sourceFile.getName() );
      report.add( fileName, new InputStreamFromFileProvider( sourceFile ) );
    }

    return report;
  }


}
