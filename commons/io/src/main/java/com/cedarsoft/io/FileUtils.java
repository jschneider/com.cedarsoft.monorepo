package com.cedarsoft.io;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class FileUtils {
  private FileUtils() {
  }

  /**
   * Throws an IOException if the given file is not a directory
   */
  public static void ensureDirectoryExists(@Nonnull File directory) throws IOException {
    if (!directory.exists()) {
      throw new IOException("Does not exist <" + directory.getAbsolutePath() + ">");
    }
    if (!directory.isDirectory()) {
      throw new IOException("Directory not found <" + directory.getAbsolutePath() + ">");
    }
  }
}
