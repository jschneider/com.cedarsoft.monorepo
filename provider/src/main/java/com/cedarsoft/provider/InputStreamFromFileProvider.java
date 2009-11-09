package com.cedarsoft.provider;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.Override;

/**
 *
 */
public class InputStreamFromFileProvider implements Provider<InputStream, IOException> {
  @NotNull
  private final File file;

  public InputStreamFromFileProvider( @NotNull File file ) {
    this.file = file;
  }

  @Override
  @NotNull
  public InputStream provide() throws IOException {
    return new BufferedInputStream( new FileInputStream( file ) );
  }

  @Override
  @NotNull
  public String getDescription() {
    return file.getAbsolutePath();
  }
}
