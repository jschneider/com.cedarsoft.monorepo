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


import java.io.File

/**
 * Represents a file name
 *
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
data class FileName(
  /**
   *
   * Getter for the field `baseName`.
   *
   * @return a BaseName object.
   */
  val baseName: BaseName,
  /**
   *
   * Getter for the field `extension`.
   *
   * @return a Extension object.
   */
  val extension: Extension
) {

  /**
   *
   * getName
   *
   * @return a String object.
   */
  val name: String
    get() = baseName.toString() + extension.combined

  /**
   * Creates a new file name
   *
   * @param baseName  the base name
   * @param extension the file extension
   */
  constructor(baseName: String, extension: String) : this(BaseName(baseName), Extension(extension))

  /**
   * Creates a new file name
   *
   * @param baseName  the base name
   * @param extension the extension
   */
  constructor(baseName: String, extension: Extension) : this(BaseName(baseName), extension)

  /**
   *
   * Constructor for FileName.
   *
   * @param baseName  a String object.
   * @param delimiter a String object.
   * @param extension a String object.
   */
  constructor(baseName: String, delimiter: String, extension: String) : this(BaseName(baseName), Extension(delimiter, extension))

  /**
   * Creates a file name
   *
   * @param baseName  the base name
   * @param delimiter the delimiter
   * @param extension the extension
   */
  constructor(baseName: BaseName, delimiter: String, extension: String) : this(baseName, Extension(delimiter, extension))

  /**
   * {@inheritDoc}
   */
  override fun toString(): String {
    return baseName.toString() + extension.toString()
  }

  /**
   * Returns the corresponding file
   *
   * @param baseDir the base dir
   * @return the file represented
   */
  fun getFile(baseDir: File): File {
    assert(baseDir.isDirectory)
    return File(baseDir, name)
  }
}
