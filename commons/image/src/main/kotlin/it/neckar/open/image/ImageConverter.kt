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
import java.awt.Image
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.awt.image.RenderedImage

/**
 *
 * ImageConverter class.
 *
 */
object ImageConverter {
  /**
   * Calculates the new dimension of the image
   *
   * @param original           the original image
   * @param originalDPI the original resolution
   * @param targetDPI   the target resolution
   * @return the dimension
   */
  fun calculateNewDimensions(original: RenderedImage, originalDPI: DPI, targetDPI: DPI): Dimension {
    val newWidth = original.width * targetDPI.dpi / originalDPI.dpi
    val newHeight = original.height * targetDPI.dpi / originalDPI.dpi
    return Dimension(newWidth, newHeight)
  }

  /**
   * Resizes the image to the given size
   *
   * @param original        a BufferedImage object.
   * @param targetDimension a Dimension object.
   * @return a BufferedImage object.
   */
  fun resize(original: Image, targetDimension: Dimension): BufferedImage {
    val resized = BufferedImage(targetDimension.width, targetDimension.height, BufferedImage.TYPE_INT_RGB)
    val graphics2D = resized.createGraphics()
    graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
    graphics2D.drawImage(original, 0, 0, targetDimension.width, targetDimension.height, null)
    return resized
  }

  /**
   *
   * resize
   *
   * @param original           a BufferedImage object.
   * @param originalDPI a DPI object.
   * @param targetDPI   a DPI object.
   * @return a BufferedImage object.
   */
  fun resize(original: BufferedImage, originalDPI: DPI, targetDPI: DPI): BufferedImage {
    return resize(original, calculateNewDimensions(original, originalDPI, targetDPI))
  }
}
