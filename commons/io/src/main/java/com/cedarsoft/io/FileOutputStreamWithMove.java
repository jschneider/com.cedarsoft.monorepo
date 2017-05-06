package com.cedarsoft.io;

import javax.annotation.Nonnull;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class FileOutputStreamWithMove extends FilterOutputStream {
  public static final String SUFFIX_TMP = ".tmp";

  private boolean closed;
  @Nonnull
  private final File tmpFile;
  @Nonnull
  private final File file;

  public FileOutputStreamWithMove(@Nonnull File file) throws FileNotFoundException {
    //noinspection ConstantConditions
    super(null);

    tmpFile = new File(file.getParent(), file.getName() + SUFFIX_TMP);
    this.file = file;
    tmpFile.deleteOnExit();

    this.out = new BufferedOutputStream(new FileOutputStream(tmpFile));
  }

  @Override
  public void close() throws IOException {
    super.close();
    if (closed) {
      return;
    }
    //Only move the file if it exists
    if (tmpFile.exists()) {
      //delete the original file first - overwrite mode
      if (file.exists()) {
        file.delete();
      }

      Files.move(tmpFile.toPath(), file.toPath());
    }
    closed = true;
  }
}

