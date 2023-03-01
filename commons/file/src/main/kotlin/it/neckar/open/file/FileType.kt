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

package it.neckar.open.file


import com.google.common.collect.ImmutableList
import java.io.File
import java.util.Locale

/**
 *
 * FileType class.
 *
 */
data class FileType
private constructor(
  /**
   *
   * Getter for the field `id`.
   *
   * @return a String object.
   */
  val id: String,
  val contentType: String,
  /**
   * Returns whether this file is a dependent type.
   * A dependent file is a file that needs another file it depends on.
   *
   * @return whether this file is a dependent type
   */
  val isDependentType: Boolean,
  val extensions: ImmutableList<out Extension>
) {

  constructor(id: String, contentType: String, dependentType: Boolean, vararg extensions: Extension) : this(id, contentType, dependentType, ImmutableList.copyOf(extensions))
  constructor(id: String, contentType: String, dependentType: Boolean, extensions: List<@JvmWildcard Extension>) : this(id, contentType, dependentType, ImmutableList.copyOf(extensions))

  val defaultExtension: Extension
    get() = extensions[0]

  init {
    if (extensions.isEmpty()) {
      throw IllegalArgumentException("Need at least one extension")
    }
  }

  /**
   *
   * matches
   *
   * @param fileName a String object.
   * @return a boolean.
   */
  fun matches(fileName: String): Boolean {
    for (ex in extensions) {
      if (fileName.toLowerCase(Locale.US).endsWith(ex.combined)) {
        return true
      }
    }
    return false
  }

  /**
   *
   * matches
   *
   * @param fileName a FileName object.
   * @return a boolean.
   */
  fun matches(fileName: FileName): Boolean {
    return matches(fileName.name)
  }

  /**
   *
   * getFileName
   *
   * @param file a File object.
   * @return a FileName object.
   */
  fun getFileName(file: File): FileName {
    return getFileName(file.name)
  }

  fun getFileName(fileName: String): FileName { //$NON-NLS-1$
    var bestBase: String? = null
    var bestExtension: Extension? = null

    for (extension in extensions) {
      val index = fileName.toLowerCase(Locale.ENGLISH).indexOf(extension.combined)
      if (index < 0) {
        continue
      }

      val base = fileName.substring(0, index)
      if (bestBase == null || base.length < bestBase.length) {
        bestBase = base
        bestExtension = extension.createCaseSensitiveExtension(fileName)
      }
    }

    if (bestBase == null) {
      throw IllegalArgumentException("Cannot get base for $fileName")
    }
    requireNotNull(bestExtension) { "bestExtension must not be null" }

    return FileName(bestBase, bestExtension)
  }

  fun getExtension(fileName: String): Extension {
    return getFileName(fileName).extension
  }

  fun getBaseName(fileName: String): String {
    return getFileName(fileName).baseName.name
  }

  fun isDefaultExtension(extension: Extension): Boolean {
    return defaultExtension == extension
  }
}
