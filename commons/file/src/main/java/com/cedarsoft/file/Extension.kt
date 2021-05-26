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


import java.util.Locale

/**
 * An extension of a file.
 * The case does *not* matter(!)
 *
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
data class Extension(
  /**
   *
   * Getter for the field `delimiter`.
   *
   * @return a String object.
   */
  val delimiter: String,
  /**
   *
   * Getter for the field `extension`.
   *
   * @return a String object.
   */
  val extension: String
) //We only accept lower case extensions
{

  /**
   *
   * Constructor for Extension.
   *
   * @param extension a String object.
   */
  constructor(extension: String) : this(DEFAULT_DELIMITER, extension)

  /**
   *
   * getCombined
   *
   * @return a String object.
   */
  val combined: String
    get() = delimiter + extension

  fun createCaseSensitiveExtension(fileName: String): Extension {
    val extensionIndex = fileName.toLowerCase(Locale.US).lastIndexOf(extension.toLowerCase(Locale.US))
    return Extension(delimiter, fileName.substring(extensionIndex))
  }

  /**
   * {@inheritDoc}
   */
  override fun toString(): String {
    return combined
  }

  companion object {
    /**
     * Constant `DEFAULT_DELIMITER="."`
     */
    const val DEFAULT_DELIMITER = "."
    /**
     * Constant `NONE`
     */
    @JvmStatic
    val NONE = Extension("", "")
  }
}
