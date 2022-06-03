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
package com.cedarsoft.commons

import java.util.Locale
import java.util.ResourceBundle
import javax.annotation.Nonnull

/**
 * An extended type of Boolean that has a third option: [.UNKNOWN].
 *
 */
enum class ExtendedBoolean {
  Unknown,
  True,
  False;

  val isTrue: Boolean
    get() = this == True

  val isFalse: Boolean
    get() = this == False

  val isUnknown: Boolean
    get() = this == Unknown

  /**
   * Returns the description for the default locale
   *
   * @return the description for the default locale
   */
  val description: String
    get() = getDescription(Locale.getDefault())

  /**
   * Returns the description for the given locale
   *
   * @param locale the locale
   * @return the description for the locale
   */
  fun getDescription(@Nonnull locale: Locale): String {
    val bundle = ResourceBundle.getBundle(javaClass.name, locale)
    return bundle.getString(name)
  }

  companion object {
    /**
     * Returns the ExtendedBoolean for a boolean
     *
     * @param value the boolean value
     * @return [.TRUE] or [.FALSE] - depending on the given value
     */
    @JvmStatic
    fun valueOf(value: Boolean): ExtendedBoolean {
      return if (value) {
        True
      } else {
        False
      }
    }
  }
}
