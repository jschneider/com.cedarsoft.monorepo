package com.cedarsoft.file;

import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 *
 */
public class BaseNameAwareFilesReportFactory {
  @NotNull
  private final FileTypeRegistry fileTypeRegistry;

  @Inject
  public BaseNameAwareFilesReportFactory( @NotNull FileTypeRegistry fileTypeRegistry ) {
    this.fileTypeRegistry = fileTypeRegistry;
  }

  @NotNull
  public BaseNameAwareFileNames createReport( @NotNull Iterable<? extends File> sourceFiles ) {
    BaseNameAwareFileNames report = new BaseNameAwareFileNames();

    for ( File sourceFile : sourceFiles ) {
      FileName fileName = fileTypeRegistry.parseFileName( sourceFile.getName() );
      report.add( fileName );
    }

    return report;
  }
}
