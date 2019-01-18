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

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.annotation.Nonnull;

import org.apache.commons.io.IOUtils;
import org.junit.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

import com.cedarsoft.photos.tools.CmdLineToolNotAvailableException;
import com.cedarsoft.test.utils.TemporaryFolder;
import com.cedarsoft.test.utils.WithTempFiles;

/**
 *
 */
@Disabled
@WithTempFiles
public class ExifToolTest {

  private ExifTool exifTool;

  @BeforeEach
  public void setUp() throws Exception {
    exifTool = createExifTool();
  }

  @Test
  public void testClearRotation(@Nonnull TemporaryFolder tmp) throws IOException {
    if (exifTool == null) {
      return;
    }

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
  public static ExifTool createExifTool() throws CmdLineToolNotAvailableException {
    File bin = new File("/usr/bin/exiftool");
    if (!bin.exists()) {
      throw new AssumptionViolatedException("No exiftool installed.");
    }
    return new ExifTool(bin);
  }
}
