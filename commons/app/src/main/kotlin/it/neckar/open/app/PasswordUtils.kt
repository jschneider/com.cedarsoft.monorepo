/**
 * Copyright (C) cedarsoft GmbH.

 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at

 * http://www.cedarsoft.org/gpl3ce
 * (GPL 3 with Classpath Exception)

 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.

 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).

 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.

 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */

package it.neckar.open.app


import java.nio.charset.StandardCharsets
import java.security.MessageDigest

/**
 *
 * PasswordUtils class.

 */
object PasswordUtils {
  @JvmStatic
  fun calculateMD5Hash(password: String): ByteArray {
    val bytes = password.toByteArray(StandardCharsets.UTF_8)
    val messageDigest = MessageDigest.getInstance("MD5")
    return messageDigest.digest(bytes)
  }

  @JvmStatic
  fun hasExpectedHash(password: String, expectedHash: ByteArray?): Boolean {
    if (expectedHash == null) {
      return false
    }

    val actual = calculateMD5Hash(password)
    return try {
      validatePasswordHash(expectedHash, actual)
      true
    } catch (ignore: InvalidPasswordException) {
      false
    }
  }

  /**
   * Validates the password
   */
  @JvmStatic
  @Throws(InvalidPasswordException::class)
  fun validatePasswordHash(expected: ByteArray?, actual: ByteArray?) {
    if (expected == null || actual == null) {
      throw InvalidPasswordException()
    }
    if (actual.size != expected.size) {
      throw InvalidPasswordException()
    }

    for (i in actual.indices) {
      val actualByte = actual[i]
      val expectedByte = expected[i]

      if (actualByte != expectedByte) {
        throw InvalidPasswordException()
      }
    }
  }
}
