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
package com.cedarsoft.io

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

/**
 *
 */
class RelativePathFinderTest {
  private lateinit var s: String

  @BeforeEach
  fun setUp() {
    s = File.separator
  }

  @Test
  fun testBug() {
    assertEquals(
      ".." + s + ".." + s + ".." + s + ".." + s + ".." + s + ".." + s + "repository" + s + "10" + s + "6d775ae5eda36d969c9ab21068a32803b2ebe96b2ad581ab4e84fe0cc9d34b" + s + "data.CR2", RelativePathFinder.getRelativePath(
        "" + s + "media" + s + "tamar" + s + "data" + s + "fotos" + s + "collustra" + s + "repository" + s + "10" + s + "6d775ae5eda36d969c9ab21068a32803b2ebe96b2ad581ab4e84fe0cc9d34b" + s + "data.CR2",
        "" + s + "media" + s + "tamar" + s + "data" + s + "fotos" + s + "collustra" + s + "links" + s + "by-date" + s + "UTC" + s + "2009" + s + "05" + s + "20",
        "" + s + ""
      )
    )
  }

  @Test
  fun testGetRelativePath() {
    assertEquals(".." + s + ".." + s + ".." + s + "a", RelativePathFinder.getRelativePath("" + s + "tmp" + s + "a", "" + s + "tmp" + s + "other" + s + "a" + s + "b", "" + s + ""))
    assertEquals(".." + s + ".." + s + ".." + s + "referenced", RelativePathFinder.getRelativePath("" + s + "tmp" + s + "referenced" + s + "", "" + s + "tmp" + s + "356406" + s + "a" + s + "b", "" + s + ""))
    assertEquals(".." + s + ".." + s + ".." + s + "referenced", RelativePathFinder.getRelativePath("" + s + "tmp" + s + "referenced", "" + s + "tmp" + s + "356406" + s + "a" + s + "b", "" + s + ""))
  }

  @Test
  fun testGetRelativePathWin() {
    assertEquals("..\\..\\..\\a", RelativePathFinder.getRelativePath("C:\\tmp\\a", "C:\\tmp\\other\\a\\b", "\\"))
    assertEquals("..\\..\\..\\referenced", RelativePathFinder.getRelativePath("C:\\tmp\\referenced\\", "C:\\tmp\\356406\\a\\b", "\\"))
    assertEquals("..\\..\\..\\referenced", RelativePathFinder.getRelativePath("C:\\tmp\\referenced", "C:\\tmp\\356406\\a\\b", "\\"))
  }

  @Test
  fun testGetRelativePath3() {
    assertEquals(".", RelativePathFinder.getRelativePath("" + s + "a", "" + s + "a", "" + s + ""))
    assertEquals("..", RelativePathFinder.getRelativePath("" + s + "a", "" + s + "a" + s + "b", "" + s + ""))
    assertEquals("..$s..", RelativePathFinder.getRelativePath("" + s + "a", "" + s + "a" + s + "b" + s + "c", "" + s + ""))
    assertEquals("..$s..$s..", RelativePathFinder.getRelativePath("" + s + "a", "" + s + "a" + s + "b" + s + "c" + s + "d", "" + s + ""))
    assertEquals(".." + s + ".." + s + ".." + s + "a", RelativePathFinder.getRelativePath("" + s + "a", "" + s + "other" + s + "a" + s + "b", "" + s + ""))
  }

  @Test
  fun testGetRelativePath2() {
    assertEquals(".." + s + ".." + s + "b" + s + "c", RelativePathFinder.getRelativePath("" + s + "a" + s + "b" + s + "c", "" + s + "a" + s + "x" + s + "y" + s + "", "" + s + ""))
    assertEquals(".." + s + ".." + s + "b", RelativePathFinder.getRelativePath("" + s + "a" + s + "b" + s + "", "" + s + "a" + s + "x" + s + "y" + s + "", "" + s + ""))
    assertEquals("..$s..", RelativePathFinder.getRelativePath("" + s + "a" + s + "", "" + s + "a" + s + "x" + s + "y" + s + "", "" + s + ""))
  }

  @Test
  fun testDirectParent() {
    assertEquals("stuff" + s + "xyz.dat", RelativePathFinder.getRelativePath("" + s + "var" + s + "data" + s + "stuff" + s + "xyz.dat", "" + s + "var" + s + "data" + s + "", "" + s + ""))
    assertEquals("stuff" + s + "xyz.dat", RelativePathFinder.getRelativePath("" + s + "var" + s + "data" + s + "stuff" + s + "xyz.dat" + s + "", "" + s + "var" + s + "data" + s + "", "" + s + ""))
    assertEquals("stuff" + s + "xyz.dat", RelativePathFinder.getRelativePath("" + s + "var" + s + "data" + s + "stuff" + s + "xyz.dat" + s + "", "" + s + "var" + s + "data", "" + s + ""))
    assertEquals("stuff" + s + "xyz.dat", RelativePathFinder.getRelativePath("" + s + "var" + s + "data" + s + "stuff" + s + "xyz.dat", "" + s + "var" + s + "data", "" + s + ""))
  }

  @Test
  fun testFile() {
    assertEquals("stuff" + s + "xyz.dat", RelativePathFinder.getRelativePath(File("" + s + "var" + s + "data" + s + "stuff" + s + "xyz.dat"), File("" + s + "var" + s + "data" + s + ""), "" + s + "").path)
    assertEquals(".." + s + ".." + s + "b" + s + "c", RelativePathFinder.getRelativePath(File("" + s + "a" + s + "b" + s + "c"), File("" + s + "a" + s + "x" + s + "y" + s + ""), "" + s + "").path)
  }
}
