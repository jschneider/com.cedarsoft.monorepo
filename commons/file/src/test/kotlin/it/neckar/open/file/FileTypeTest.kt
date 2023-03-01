/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.cedarsoft.org/gpl3ce
 * (GPL 3 with Classpath Exception)
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
package it.neckar.open.file

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

/**
 *
 */
class FileTypeTest {
  @Test
  fun testInva() {
    try {
      FileType("asdf", "asdf", true)
      Assertions.fail("Where is the Exception")
    } catch (ignore: IllegalArgumentException) {
    }
  }

  @Test
  fun testFileName() {
    val f = File("asdf.jpg")
    assertEquals("asdf", FileTypeRegistry.JPEG.getFileName(f).baseName.name)
  }

  @Test
  fun testContentType() {
    assertEquals("image/jpeg", FileTypeRegistry.JPEG.contentType)
    assertEquals("image/xcf", FileTypeRegistry.GIMP.contentType)
    assertEquals("image/psd", FileTypeRegistry.PHOTO_SHOP.contentType)
    assertEquals("image/cr2", FileTypeRegistry.RAW_CANON.contentType)
    assertEquals("application/lightzone", FileTypeRegistry.LIGHT_ZONE.contentType)
  }

  @Test
  fun testDefaultExtension() {
    assertEquals("jpg", FileTypeRegistry.JPEG.defaultExtension.extension)
    assertEquals(".jpg", FileTypeRegistry.JPEG.defaultExtension.combined)
    Assertions.assertTrue(FileTypeRegistry.JPEG.isDefaultExtension(FileTypeRegistry.JPEG.defaultExtension))
  }

  @Test
  fun testGetFileName() {
    assertEquals("asdf", FileTypeRegistry.JPEG.getFileName("asdf.jpg").baseName.name)
    assertEquals("jpg", FileTypeRegistry.JPEG.getFileName("asdf.jpg").extension.extension)
    assertEquals(".", FileTypeRegistry.JPEG.getFileName("asdf.jpg").extension.delimiter)
    assertEquals("asdf_", FileTypeRegistry.JPEG.getBaseName("asdf_.jpg"))
    assertEquals("asdf_", FileTypeRegistry.JPEG.getBaseName("asdf_.jpeg"))
    assertEquals("asdf_", FileTypeRegistry.JPEG.getBaseName("asdf_.JPEG"))
    assertEquals("Asdf_", FileTypeRegistry.JPEG.getBaseName("Asdf_.JPEG"))
    assertEquals("asdf", FileTypeRegistry.LIGHT_ZONE.getBaseName("asdf_lzn.jpg"))
    assertEquals("asdf_", FileTypeRegistry.LIGHT_ZONE.getBaseName("asdf__lzn.jpg"))
    assertEquals("asdf_", FileTypeRegistry.LIGHT_ZONE.getBaseName("asdf__LZN.JPG"))
    assertEquals("_", FileTypeRegistry.JPEG.getBaseName("_.jpeg"))
    assertEquals("", FileTypeRegistry.JPEG.getBaseName(".jpeg"))
    try {
      FileTypeRegistry.JPEG.getBaseName("jpeg")
      Assertions.fail("Where is the Exception")
    } catch (ignore: IllegalArgumentException) {
    }
  }

  @Test
  fun testGetBase() {
    assertEquals("asdf", FileTypeRegistry.JPEG.getBaseName("asdf.jpg"))
    assertEquals("asdf_", FileTypeRegistry.JPEG.getBaseName("asdf_.jpg"))
    assertEquals("asdf_", FileTypeRegistry.JPEG.getBaseName("asdf_.jpeg"))
    assertEquals("asdf_", FileTypeRegistry.JPEG.getBaseName("asdf_.JPEG"))
    assertEquals("Asdf_", FileTypeRegistry.JPEG.getBaseName("Asdf_.JPEG"))
    assertEquals("asdf", FileTypeRegistry.LIGHT_ZONE.getBaseName("asdf_lzn.jpg"))
    assertEquals("asdf_", FileTypeRegistry.LIGHT_ZONE.getBaseName("asdf__lzn.jpg"))
    assertEquals("asdf_", FileTypeRegistry.LIGHT_ZONE.getBaseName("asdf__LZN.JPG"))
  }

  @Test
  fun testGetExtension() {
    assertEquals("jpg", FileTypeRegistry.JPEG.getExtension("asdf.jpg").extension)
    assertEquals("jpg", FileTypeRegistry.JPEG.getExtension("asdf_.jpg").extension)
    assertEquals("jpeg", FileTypeRegistry.JPEG.getExtension("asdf_.jpeg").extension)
    assertEquals("JPEG", FileTypeRegistry.JPEG.getExtension("asdf_.JPEG").extension)
    assertEquals("JPEG", FileTypeRegistry.JPEG.getExtension("Asdf_.JPEG").extension)
    assertEquals(2, FileTypeRegistry.JPEG.extensions.size.toLong())
    assertEquals(".jpg", FileTypeRegistry.JPEG.defaultExtension.combined)
    assertEquals("lzn.jpg", FileTypeRegistry.LIGHT_ZONE.getExtension("asdf_lzn.jpg").extension)
    assertEquals("lzn.jpg", FileTypeRegistry.LIGHT_ZONE.getExtension("asdf__lzn.jpg").extension)
    assertEquals("LZN.JPG", FileTypeRegistry.LIGHT_ZONE.getExtension("asdf__LZN.JPG").extension)
    assertEquals(1, FileTypeRegistry.LIGHT_ZONE.extensions.size.toLong())
  }

  @Test
  fun testGetDelimiter() {
    assertEquals(".", FileTypeRegistry.JPEG.getExtension("asdf.jpg").delimiter)
    assertEquals(".", FileTypeRegistry.JPEG.getExtension("asdf_.jpg").delimiter)
    assertEquals(".", FileTypeRegistry.JPEG.getExtension("asdf_.jpeg").delimiter)
    assertEquals(".", FileTypeRegistry.JPEG.getExtension("asdf_.JPEG").delimiter)
    assertEquals(".", FileTypeRegistry.JPEG.getExtension("Asdf_.JPEG").delimiter)
    assertEquals("_", FileTypeRegistry.LIGHT_ZONE.getExtension("asdf_lzn.jpg").delimiter)
    assertEquals("_", FileTypeRegistry.LIGHT_ZONE.getExtension("asdf__lzn.jpg").delimiter)
    assertEquals("_", FileTypeRegistry.LIGHT_ZONE.getExtension("asdf__LZN.JPG").delimiter)
  }

  @Test
  fun testDependentFiles() {
    Assertions.assertFalse(FileTypeRegistry.GIMP.isDependentType)
    Assertions.assertFalse(FileTypeRegistry.JPEG.isDependentType)
    Assertions.assertTrue(FileTypeRegistry.LIGHT_ZONE.isDependentType)
  }
}
