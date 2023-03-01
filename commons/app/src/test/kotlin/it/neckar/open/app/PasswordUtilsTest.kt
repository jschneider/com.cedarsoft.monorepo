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
package it.neckar.open.app

import it.neckar.open.app.PasswordUtils.calculateMD5Hash
import it.neckar.open.app.PasswordUtils.hasExpectedHash
import it.neckar.open.app.PasswordUtils.validatePasswordHash
import org.apache.commons.codec.DecoderException
import org.apache.commons.codec.binary.Hex
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.nio.charset.StandardCharsets

/**
 *
 */
class PasswordUtilsTest {
  @Test
  @Throws(DecoderException::class)
  fun testIt() {
    Assertions.assertEquals("1a1dc91c907325c69271ddf0c944bc72", String(Hex.encodeHex(calculateMD5Hash("pass"))))
    Assertions.assertEquals("ea847988ba59727dbf4e34ee75726dc3", String(Hex.encodeHex(calculateMD5Hash("topsecret"))))
    Assertions.assertTrue(hasExpectedHash("pass", Hex.decodeHex("1a1dc91c907325c69271ddf0c944bc72".toCharArray())))
    Assertions.assertTrue(hasExpectedHash("topsecret", Hex.decodeHex("ea847988ba59727dbf4e34ee75726dc3".toCharArray())))
    Assertions.assertFalse(hasExpectedHash("topsecret", null))
    Assertions.assertFalse(hasExpectedHash("topsecret", Hex.decodeHex("1234".toCharArray())))
  }

  @Test
  @Throws(InvalidPasswordException::class)
  fun testEx() {
    validatePasswordHash("a".toByteArray(StandardCharsets.UTF_8), "a".toByteArray(StandardCharsets.UTF_8))
    try {
      validatePasswordHash("a".toByteArray(StandardCharsets.UTF_8), "b".toByteArray(StandardCharsets.UTF_8))
      Assertions.fail("Where is the Exception")
    } catch (ignore: InvalidPasswordException) {
    }
  }

  @Test
  @Throws(InvalidPasswordException::class)
  fun testEx2() {
    try {
      validatePasswordHash(null, null)
      Assertions.fail("Where is the Exception")
    } catch (ignore: InvalidPasswordException) {
    }
  }

  @Test
  @Throws(InvalidPasswordException::class)
  fun testEx3() {
    try {
      validatePasswordHash("a".toByteArray(StandardCharsets.UTF_8), "ab".toByteArray(StandardCharsets.UTF_8))
      Assertions.fail("Where is the Exception")
    } catch (ignore: InvalidPasswordException) {
    }
  }

  @Test
  @Throws(InvalidPasswordException::class)
  fun testEx4() {
    try {
      validatePasswordHash("a".toByteArray(StandardCharsets.UTF_8), null)
      Assertions.fail("Where is the Exception")
    } catch (ignore: InvalidPasswordException) {
    }
  }
}
