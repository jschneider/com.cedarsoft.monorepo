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

import com.cedarsoft.common.resources.getResourceSafe
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.awt.Dimension
import java.awt.image.BufferedImage
import java.io.IOException
import javax.imageio.ImageIO

/**
 *
 */
class ImageConverterTest {

  private val imageConverter = ImageConverter()
  private lateinit var original: BufferedImage

  @BeforeEach
  fun setUp() {
    original = ImageIO.read(javaClass.getResourceSafe("/paris.jpg"))
  }

  @Test
  @Throws(InterruptedException::class)
  fun testResize() {
    val resized = ImageConverter().resize(original, Dimension(72, 88))
    assertEquals(72, resized.width.toLong())
    assertEquals(88, resized.height.toLong())

    //    JFrame frame = new JFrame();
    //    frame.setContentPane( new JPanel() {
    //      @Override
    //      protected void paintComponent( Graphics g ) {
    //        super.paintComponent( g );
    //        g.drawImage( resized, 0, 0, this );
    //      }
    //    } );
    //
    //    frame.setSize( 800, 600 );
    //    frame.setVisible( true );
    //    Thread.sleep( 4000 );
  }

  @Test
  @Throws(IOException::class)
  fun testSizes() {
    assertEquals(300, original.width.toLong())
    assertEquals(367, original.height.toLong())
    val newWidth = original.width * 72 / 300
    assertEquals(72, newWidth.toLong())
    val newHeight = original.height * 72 / 300
    assertEquals(88, newHeight.toLong())
    val dim = ImageConverter().calculateNewDimensions(original, DPI.DPI_300, DPI.DPI_072)
    assertEquals(72, dim.width.toLong())
    assertEquals(88, dim.height.toLong())
  }
}
