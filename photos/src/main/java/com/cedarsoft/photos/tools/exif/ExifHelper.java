package com.cedarsoft.photos.tools.exif;

import com.cedarsoft.crypt.Hash;
import com.cedarsoft.photos.ImageStorage;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ExifHelper {
  @Nonnull
  public static final String EXIF_FILE_NAME = "exif";
  @Nonnull
  private final ImageStorage storage;

  @Inject
  public ExifHelper(@Nonnull ImageStorage storage) {
    this.storage = storage;
  }

  /**
   * Returns the exif info
   */
  @Nonnull
  public ExifInfo getExifInfo(@Nonnull Hash hash) throws IOException, NoExifInfoFoundException {
    File dir = storage.getDir(hash);
    if (!dir.exists()) {
      throw new NoExifInfoFoundException(hash);
    }

    File exifFile = new File(dir, EXIF_FILE_NAME);
    if (!exifFile.exists()) {
      throw new NoExifInfoFoundException(hash);
    }

    try (FileInputStream in = new FileInputStream(exifFile)) {
      return new ExifInfo(in);
    }
  }

  public static class NoExifInfoFoundException extends Exception {
    public NoExifInfoFoundException(@Nonnull Hash hash) {
      super("No exif available for <" + hash + ">");
    }
  }
}
