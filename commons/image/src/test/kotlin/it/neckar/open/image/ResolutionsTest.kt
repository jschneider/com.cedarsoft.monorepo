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
package it.neckar.open.image

import org.assertj.core.api.Assertions
import org.assertj.core.api.Fail
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.awt.Dimension
import java.util.Arrays

/**
 *
 */
class ResolutionsTest {
  private var resolutions: Resolutions? = null

  @BeforeEach
  @Throws(Exception::class)
  fun setUp() {
    resolutions = Resolutions()
  }

  @Test
  fun testScreenResolutions() {
    assertEquals(25, resolutions!!.getScreenResolutions().size.toLong())
    for (resolution in resolutions!!.getScreenResolutions()) {
      assertTrue(resolution.width > 0, resolution.toString())
      assertTrue(resolution.height > 0, resolution.toString())
    }
  }

  @Test
  fun testSorting() {
    val first = resolutions!!.getScreenResolutions()[0]
    assertTrue(first.compareTo(resolutions!!.getScreenResolutions()[1]) > 0)
    val screenResolutions = resolutions!!.getScreenResolutions()
    for (i in screenResolutions.indices) {
      val resolution = screenResolutions[i]
      for (j in i + 1 until screenResolutions.size) {
        assertTrue(resolution.compareTo(screenResolutions[j]) > 0)
      }
    }
  }

  @Test
  fun testNone() {
    try {
      assertEquals(resolutions!!.getBest(-10, -10), Dimension(160, 160))
      org.junit.jupiter.api.Assertions.fail("Where is the Exception")
    } catch (ignore: IllegalArgumentException) {
    }
    try {
      assertEquals(resolutions!!.getBest(10, 10), Dimension(160, 160))
      Fail.fail<Any>("Where is the Exception")
    } catch (e: IllegalArgumentException) {
      Assertions.assertThat(e).hasMessage("No resolution found for 10/10")
    }
  }

  @Test
  fun testSizes() {
    assertEquals(Resolutions._160_160, resolutions!!.getBest(160, 160))
    assertEquals(Resolutions._160_160, resolutions!!.getBest(161, 161))
    assertEquals(Resolutions._4096_2304, resolutions!!.getBest(Int.MAX_VALUE, Int.MAX_VALUE))
  }

  @Test
  fun testConstructor() {
    val small = Resolution(100, 100)
    resolutions = Resolutions(Arrays.asList(small))
    assertEquals(resolutions!!.getBest(160, 160), small)
    assertEquals(resolutions!!.getBest(161, 161), small)
    assertEquals(resolutions!!.getBest(Int.MAX_VALUE, Int.MAX_VALUE), small)
  }
}
