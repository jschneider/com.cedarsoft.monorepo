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
package com.cedarsoft.exceptions

import javax.annotation.concurrent.Immutable

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
@Immutable
data class ErrorCode(
  /**
   * The prefix for the error code - must be unique over all components
   */
  /**
   * Returns the prefix of the error code
   *
   * @return the prefix of the error code
   */
  val prefix: Prefix,
  /**
   * The id for this error code - must be unique for one prefix
   */
  /**
   * Returns the id
   *
   * @return the id
   */
  val id: Int
) {

  /**
   * Returns the error code formatted as string
   *
   * @return the error code as string
   */
  fun format(): String {
    return prefix.asString() + SEPARATOR + id
  }

  override fun toString(): String {
    return format()
  }

  /**
   * Represents a prefix for error codes.
   * The prefix is used to structure the error codes.
   * The ids are unique within a prefix.
   */
  @Immutable
  data class Prefix
  /**
   * Creates a new prefix
   *
   * @param prefix the prefix
   */
    (private val prefix: String) {

    /**
     * Returns the string representation of this prefix
     *
     * @return the string representation
     */
    fun asString(): String {
      return prefix
    }

    override fun toString(): String {
      return prefix
    }
  }

  companion object {
    private const val SEPARATOR = '-'
  }
}
