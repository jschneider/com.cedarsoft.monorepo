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

package com.cedarsoft.io;

import org.junit.*;

import java.io.File;

import static org.junit.Assert.*;

/**
 *
 */
public class RelativePathFinderTest {
  @Test
  public void testBug() {
    assertEquals( "../../../../../../repository/10/6d775ae5eda36d969c9ab21068a32803b2ebe96b2ad581ab4e84fe0cc9d34b/data.CR2", RelativePathFinder.getRelativePath( "/media/tamar/data/fotos/collustra/repository/10/6d775ae5eda36d969c9ab21068a32803b2ebe96b2ad581ab4e84fe0cc9d34b/data.CR2", "/media/tamar/data/fotos/collustra/links/by-date/UTC/2009/05/20", "/" ) );
  }

  @Test
  public void testGetRelativePath() {
    assertEquals( "../../../a", RelativePathFinder.getRelativePath( "/tmp/a", "/tmp/other/a/b", "/" ) );
    assertEquals( "../../../referenced", RelativePathFinder.getRelativePath( "/tmp/referenced/", "/tmp/356406/a/b", "/" ) );
    assertEquals( "../../../referenced", RelativePathFinder.getRelativePath( "/tmp/referenced", "/tmp/356406/a/b", "/" ) );
  }

  @Test
  public void testGetRelativePathWin() {
    assertEquals( "..\\..\\..\\a", RelativePathFinder.getRelativePath( "C:\\tmp\\a", "C:\\tmp\\other\\a\\b", "\\" ) );
    assertEquals( "..\\..\\..\\referenced", RelativePathFinder.getRelativePath( "C:\\tmp\\referenced\\", "C:\\tmp\\356406\\a\\b", "\\" ) );
    assertEquals( "..\\..\\..\\referenced", RelativePathFinder.getRelativePath( "C:\\tmp\\referenced", "C:\\tmp\\356406\\a\\b", "\\" ) );
  }

  @Test
  public void testGetRelativePath3() {
    assertEquals( ".", RelativePathFinder.getRelativePath( "/a", "/a", "/" ) );
    assertEquals( "..", RelativePathFinder.getRelativePath( "/a", "/a/b", "/" ) );
    assertEquals( "../..", RelativePathFinder.getRelativePath( "/a", "/a/b/c", "/" ) );
    assertEquals( "../../..", RelativePathFinder.getRelativePath( "/a", "/a/b/c/d", "/" ) );


    assertEquals( "../../../a", RelativePathFinder.getRelativePath( "/a", "/other/a/b", "/" ) );
  }

  @Test
  public void testGetRelativePath2() {
    assertEquals( "../../b/c", RelativePathFinder.getRelativePath( "/a/b/c", "/a/x/y/", "/" ) );
    assertEquals( "../../b", RelativePathFinder.getRelativePath( "/a/b/", "/a/x/y/", "/" ) );
    assertEquals( "../..", RelativePathFinder.getRelativePath( "/a/", "/a/x/y/", "/" ) );
  }

  @Test
  public void testDirectParent() {
    assertEquals( "stuff/xyz.dat", RelativePathFinder.getRelativePath( "/var/data/stuff/xyz.dat", "/var/data/", "/" ) );
    assertEquals( "stuff/xyz.dat", RelativePathFinder.getRelativePath( "/var/data/stuff/xyz.dat/", "/var/data/", "/" ) );
    assertEquals( "stuff/xyz.dat", RelativePathFinder.getRelativePath( "/var/data/stuff/xyz.dat/", "/var/data", "/" ) );
    assertEquals( "stuff/xyz.dat", RelativePathFinder.getRelativePath( "/var/data/stuff/xyz.dat", "/var/data", "/" ) );
  }

  @Test
  public void testFile() {
    assertEquals( "stuff/xyz.dat", RelativePathFinder.getRelativePath( new File( "/var/data/stuff/xyz.dat" ), new File( "/var/data/" ), "/" ).getPath() );
    assertEquals( "../../b/c", RelativePathFinder.getRelativePath( new File( "/a/b/c" ), new File( "/a/x/y/" ), "/" ).getPath() );
  }
}
