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
package com.cedarsoft.id

import java.util.Locale

/**
 *
 */
object NameSpaceSupport {
  @JvmStatic
  fun createNameSpaceUriBase(type: Class<*>): String {
    return createNameSpaceUriBase(type.name)
  }

  @JvmStatic
  fun createNameSpaceUriBase(className: String): String {
    val parts = className.split("\\.".toRegex()).toTypedArray()

    //If we have lesser than three parts just return the type - a fallback
    if (parts.size < 3) {
      return "https://$className"
    }
    val uri = StringBuilder("https://")
    uri.append(parts[1])
    uri.append(".")
    uri.append(parts[0])
    var i = 2
    val partsLength = parts.size
    while (i < partsLength) {
      val part = parts[i]
      uri.append("/")
      uri.append(createNameWithSpaces(part))
      i++
    }
    return uri.toString()
  }

  @JvmStatic
  fun createNameWithSpaces(camelName: String): String {
    //If it is the same, just return it
    val lowerCaseName = camelName.lowercase(Locale.getDefault())
    if (lowerCaseName == camelName) {
      return camelName
    }
    val builder = StringBuilder()
    for (i in camelName.indices) {
      val camelPart = camelName.substring(i, i + 1)
      val asLower = camelPart.lowercase(Locale.getDefault())
      if (builder.isNotEmpty() && asLower != camelPart) {
        builder.append("-")
      }
      builder.append(asLower)
    }
    return builder.toString()
  }
}
