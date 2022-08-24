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
package com.cedarsoft.file

/**
 *
 * FileTypeRegistry class.
 *
 */
object FileTypeRegistry {
  /**
   * Constant `LIGHT_ZONE`
   */
  @JvmStatic
  val LIGHT_ZONE: FileType = FileType("LightZone", "application/lightzone", true, Extension("_", "lzn.jpg"))

  /**
   * Constant `JPEG`
   */
  @JvmStatic
  val JPEG: FileType = FileType("JPEG", "image/jpeg", false, Extension(".", "jpg"), Extension(".", "jpeg"))

  /**
   * Constant `TIFF`
   */
  @JvmStatic
  val TIFF: FileType = FileType("TIFF", "image/tiff", false, Extension(".", "tif"), Extension(".", "tiff"))

  /**
   * Constant `GIMP`
   */
  @JvmStatic
  val GIMP: FileType = FileType("Gimp", "image/xcf", false, Extension(".", "xcf"))

  /**
   * Constant `PHOTO_SHOP`
   */
  @JvmStatic
  val PHOTO_SHOP: FileType = FileType("Photoshop", "image/psd", false, Extension(".", "psd"))

  /**
   * Constant `RAW_CANON`
   */
  @JvmStatic
  val RAW_CANON: FileType = FileType("CanonRaw", "image/cr2", false, Extension(".", "cr2"))

  @JvmStatic
  private val DEFAULT = listOf(LIGHT_ZONE, JPEG, TIFF, GIMP, RAW_CANON, PHOTO_SHOP)
}
