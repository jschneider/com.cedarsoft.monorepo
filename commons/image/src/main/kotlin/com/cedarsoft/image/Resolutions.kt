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

/**
 * Contains common dimensions for different screens
 */
class Resolutions @JvmOverloads constructor(
  screenResolutions: Iterable<Resolution> = listOf(
    FOUR_K,
    QUXGA,
    QXGA,
    WUXGA,
    HD_1080,
    WSXGA_PLUS,
    UXGA,
    WSXGA,
    WXGA,
    SXGA,
    SXGA_960,
    WXGA_800,
    WXGA_768,
    HD_720,
    XGA_PLUS,
    XGA,
    SVGA,
    DVD,
    VGA,  //new Resolution( "", 480, 480 ),
    HVGA,
    WQVGA,
    QVGA,  //new Resolution( "", 256, 256 ),
    HQVGA,  //new Resolution( "", 208, 208 ),
    LoRES,
    QQVGA
  ),
) {

  private val screenResolutions: List<Resolution> = screenResolutions
    .toList()
    .sorted()
    .reversed()

  /**
   * Returns the common dimension that is the same or smaller
   *
   * @param maxWidth  the max width
   * @param maxHeight the max height
   * @return the best dimension for the max width/height
   */
  fun getBest(maxWidth: Int, maxHeight: Int): Resolution {
    for (resolution in screenResolutions) {
      if (maxWidth >= resolution.width && maxHeight >= resolution.height) {
        return resolution
      }
    }
    throw IllegalArgumentException("No resolution found for $maxWidth/$maxHeight")
  }

  /**
   * Returns the common dimensions
   *
   * @return the common dimensions
   */
  fun getScreenResolutions(): List<Resolution> {
    return screenResolutions
  }

  @Suppress("ObjectPropertyName")
  companion object {
    val A3_300: Resolution = Resolution(3508, 4961)

    val A4_300: Resolution = Resolution(2480, 3508)

    val A4_72: Resolution = Resolution(595, 842)

    val FOUR_K: Resolution = Resolution(4096, 2304)

    val _4096_2304: Resolution = FOUR_K


    val QUXGA: Resolution = Resolution(3200, 2400)


    val _3200_2400: Resolution = QUXGA


    val WQXGA: Resolution = Resolution(2560, 1600)


    val _2560_1600: Resolution = WQXGA


    val WQHD: Resolution = Resolution(2560, 1440)


    val _2560_1440: Resolution = WQHD


    val QXGA: Resolution = Resolution(2048, 1536)


    val _2048_1536: Resolution = QXGA


    val WUXGA: Resolution = Resolution(1920, 1200)


    val _1920_1200: Resolution = WUXGA


    val HD_1080: Resolution = Resolution(1920, 1080)


    val _1920_1080: Resolution = HD_1080


    val WSXGA_PLUS: Resolution = Resolution(1680, 1050)


    val _1680_1050: Resolution = WSXGA_PLUS


    val UXGA: Resolution = Resolution(1600, 1200)


    val _1600_1200: Resolution = UXGA


    val WSXGA: Resolution = Resolution(1440, 900)


    val _1140_900: Resolution = WSXGA


    val WXGA: Resolution = Resolution(1366, 768)


    val _1366_768: Resolution = WXGA


    val SXGA: Resolution = Resolution(1280, 1024)


    val _1280_1024: Resolution = SXGA


    val SXGA_960: Resolution = Resolution(1280, 960)


    val _1280_960: Resolution = SXGA_960


    val WXGA_800: Resolution = Resolution(1280, 800)


    val _1280_800: Resolution = WXGA_800


    val WXGA_768: Resolution = Resolution(1280, 768)


    val _1280_768: Resolution = WXGA_768


    val HD_720: Resolution = Resolution(1280, 720)


    val _1280_720: Resolution = HD_720


    val XGA_PLUS: Resolution = Resolution(1152, 864)


    val _1152_864: Resolution = XGA_PLUS


    val XGA: Resolution = Resolution(1024, 768)


    val _1024_768: Resolution = XGA


    val SVGA: Resolution = Resolution(800, 600)


    val _800_600: Resolution = SVGA


    val WVGA: Resolution = Resolution(800, 480)


    val _800_480: Resolution = XGA


    val DVD: Resolution = Resolution(720, 576)


    val _720_576: Resolution = DVD


    val VGA: Resolution = Resolution(640, 480)


    val _640_480: Resolution = VGA


    val HVGA: Resolution = Resolution(480, 320)


    val _480_320: Resolution = HVGA


    val WQVGA: Resolution = Resolution(400, 240)


    val _400_240: Resolution = WQVGA


    val QVGA: Resolution = Resolution(320, 240)


    val _320_240: Resolution = QVGA


    val HQVGA: Resolution = Resolution(240, 160)


    val _240_160: Resolution = HQVGA


    val LoRES: Resolution = Resolution(160, 160)


    val _160_160: Resolution = LoRES


    val QQVGA: Resolution = Resolution(160, 120)


    val _160_120: Resolution = QQVGA
  }
}
