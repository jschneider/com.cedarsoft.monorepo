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

package it.neckar.open.zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

/**
 * Helper class that creates zip files
 *
 */
public class ZipCreator {
  static final int BUFFER_SIZE = 2048;

  @Nonnull
  private final File zipFile;

  /**
   * Create a new zip creator with the given file as backend
   *
   * @param zipFile the zip file
   */
  public ZipCreator( @Nonnull File zipFile ) {
    this.zipFile = zipFile;
  }

  /**
   * Returns the zip file
   *
   * @return the zip file
   */
  @Nonnull
  public File getZipFile() {
    return zipFile;
  }

  /**
   * Zip all files within the given directories
   *
   * @param directories the directories all files are zipped within
   * @return the zipped file
   *
   * @throws IOException if an io exception occures
   */
  public File zip( @Nonnull File... directories ) throws IOException {
    try (ZipArchiveOutputStream outStream = new ZipArchiveOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)))) {
      for (File directory : directories) {
        String baseName = directory.getCanonicalPath();
        addFiles(baseName, outStream, directory);
      }
    }
    return zipFile;
  }

  /**
   * Add the files within the given directory to the ZipOutputStream. This method will call itself for each subdirectory.
   *
   * @param baseName  represents the relative canonical base name within the zip file
   * @param outStream the ouput stream
   * @param directory the directory
   * @throws IOException if any.
   */
  protected void addFiles( @Nonnull String baseName, @Nonnull ZipArchiveOutputStream outStream, @Nonnull File directory ) throws IOException {
    @Nullable File[] files = directory.listFiles();
    if (files == null) {
      throw new IOException("could not list <" + directory.getAbsolutePath() + ">");
    }
    byte[] data = new byte[BUFFER_SIZE];
    for (File file : files) {
      String relativeName = getCanonicalRelativePath( baseName, file );
      ArchiveEntry entry = new ZipArchiveEntry( relativeName );
      try {
        outStream.putArchiveEntry( entry );
      } catch ( ZipException ignore ) {
      }

      if ( file.isDirectory() ) {
        //Add the files within the directory
        addFiles( baseName, outStream, file );
        continue;
      }

      try (InputStream origin = new BufferedInputStream(new FileInputStream(file), BUFFER_SIZE)) {
        int count;
        while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
          outStream.write(data, 0, count);
        }
      }

      outStream.closeArchiveEntry();
    }
  }

  /**
   * Returns the relative canonical path
   *
   * @param baseName the canonical base path
   * @param file     the file
   * @return the path of the given file relative to the base name
   * @see #getRelativePath(String, File)
   * @throws IOException if any.
   */
  @Nonnull
  protected static String getCanonicalRelativePath( @Nonnull String baseName, @Nonnull File file ) throws IOException {
    //noinspection NonConstantStringShouldBeStringBuffer
    String name = file.getCanonicalPath().substring( baseName.length() + 1 ); // use #getCanonicalPath because baseName is canonical
    if ( file.isDirectory() ) {
      name += '/';
    }
    return name.replaceAll( "\\\\", "/" );
  }

  /**
   * Returns the relative path
   *
   * @param baseName the base path
   * @param file     the file
   * @return the path of the given file relative to the base name
   * @see #getCanonicalRelativePath(String, File)
   */
  @Nonnull
  protected static String getRelativePath( @Nonnull String baseName, @Nonnull File file ) {
    //noinspection NonConstantStringShouldBeStringBuffer
    String name = file.getAbsolutePath().substring( baseName.length() + 1 );
    if ( file.isDirectory() ) {
      name += '/';
    }
    return name.replaceAll( "\\\\", "/" );
  }
}
