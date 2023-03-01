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

import java.io.Serializable

/**
 * Represents an aspect ratio
 */
data class AspectRatio(
  val widthFactor: Double,
  val heightFactor: Double,
) : Serializable {

  /**
   * The ratio of width factor to height factor
   */
  val ratio: Double
    get() = widthFactor / heightFactor

  fun invert(): AspectRatio {
    return AspectRatio(heightFactor, widthFactor)
  }

  companion object {
    @JvmField
    val AR_1_1: AspectRatio = AspectRatio(1.0, 1.0)

    @JvmField
    val AR_3_2: AspectRatio = AspectRatio(3.0, 2.0)

    @JvmField
    val AR_4_3: AspectRatio = AspectRatio(4.0, 3.0)

    @JvmField
    val AR_5_3: AspectRatio = AspectRatio(5.0, 3.0)

    @JvmField
    val AR_5_4: AspectRatio = AspectRatio(5.0, 4.0)

    @JvmField
    val AR_16_9: AspectRatio = AspectRatio(16.0, 9.0)

    @JvmField
    val AR_16_10: AspectRatio = AspectRatio(16.0, 10.0)

    /**
     * Used for paper (A4)...
     */
    @JvmField
    val AR_SQ2_1: AspectRatio = AspectRatio(Math.sqrt(2.0), 1.0)

    @JvmField
    val PRE_DEFINED: List<AspectRatio> = listOf(AR_1_1, AR_3_2, AR_4_3, AR_5_3, AR_5_4, AR_16_9, AR_16_10)

    /**
     * The bounds used when finding the aspect ratio for a width/height
     */
    const val BOUNDS: Double = 0.005

    private const val serialVersionUID = -1083087626635613019L

    /**
     * Returns the predefined aspect ratio
     *
     * @param width  the width
     * @param height the height
     * @return the aspect ratio
     */
    @JvmStatic
    operator fun get(width: Double, height: Double): AspectRatio {
      val ratio = width / height
      for (aspectRatio in PRE_DEFINED) {
        val delta = aspectRatio.ratio - ratio
        if (Math.abs(delta) < BOUNDS) {
          return aspectRatio
        }
      }
      return AspectRatio(width, height)
    }
  }
}
