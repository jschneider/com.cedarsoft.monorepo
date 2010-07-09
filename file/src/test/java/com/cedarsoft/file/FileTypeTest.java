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

package com.cedarsoft.file;

import org.junit.*;

import static org.junit.Assert.*;

/**
 *
 */
public class FileTypeTest {
  @Test
  public void testGetFileName() {
    assertEquals( "asdf", FileTypeRegistry.JPEG.getFileName( "asdf.jpg" ).getBaseName().getName() );
    assertEquals( "jpg", FileTypeRegistry.JPEG.getFileName( "asdf.jpg" ).getExtension().getExtension() );
    assertEquals( ".", FileTypeRegistry.JPEG.getFileName( "asdf.jpg" ).getExtension().getDelimiter() );

    assertEquals( "asdf_", FileTypeRegistry.JPEG.getBaseName( "asdf_.jpg" ) );
    assertEquals( "asdf_", FileTypeRegistry.JPEG.getBaseName( "asdf_.jpeg" ) );
    assertEquals( "asdf_", FileTypeRegistry.JPEG.getBaseName( "asdf_.JPEG" ) );
    assertEquals( "Asdf_", FileTypeRegistry.JPEG.getBaseName( "Asdf_.JPEG" ) );

    assertEquals( "asdf", FileTypeRegistry.LIGHT_ZONE.getBaseName( "asdf_lzn.jpg" ) );
    assertEquals( "asdf_", FileTypeRegistry.LIGHT_ZONE.getBaseName( "asdf__lzn.jpg" ) );
    assertEquals( "asdf_", FileTypeRegistry.LIGHT_ZONE.getBaseName( "asdf__LZN.JPG" ) );
  }

  @Test
  public void testGetBase() {
    assertEquals( "asdf", FileTypeRegistry.JPEG.getBaseName( "asdf.jpg" ) );
    assertEquals( "asdf_", FileTypeRegistry.JPEG.getBaseName( "asdf_.jpg" ) );
    assertEquals( "asdf_", FileTypeRegistry.JPEG.getBaseName( "asdf_.jpeg" ) );
    assertEquals( "asdf_", FileTypeRegistry.JPEG.getBaseName( "asdf_.JPEG" ) );
    assertEquals( "Asdf_", FileTypeRegistry.JPEG.getBaseName( "Asdf_.JPEG" ) );

    assertEquals( "asdf", FileTypeRegistry.LIGHT_ZONE.getBaseName( "asdf_lzn.jpg" ) );
    assertEquals( "asdf_", FileTypeRegistry.LIGHT_ZONE.getBaseName( "asdf__lzn.jpg" ) );
    assertEquals( "asdf_", FileTypeRegistry.LIGHT_ZONE.getBaseName( "asdf__LZN.JPG" ) );
  }

  @Test
  public void testGetExtension() {
    assertEquals( "jpg", FileTypeRegistry.JPEG.getExtension( "asdf.jpg" ).getExtension() );
    assertEquals( "jpg", FileTypeRegistry.JPEG.getExtension( "asdf_.jpg" ).getExtension() );
    assertEquals( "jpeg", FileTypeRegistry.JPEG.getExtension( "asdf_.jpeg" ).getExtension() );
    assertEquals( "jpeg", FileTypeRegistry.JPEG.getExtension( "asdf_.JPEG" ).getExtension() );
    assertEquals( "jpeg", FileTypeRegistry.JPEG.getExtension( "Asdf_.JPEG" ).getExtension() );

    assertEquals( "lzn.jpg", FileTypeRegistry.LIGHT_ZONE.getExtension( "asdf_lzn.jpg" ).getExtension() );
    assertEquals( "lzn.jpg", FileTypeRegistry.LIGHT_ZONE.getExtension( "asdf__lzn.jpg" ).getExtension() );
    assertEquals( "lzn.jpg", FileTypeRegistry.LIGHT_ZONE.getExtension( "asdf__LZN.JPG" ).getExtension() );
  }

  @Test
  public void testGetDelimiter() {
    assertEquals( ".", FileTypeRegistry.JPEG.getExtension( "asdf.jpg" ).getDelimiter() );
    assertEquals( ".", FileTypeRegistry.JPEG.getExtension( "asdf_.jpg" ).getDelimiter() );
    assertEquals( ".", FileTypeRegistry.JPEG.getExtension( "asdf_.jpeg" ).getDelimiter() );
    assertEquals( ".", FileTypeRegistry.JPEG.getExtension( "asdf_.JPEG" ).getDelimiter() );
    assertEquals( ".", FileTypeRegistry.JPEG.getExtension( "Asdf_.JPEG" ).getDelimiter() );

    assertEquals( "_", FileTypeRegistry.LIGHT_ZONE.getExtension( "asdf_lzn.jpg" ).getDelimiter() );
    assertEquals( "_", FileTypeRegistry.LIGHT_ZONE.getExtension( "asdf__lzn.jpg" ).getDelimiter() );
    assertEquals( "_", FileTypeRegistry.LIGHT_ZONE.getExtension( "asdf__LZN.JPG" ).getDelimiter() );
  }

  @Test
  public void testExtension() {
    FileTypeRegistry fileTypeRegistry = new FileTypeRegistry();

    assertSame( fileTypeRegistry.get( new FileName( "asdf", ".", "jpg" ) ), FileTypeRegistry.JPEG );
    assertSame( fileTypeRegistry.get( new FileName( "asdf", ".", "cr2" ) ), FileTypeRegistry.RAW_CANON );

    assertSame( fileTypeRegistry.get( "asdf.jpg" ), FileTypeRegistry.JPEG );
    assertSame( fileTypeRegistry.get( "asdf.cr2" ), FileTypeRegistry.RAW_CANON );
    assertSame( fileTypeRegistry.get( "asdf_lzn.jpg" ), FileTypeRegistry.LIGHT_ZONE );
  }

  @Test
  public void testLightZone() {
    FileTypeRegistry fileTypeRegistry = new FileTypeRegistry();

    assertEquals( FileTypeRegistry.LIGHT_ZONE, fileTypeRegistry.get( "asdf_lzn.jpg" ) );
    assertEquals( "asdf", fileTypeRegistry.get( "asdf_lzn.jpg" ).getFileName( "asdf_lzn.jpg" ).getBaseName().getName() );
    assertEquals( "_", fileTypeRegistry.get( "asdf_lzn.jpg" ).getFileName( "asdf_lzn.jpg" ).getExtension().getDelimiter() );
    assertEquals( "lzn.jpg", fileTypeRegistry.get( "asdf_lzn.jpg" ).getFileName( "asdf_lzn.jpg" ).getExtension().getExtension() );

    assertEquals( "asdf", new FileName( "asdf", "_", "lzn.jpg" ).getBaseName().getName() );
    assertEquals( "_", new FileName( "asdf", "_", "lzn.jpg" ).getExtension().getDelimiter() );
    assertEquals( "lzn.jpg", new FileName( "asdf", "_", "lzn.jpg" ).getExtension().getExtension() );

    assertTrue( FileTypeRegistry.LIGHT_ZONE.matches( "my_lzn.jpg" ) );
  }

  @Test
  public void testDependentFiles() {
    assertFalse( FileTypeRegistry.GIMP.isDependentType() );
    assertFalse( FileTypeRegistry.JPEG.isDependentType() );
    assertTrue( FileTypeRegistry.LIGHT_ZONE.isDependentType() );
  }
}
