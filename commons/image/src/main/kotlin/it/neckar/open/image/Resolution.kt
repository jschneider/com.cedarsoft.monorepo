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

import java.awt.Dimension
import java.io.Serializable

/**
 * Represents a resolution.
 * Resolutions can be compared using the width as first criteria and the height as second.
 *
 *
 * This means that 100/1 is larger than 99/Integer.MAX
 */
data class Resolution(
  val width: Int,
  val height: Int,
) : Comparable<Resolution>, Serializable {

  val aspectRatio: AspectRatio = AspectRatio[width.toDouble(), height.toDouble()]

  /**
   * Creates a new resolution based on the dimension
   */
  constructor(dimension: Dimension) : this(dimension.width, dimension.height) {}

  override fun compareTo(other: Resolution): Int {
    return if (width != other.width) {
      Integer.valueOf(width).compareTo(other.width)
    } else Integer.valueOf(height).compareTo(other.height)
  }

  override fun toString(): String {
    return "($width/$height)"
  }

  /**
   * Converts the resolution to a dimensino
   *
   * @return a dimension with the same width/height
   */
  fun toDimension(): Dimension {
    return Dimension(width, height)
  }

  companion object {
    private const val serialVersionUID = -8344104794069328642L
  }
}
