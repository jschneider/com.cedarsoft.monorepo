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
import it.neckar.open.crypt.HashCalculator;

import it.neckar.open.io.LinkUtils;

import com.google.common.collect.ImmutableSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Imports photos from a given directory.
 * Creates hard links
 *
 */
public class Importer {
  @Nonnull
  private static final ImmutableSet<String> SUPPORTED_FILE_SUFFICIES = ImmutableSet.of("jpeg", "jpg", "cr2");
  @Nonnull
  private final ImageStorage imageStorage;

  @Inject
  public Importer(@Nonnull ImageStorage imageStorage) {
    this.imageStorage = imageStorage;
  }

  /**
   * Imports the given file
   */
  @NonUiThread
  public void importFile(@Nonnull File fileToImport, @Nonnull Listener listener) throws IOException {
    Hash hash = HashCalculator.calculate(ImageStorage.ALGORITHM, fileToImport);

    File targetFile = imageStorage.getDataFile(hash);
    if (targetFile.exists()) {
      listener.skipped(hash, fileToImport, targetFile);
      return;
    }

    File dir = targetFile.getParentFile();
    ImageStorage.ensureDirectoryExists(dir);

    //Set writable before
    dir.setWritable(true, true);
    try {
      //Create a hard link
      LinkUtils.createHardLink(fileToImport, targetFile);

      //Set the file to read only
      targetFile.setWritable(false);
      targetFile.setExecutable(false);
    } catch (LinkUtils.AlreadyExistsWithOtherTargetException e) {
      throw new IOException(e);
    } finally {
      //Set to read only
      dir.setWritable(false, false);
    }

    listener.imported(hash, fileToImport, targetFile);
  }

  /**
   * Imports all files within the given directory
   */
  @NonUiThread
  public void importDirectory(@Nonnull File directory, @Nonnull Listener listener) throws IOException {
    if (!directory.isDirectory()) {
      throw new FileNotFoundException("Not a directory <" + directory.getAbsolutePath() + ">");
    }

    if (directory.getName().equals(".@__thumb")) {
      //Skip thumbs dir
      return;
    }

    @Nullable File[] files = directory.listFiles();
    if (files == null) {
      throw new IllegalStateException("Could not list files in <" + directory.getAbsolutePath() + ">");
    }

    for (File file : files) {
      if (isSupported(file)) {
        importFile(file, listener);
      }

      //Import all files from the sub directories
      if (file.isDirectory()) {
        importDirectory(file, listener);
      }
    }
  }

  /**
   * Returns whether the given file is supported
   */
  private static boolean isSupported(@Nonnull File file) {
    String fileNameLowerCase = file.getName().toLowerCase();

    for (String supportedFileSuffix : SUPPORTED_FILE_SUFFICIES) {
      if (fileNameLowerCase.endsWith("." + supportedFileSuffix)) {
        return true;
      }
    }

    return false;
  }

  public interface Listener {
    /**
     * Is called if the file is skipped
     */
    void skipped(@Nonnull Hash hash, @Nonnull File fileToImport, @Nonnull File targetFile);

    /**
     * Is called if the file has been imported
     */
    void imported(@Nonnull Hash hash, @Nonnull File fileToImport, @Nonnull File targetFile);
  }
}
