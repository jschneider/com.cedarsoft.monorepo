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
package it.neckar.open.crypt

import assertk.*
import assertk.assertions.*
import it.neckar.open.resources.getResourceSafe
import it.neckar.open.crypt.Hash.Companion.fromHex
import it.neckar.open.crypt.HashCalculator.calculate
import org.apache.commons.io.IOUtils
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.nio.charset.StandardCharsets

/**
 *
 *
 * Date: Jul 20, 2007<br></br>
 * Time: 11:28:04 PM<br></br>
 */
class HashTest {
  @Test
  fun testDigestTest() {
    assertThat(Algorithm.SHA256.messageDigest).isNotSameAs(Algorithm.SHA256.messageDigest)
  }

  @Test
  fun testToString() {
    assertThat(fromHex(Algorithm.MD5, "aabb").toString()).isEqualTo("[MD5: aabb]")
    assertThat(fromHex(Algorithm.SHA256, "aabb1111111111").toString()).isEqualTo("[SHA256: aabb1111111111]")
  }

  @Test
  @Throws(Exception::class)
  fun testString() {
    assertThat(calculate(Algorithm.SHA256, "asdf").valueAsHex).isEqualTo("f0e4c2f76c58916ec258f246851bea091d14d4247a2fc3e18694461b1816e13b")
    assertThat(calculate(Algorithm.SHA256.messageDigest, "asdf").valueAsHex).isEqualTo("f0e4c2f76c58916ec258f246851bea091d14d4247a2fc3e18694461b1816e13b")
  }

  @Test
  @Throws(IOException::class, ClassNotFoundException::class)
  fun testSerialization() {
    val hash = fromHex(Algorithm.SHA256, "1234")
    val out = ByteArrayOutputStream()
    ObjectOutputStream(out).writeObject(hash)
    val deserialized = ObjectInputStream(ByteArrayInputStream(out.toByteArray())).readObject() as Hash
    assertThat(deserialized).isEqualTo(hash)
  }

  @Test
  fun testIt() {
    val paris = javaClass.getResourceSafe("/paris.jpg")
    assertThat(paris).isNotNull()

    assertThat(calculate(Algorithm.MD5, paris).valueAsHex).isEqualTo("fbd5f9b6c0fd2035c490e46be0bc3ec3") //value read using md5sum cmd line tool
    assertThat(calculate(Algorithm.SHA1, paris).valueAsHex).isEqualTo("aa5371938c4190543bddcfc1193a247717feba06") //value read using sha1sum cmd line tool
    assertThat(calculate(Algorithm.SHA1, IOUtils.toByteArray(paris.openStream())).valueAsHex).isEqualTo("aa5371938c4190543bddcfc1193a247717feba06")
    assertThat(calculate(Algorithm.SHA1, paris.openStream()).valueAsHex).isEqualTo("aa5371938c4190543bddcfc1193a247717feba06")
  }

  @Test
  fun testRound() {
    val hash = Hash(Algorithm.SHA256, "asdf".toByteArray(StandardCharsets.UTF_8))
    assertThat(hash.valueAsHex).isEqualTo("61736466")
    assertThat(hash).isEqualTo(fromHex(hash.algorithm, hash.valueAsHex))
  }

  @Test
  fun testAlgos() {
    val paris = javaClass.getResourceSafe("/paris.jpg")
    assertThat(paris).isNotNull()

    for (algorithm in Algorithm.values()) {
      val value = calculate(algorithm, paris).valueAsHex
      assertThat(value).isNotNull()
      assertThat(value.length).isGreaterThanOrEqualTo(10)
    }
  }
}
