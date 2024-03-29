/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce
 *         (GPL 3 with Classpath Exception)
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */
package com.cedarsoft.photos.tools.exif;

import it.neckar.open.crypt.Hash;

import com.cedarsoft.photos.ImageStorage;
import javax.annotation.concurrent.Immutable;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

/**
 */
@Immutable
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
