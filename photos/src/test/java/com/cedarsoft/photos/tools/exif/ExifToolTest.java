/**
 * Copyright (C) cedarsoft GmbH.
 * <p>
 * Licensed under the GNU General Public License version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.cedarsoft.org/gpl3
 * <p>
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation.
 * <p>
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 * <p>
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * <p>
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */

package com.cedarsoft.photos.tools.exif;

import org.apache.commons.io.IOUtils;
import org.junit.*;
import org.junit.rules.*;

import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.*;

/**
 *
 */
public class ExifToolTest {
  @Rule
  public TemporaryFolder tmp = new TemporaryFolder();

  @Test
  public void testClearRotation() throws IOException {
    ExifTool exifTool = createExifTool();

    File file = tmp.newFile("ExifToolTest_testClearRotation.jpg");

    try (FileOutputStream fos = new FileOutputStream(file)) {
      IOUtils.copy(getClass().getResourceAsStream("/img1.jpg"), fos);
    }

    {
      OutputStream out = new ByteArrayOutputStream();
      exifTool.run(null, out, "-Orientation", "-S", file.getAbsolutePath());
      assertEquals("Orientation: Rotate 270 CW", out.toString().trim());
    }

    exifTool.clearRotation(file);

    {
      OutputStream out = new ByteArrayOutputStream();
      exifTool.run(null, out, "-Orientation", "-S", file.getAbsolutePath());
      assertEquals("Orientation: Horizontal (normal)", out.toString().trim());
    }
  }

  @Nonnull
  public static ExifTool createExifTool() {
    File bin = new File("/usr/bin/exiftool");
    if (!bin.exists()) {
      throw new AssertionError("No exiftool installed.");
    }
    return new ExifTool(bin);
  }
}
