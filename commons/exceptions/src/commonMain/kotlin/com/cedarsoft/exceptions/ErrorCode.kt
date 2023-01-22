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

import com.cedarsoft.annotations.JavaFriendly
import com.cedarsoft.exceptions.io.ErrorCodeSerializer
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlin.jvm.JvmStatic


/**
 * Identifies an error.
 * Usually an enumeration should be created that contains one set of error codes.
 */
@Serializable(with = ErrorCodeSerializer::class)
data class ErrorCode(
  /**
   * The prefix for the error code - must be unique over all components
   */
  /**
   * Returns the prefix of the error code
   */
  val prefix: Prefix,
  /**
   * The id for this error code - must be unique for one prefix
   */
  val id: Int
) {

  init {
    require(prefix.asString().contains(SEPARATOR).not()) {
      "Prefix <${prefix}> must not contain separator <$SEPARATOR>"
    }
  }

  /**
   * Returns the error code formatted as string
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
  @Serializable
  @JvmInline
  value class Prefix(private val prefix: String) {

    /**
     * Returns the string representation of this prefix
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

    @JavaFriendly
    @JvmStatic
    fun create(prefix: String, id: Int): ErrorCode {
      return ErrorCode(Prefix(prefix), id)
    }

    fun parse(errorCodeAsString: String): ErrorCode {
      val parts = errorCodeAsString.split(SEPARATOR)
      require(parts.size == 2) {
        "Invalid error code as string <$errorCodeAsString>"
      }

      return ErrorCode.create(parts[0], parts[1].toInt())
    }
  }
}
