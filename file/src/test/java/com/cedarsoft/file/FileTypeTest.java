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
    assertEquals( FileTypeRegistry.JPEG.getFileName( "asdf.jpg" ).getBaseName().getName(), "asdf" );
    assertEquals( FileTypeRegistry.JPEG.getFileName( "asdf.jpg" ).getExtension().getExtension(), "jpg" );
    assertEquals( FileTypeRegistry.JPEG.getFileName( "asdf.jpg" ).getExtension().getDelimiter(), "." );

    assertEquals( FileTypeRegistry.JPEG.getBaseName( "asdf_.jpg" ), "asdf_" );
    assertEquals( FileTypeRegistry.JPEG.getBaseName( "asdf_.jpeg" ), "asdf_" );
    assertEquals( FileTypeRegistry.JPEG.getBaseName( "asdf_.JPEG" ), "asdf_" );
    assertEquals( FileTypeRegistry.JPEG.getBaseName( "Asdf_.JPEG" ), "Asdf_" );

    assertEquals( FileTypeRegistry.LIGHT_ZONE.getBaseName( "asdf_lzn.jpg" ), "asdf" );
    assertEquals( FileTypeRegistry.LIGHT_ZONE.getBaseName( "asdf__lzn.jpg" ), "asdf_" );
    assertEquals( FileTypeRegistry.LIGHT_ZONE.getBaseName( "asdf__LZN.JPG" ), "asdf_" );
  }

  @Test
  public void testGetBase() {
    assertEquals( FileTypeRegistry.JPEG.getBaseName( "asdf.jpg" ), "asdf" );
    assertEquals( FileTypeRegistry.JPEG.getBaseName( "asdf_.jpg" ), "asdf_" );
    assertEquals( FileTypeRegistry.JPEG.getBaseName( "asdf_.jpeg" ), "asdf_" );
    assertEquals( FileTypeRegistry.JPEG.getBaseName( "asdf_.JPEG" ), "asdf_" );
    assertEquals( FileTypeRegistry.JPEG.getBaseName( "Asdf_.JPEG" ), "Asdf_" );

    assertEquals( FileTypeRegistry.LIGHT_ZONE.getBaseName( "asdf_lzn.jpg" ), "asdf" );
    assertEquals( FileTypeRegistry.LIGHT_ZONE.getBaseName( "asdf__lzn.jpg" ), "asdf_" );
    assertEquals( FileTypeRegistry.LIGHT_ZONE.getBaseName( "asdf__LZN.JPG" ), "asdf_" );
  }

  @Test
  public void testGetExtension() {
    assertEquals( FileTypeRegistry.JPEG.getExtension( "asdf.jpg" ).getExtension(), "jpg" );
    assertEquals( FileTypeRegistry.JPEG.getExtension( "asdf_.jpg" ).getExtension(), "jpg" );
    assertEquals( FileTypeRegistry.JPEG.getExtension( "asdf_.jpeg" ).getExtension(), "jpeg" );
    assertEquals( FileTypeRegistry.JPEG.getExtension( "asdf_.JPEG" ).getExtension(), "jpeg" );
    assertEquals( FileTypeRegistry.JPEG.getExtension( "Asdf_.JPEG" ).getExtension(), "jpeg" );

    assertEquals( FileTypeRegistry.LIGHT_ZONE.getExtension( "asdf_lzn.jpg" ).getExtension(), "lzn.jpg" );
    assertEquals( FileTypeRegistry.LIGHT_ZONE.getExtension( "asdf__lzn.jpg" ).getExtension(), "lzn.jpg" );
    assertEquals( FileTypeRegistry.LIGHT_ZONE.getExtension( "asdf__LZN.JPG" ).getExtension(), "lzn.jpg" );
  }

  @Test
  public void testGetDelimiter() {
    assertEquals( FileTypeRegistry.JPEG.getExtension( "asdf.jpg" ).getDelimiter(), "." );
    assertEquals( FileTypeRegistry.JPEG.getExtension( "asdf_.jpg" ).getDelimiter(), "." );
    assertEquals( FileTypeRegistry.JPEG.getExtension( "asdf_.jpeg" ).getDelimiter(), "." );
    assertEquals( FileTypeRegistry.JPEG.getExtension( "asdf_.JPEG" ).getDelimiter(), "." );
    assertEquals( FileTypeRegistry.JPEG.getExtension( "Asdf_.JPEG" ).getDelimiter(), "." );

    assertEquals( FileTypeRegistry.LIGHT_ZONE.getExtension( "asdf_lzn.jpg" ).getDelimiter(), "_" );
    assertEquals( FileTypeRegistry.LIGHT_ZONE.getExtension( "asdf__lzn.jpg" ).getDelimiter(), "_" );
    assertEquals( FileTypeRegistry.LIGHT_ZONE.getExtension( "asdf__LZN.JPG" ).getDelimiter(), "_" );
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

    assertEquals( fileTypeRegistry.get( "asdf_lzn.jpg" ), FileTypeRegistry.LIGHT_ZONE );
    assertEquals( fileTypeRegistry.get( "asdf_lzn.jpg" ).getFileName( "asdf_lzn.jpg" ).getBaseName().getName(), "asdf" );
    assertEquals( fileTypeRegistry.get( "asdf_lzn.jpg" ).getFileName( "asdf_lzn.jpg" ).getExtension().getDelimiter(), "_" );
    assertEquals( fileTypeRegistry.get( "asdf_lzn.jpg" ).getFileName( "asdf_lzn.jpg" ).getExtension().getExtension(), "lzn.jpg" );

    assertEquals( new FileName( "asdf", "_", "lzn.jpg" ).getBaseName().getName(), "asdf" );
    assertEquals( new FileName( "asdf", "_", "lzn.jpg" ).getExtension().getDelimiter(), "_" );
    assertEquals( new FileName( "asdf", "_", "lzn.jpg" ).getExtension().getExtension(), "lzn.jpg" );

    assertTrue( FileTypeRegistry.LIGHT_ZONE.matches( "my_lzn.jpg" ) );
  }

  @Test
  public void testDependentFiles() {
    assertFalse( FileTypeRegistry.GIMP.isDependentType() );
    assertFalse( FileTypeRegistry.JPEG.isDependentType() );
    assertTrue( FileTypeRegistry.LIGHT_ZONE.isDependentType() );
  }
}
