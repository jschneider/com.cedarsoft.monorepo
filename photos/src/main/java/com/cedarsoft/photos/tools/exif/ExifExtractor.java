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

import javax.annotation.concurrent.Immutable;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Extracts the exif information
 */
@Immutable
public class ExifExtractor {
  @Nonnull
  private final ExifTool exifTool;

  @Inject
  public ExifExtractor(@Nonnull ExifTool exifTool) {
    this.exifTool = exifTool;
  }

  public void extractHumanReadable(@Nonnull InputStream source, @Nonnull OutputStream out) throws IOException {
    exifTool.run(source, out, "-a", "-");
  }

  public void extractDetailed(@Nonnull InputStream source, @Nonnull OutputStream out) throws IOException {
    exifTool.run(source, out, "-a", "-t", "-D", "-s", "-");
  }

  public void extractHtml(@Nonnull InputStream source, @Nonnull OutputStream out) throws IOException {
    exifTool.run(source, out, "-a", "-t", "-h", "-");
  }

  public void extractSummary(@Nonnull InputStream source, @Nonnull OutputStream out) throws IOException {
    exifTool.run(source, out, "-canon", "-");
  }

  /**
   * Copies the exif data from the source file to the target file - but not the Orientation
   *
   * @param source               the source
   * @param target               the target
   * @param includingOrientation whether the orientation is included
   */
  @Deprecated //untested!
  public void copyExif(@Nonnull File source, @Nonnull File target, boolean includingOrientation) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    List<String> args = new ArrayList<>();
    args.add("-q");
    if (!includingOrientation) {
      args.add("-x");
      args.add("Orientation");
    }
    args.add("-P");
    args.add("-overwrite_original");
    args.add("-TagsFromFile");
    args.add(source.getAbsolutePath());
    args.add(target.getAbsolutePath());

    exifTool.run(null, out, args.toArray(new String[args.size()]));

    if (out.toByteArray().length > 0) {
      throw new IOException("Conversion failed due to " + new String(out.toByteArray(), StandardCharsets.UTF_8));
    }
  }

  @Nonnull
  public ExifInfo extractInfo(@Nonnull InputStream imageIn) throws IOException {
    ByteArrayOutputStream exifOut = new ByteArrayOutputStream();
    extractDetailed(imageIn, exifOut);
    return new ExifInfo(new ByteArrayInputStream(exifOut.toByteArray()));
  }

  @Nonnull
  public ExifInfo extractInfo(@Nonnull File imageFile) throws IOException {
    try (InputStream in = new BufferedInputStream(new FileInputStream(imageFile))) {
      return extractInfo(in);
    }
  }

}
