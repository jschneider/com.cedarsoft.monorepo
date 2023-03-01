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

import it.neckar.open.crypt.Algorithm;
import it.neckar.open.crypt.Hash;

import javax.annotation.concurrent.Immutable;

import org.apache.commons.io.FileUtils;

import javax.annotation.Nonnull;

import java.io.File;
import java.io.IOException;

/**
 */
@Immutable
public class ImageStorage {
  @Nonnull
  public static final Algorithm ALGORITHM = Algorithm.SHA256;

  @Nonnull
  public static final String DATA_FILE_NAME = "data";
  @SuppressWarnings("Immutable")
  @Nonnull
  private final File baseDir;
  @SuppressWarnings("Immutable")
  @Nonnull
  private final File deletedBaseDir;

  public ImageStorage(@Nonnull File baseDir, @Nonnull File deletedBaseDir) {
    this.baseDir = baseDir;
    this.deletedBaseDir = deletedBaseDir;

    if (!baseDir.isDirectory()) {
      throw new IllegalArgumentException("Base dir does not exist <" + baseDir.getAbsolutePath() + ">");
    }
  }

  @Nonnull
  @NonUiThread
  File getDataFile(@Nonnull Hash hash) throws IOException {
    File dir = getDir(SplitHash.split(hash));
    return new File(dir, DATA_FILE_NAME);
  }

  /**
   * Returns the dir for the given hash
   */
  @Nonnull
  @NonUiThread
  public File getDir(@Nonnull Hash hash) throws IOException {
    return getDir(SplitHash.split(hash));
  }

  @NonUiThread
  @Nonnull
  private File getDir(@Nonnull SplitHash splitHash) {
    File firstPartDir = getFirstPartDir(splitHash.getFirstPart());
    return new File(firstPartDir, splitHash.getHashAsHex());
  }

  /**
   * Returns the dir for the first part
   */
  @Nonnull
  private File getFirstPartDir(@Nonnull String firstPart) {
    return new File(baseDir, firstPart);
  }

  @Nonnull
  public File getBaseDir() {
    return baseDir;
  }

  @Nonnull
  public File getDeletedBaseDir() {
    return deletedBaseDir;
  }

  /**
   * Returns the dir for the first part
   */
  @Nonnull
  private File getDeletedFirstPartDir(@Nonnull String firstPart) {
    return new File(deletedBaseDir, firstPart);
  }

  @NonUiThread
  @Nonnull
  private File getDeletedDir(@Nonnull SplitHash splitHash) {
    File firstPartDir = getDeletedFirstPartDir(splitHash.getFirstPart());
    return new File(firstPartDir, splitHash.getHashAsHex());
  }

  @Nonnull
  @NonUiThread
  File getDeletedDataFile(@Nonnull Hash hash) throws IOException {
    File dir = getDeletedDir(SplitHash.split(hash));
    return new File(dir, DATA_FILE_NAME);
  }

  public void delete(@Nonnull Hash hash) throws IOException {
    SplitHash splitHash = SplitHash.split(hash);
    File targetDir = getDeletedDir(splitHash);

    //Already deleted
    if (targetDir.exists()) {
      FileUtils.deleteDirectory(targetDir);
    }

    //Now move the original directory
    File dirToDelete = getDir(splitHash);
    dirToDelete.setWritable(true);
    FileUtils.moveDirectory(dirToDelete, targetDir);
  }

  public static void ensureDirectoryExists(@Nonnull File dir) throws IOException {
    if (!dir.isDirectory()) {
      if (!dir.mkdirs()) {
        throw new IOException("Could not create directory <" + dir.getAbsolutePath() + ">");
      }
    }
  }

}
