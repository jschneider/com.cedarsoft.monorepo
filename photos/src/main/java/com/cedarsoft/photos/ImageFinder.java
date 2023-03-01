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
package com.cedarsoft.photos;

import it.neckar.open.annotations.NonUiThread;

import it.neckar.open.crypt.Hash;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 */
public class ImageFinder {
  private static final Logger LOG = Logger.getLogger(ImageFinder.class.getName());

  @Nonnull
  private final ImageStorage storage;

  @Inject
  public ImageFinder(@Nonnull ImageStorage storage) {
    this.storage = storage;
  }

  /**
   * Method that only "finds" the image with the given hash
   */
  @NonUiThread
  public void find(@Nonnull Hash hash, @Nonnull Consumer consumer) throws IOException {
    File dataFile = storage.getDataFile(hash);
    consumer.found(storage, dataFile, hash);
  }

  @NonUiThread
  public void find(@Nonnull Consumer consumer) throws IOException {
    @Nullable File[] firstPartHashDirs = storage.getBaseDir().listFiles();

    assert firstPartHashDirs != null;
    for (File firstPartHashDir : firstPartHashDirs) {
      if (!firstPartHashDir.isDirectory()) {
        LOG.warning("Unexpected file found: " + firstPartHashDir.getAbsolutePath());
        continue;
      }

      File[] dataDirs = firstPartHashDir.listFiles();
      assert dataDirs != null;
      for (File dataDir : dataDirs) {
        File dataFile = new File(dataDir, ImageStorage.DATA_FILE_NAME);
        if (!dataFile.exists()) {
          LOG.warning("Missing data file: <" + dataFile.getAbsolutePath() + ">");
          continue;
        }

        Hash hash = Hash.fromHex(ImageStorage.ALGORITHM, dataDir.getName());
        consumer.found(storage, dataFile, hash);
      }
    }
  }

  /**
   * Consumer for found images
   */
  @FunctionalInterface
  public interface Consumer {
    /**
     * Is called for each data file that has been found
     */
    @NonUiThread
    void found(@Nonnull ImageStorage storage, @Nonnull File dataFile, @Nonnull Hash hash) throws IOException;
  }
}
