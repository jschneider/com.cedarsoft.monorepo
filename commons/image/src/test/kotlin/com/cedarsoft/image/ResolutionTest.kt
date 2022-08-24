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
package com.cedarsoft.image

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 *
 */
class ResolutionTest {
  @Test
  fun testIt() {
    val resolution = Resolution(1024, 768)
    assertEquals(1024, resolution.width.toLong())
    assertEquals(768, resolution.height.toLong())
    assertEquals(AspectRatio.AR_4_3, resolution.aspectRatio)
  }

  @Test
  fun testAspectRatio() {
    assertEquals(AspectRatio.AR_1_1.ratio, 1.0 / 1.0, 0.0)
    assertEquals(AspectRatio.AR_3_2.ratio, 3.0 / 2.0, 0.0)
    assertEquals(AspectRatio.AR_4_3.ratio, 4.0 / 3.0, 0.0)
    assertEquals(AspectRatio.AR_5_3.ratio, 5.0 / 3.0, 0.0)
    assertEquals(AspectRatio.AR_5_4.ratio, 5.0 / 4.0, 0.0)
    assertEquals(AspectRatio.AR_16_9.ratio, 16.0 / 9.0, 0.0)
    assertEquals(AspectRatio.AR_16_10.ratio, 16.0 / 10.0, 0.0)
  }

  @Test
  fun testSelect() {
    assertEquals(AspectRatio.AR_4_3, AspectRatio[4.0, 3.0])
    assertEquals(AspectRatio.AR_4_3, AspectRatio[400.0, 300.0])
    assertEquals(AspectRatio.AR_4_3, AspectRatio[1024.0, 768.0])
    assertEquals(AspectRatio.AR_5_4, AspectRatio[1280.0, 1024.0])
    assertEquals(1.0, AspectRatio[1.0, 17.0].widthFactor, 0.0)
    assertEquals(17.0, AspectRatio[1.0, 17.0].heightFactor, 0.0)
  }

  @Test
  fun testNikon() {
    assertEquals(AspectRatio.AR_3_2, AspectRatio[4256.0, 2832.0]) //D3
    assertEquals(AspectRatio.AR_3_2, AspectRatio[6048.0, 4032.0]) //D3X
    assertEquals(AspectRatio.AR_3_2, AspectRatio[4256.0, 2832.0]) //D3s
    assertEquals(AspectRatio.AR_3_2, AspectRatio[4256.0, 2832.0]) //D700
  }

  @Test
  fun testCanonRes() {
    assertEquals(AspectRatio.AR_3_2, AspectRatio[3888.0, 2592.0]) //1000D
    assertEquals(AspectRatio.AR_3_2, AspectRatio[3072.0, 2048.0]) //300D
    assertEquals(AspectRatio.AR_3_2, AspectRatio[3456.0, 2304.0]) //350D
    assertEquals(AspectRatio.AR_3_2, AspectRatio[3888.0, 2592.0]) //400D
    assertEquals(AspectRatio.AR_3_2, AspectRatio[4272.0, 2848.0]) //450D
    assertEquals(AspectRatio.AR_3_2, AspectRatio[4752.0, 3168.0]) //500D
    assertEquals(AspectRatio.AR_3_2, AspectRatio[5184.0, 3456.0]) //550D
    assertEquals(AspectRatio.AR_3_2, AspectRatio[4752.0, 3168.0]) //50D
    assertEquals(AspectRatio.AR_3_2, AspectRatio[3888.0, 2592.0]) //40D
    assertEquals(AspectRatio.AR_3_2, AspectRatio[3504.0, 2336.0]) //30D
    assertEquals(AspectRatio.AR_3_2, AspectRatio[5184.0, 3456.0]) //7D
    assertEquals(AspectRatio.AR_3_2, AspectRatio[4368.0, 2912.0]) //5D
    assertEquals(AspectRatio.AR_3_2, AspectRatio[5616.0, 3744.0]) //5D2
    assertEquals(AspectRatio.AR_3_2, AspectRatio[3888.0, 2592.0]) //1D3
    assertEquals(AspectRatio.AR_3_2, AspectRatio[4896.0, 3264.0]) //1D4
    assertEquals(AspectRatio.AR_3_2, AspectRatio[2464.0, 1648.0]) //1D
    assertEquals(AspectRatio.AR_3_2, AspectRatio[4064.0, 2704.0]) //1Ds
    assertEquals(AspectRatio.AR_3_2, AspectRatio[4992.0, 3328.0]) //1Ds2
    assertEquals(AspectRatio.AR_3_2, AspectRatio[5616.0, 3744.0]) //1Ds3
  }

  @Test
  fun testComparable() {
    Assertions.assertTrue(Resolution(100, 100) == Resolution(100, 100))
    Assertions.assertTrue(Resolution(100, 100).compareTo(Resolution(100, 100)) == 0)
    Assertions.assertTrue(Resolution(100, 100).compareTo(Resolution(100, 101)) < 0)
    Assertions.assertTrue(Resolution(100, 100).compareTo(Resolution(101, 101)) < 0)
    Assertions.assertTrue(Resolution(100, 100).compareTo(Resolution(101, 100)) < 0)
    Assertions.assertTrue(Resolution(100, 100).compareTo(Resolution(100, 99)) > 0)
    Assertions.assertTrue(Resolution(100, 100).compareTo(Resolution(99, 99)) > 0)
    Assertions.assertTrue(Resolution(100, 100).compareTo(Resolution(99, 100)) > 0)
    Assertions.assertTrue(Resolution(100, 100).compareTo(Resolution(101, 99)) < 0)
    Assertions.assertTrue(Resolution(100, 100).compareTo(Resolution(99, 101)) > 0)
  }
}
