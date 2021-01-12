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
package com.cedarsoft.serialization

import assertk.*
import assertk.assertions.*
import com.cedarsoft.version.Version
import com.cedarsoft.version.Version.Companion.valueOf
import com.cedarsoft.version.VersionException
import com.cedarsoft.version.VersionMismatchException
import com.cedarsoft.version.VersionRange.Companion.from
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.StandardCharsets
import javax.annotation.Nonnull

/**
 *
 */
class DaAbstractSerializerTest {
  private var serializer: MySerializer? = null

  @BeforeEach
  @Throws(Exception::class)
  fun setUp() {
    serializer = MySerializer()
  }

  @Test
  @Throws(Exception::class)
  fun testSeria() {
    Assert.assertThat(String(serializer!!.serializeToByteArray("daObject"), StandardCharsets.UTF_8), CoreMatchers.`is`("daObject"))
  }

  @Test
  @Throws(IOException::class)
  fun testDeserialize() {
    Assert.assertThat(serializer!!.deserialize("asdf", valueOf(7, 0, 0)), CoreMatchers.`is`("asdf"))
  }

  @Test
  fun testWritable() {
    Assert.assertTrue(serializer!!.isVersionWritable(valueOf(2, 0, 0)))
    Assert.assertFalse(serializer!!.isVersionWritable(valueOf(1, 0, 0)))
    Assert.assertFalse(serializer!!.isVersionWritable(valueOf(1, 5, 0)))
    Assert.assertFalse(serializer!!.isVersionWritable(valueOf(2, 0, 1)))
    Assert.assertFalse(serializer!!.isVersionWritable(valueOf(0, 0, 1)))
    serializer!!.verifyVersionWritable(valueOf(2, 0, 0))
    try {
      serializer!!.verifyVersionWritable(valueOf(2, 0, 1))
      Assert.fail("Where is the Exception")
    } catch (e: VersionMismatchException) {
      assertThat(e).hasMessage("Version mismatch. Expected [2.0.0] but was [2.0.1]")
    }
  }

  @Test
  fun testReadable() {
    Assert.assertTrue(serializer!!.isVersionReadable(valueOf(1, 0, 0)))
    Assert.assertTrue(serializer!!.isVersionReadable(valueOf(1, 5, 0)))
    Assert.assertTrue(serializer!!.isVersionReadable(valueOf(2, 0, 0)))
    Assert.assertFalse(serializer!!.isVersionReadable(valueOf(2, 0, 1)))
    Assert.assertFalse(serializer!!.isVersionReadable(valueOf(0, 0, 1)))
    serializer!!.verifyVersionReadable(valueOf(1, 0, 0))
    serializer!!.verifyVersionReadable(valueOf(2, 0, 0))
    try {
      serializer!!.verifyVersionReadable(valueOf(2, 0, 1))
      Assert.fail("Where is the Exception")
    } catch (e: VersionMismatchException) {
      assertThat(e).hasMessage("Version mismatch. Expected [1.0.0-2.0.0] but was [2.0.1]")
    }
  }

  @Test
  @Throws(Exception::class)
  fun testIt() {
    serializer!!.verifyVersionReadable(valueOf(1, 0, 0))
    serializer!!.verifyVersionReadable(valueOf(1, 5, 0))
    serializer!!.verifyVersionReadable(valueOf(2, 0, 0))
    try {
      serializer!!.verifyVersionReadable(valueOf(2, 0, 1))
      Assert.fail("Where is the Exception")
    } catch (e: VersionMismatchException) {
      assertThat(e).hasMessage("Version mismatch. Expected [1.0.0-2.0.0] but was [2.0.1]")
    }
  }

  @Test
  @Throws(Exception::class)
  fun testIt2() {
    serializer!!.verifyVersionReadable(valueOf(1, 0, 0))
    serializer!!.verifyVersionReadable(valueOf(1, 5, 0))
    serializer!!.verifyVersionReadable(valueOf(2, 0, 0))
    try {
      serializer!!.verifyVersionReadable(valueOf(0, 99, 99))
      Assert.fail("Where is the Exception")
    } catch (e: VersionMismatchException) {
      assertThat(e).hasMessage("Version mismatch. Expected [1.0.0-2.0.0] but was [0.99.99]")
    }
  }

  @Test
  @Throws(Exception::class)
  fun testVerify() {
    Assert.assertThat(serializer!!.formatVersion, CoreMatchers.`is`(valueOf(2, 0, 0)))
    Assert.assertThat(serializer!!.formatVersionRange, CoreMatchers.`is`(from(1, 0, 0).to(2, 0, 0)))
    try {
      serializer!!.delegatesMappings.verify()
      Assert.fail("Where is the Exception")
    } catch (e: VersionException) {
      assertThat(e).hasMessage("No mappings available")
    }
  }

  class MySerializer : AbstractStreamSerializer<String, StringBuffer, String>(from(1, 0, 0).to(2, 0, 0)) {
    @Throws(IOException::class, VersionException::class, IOException::class)
    override fun serialize(@Nonnull serializeTo: StringBuffer, @Nonnull objectToSerialize: String, @Nonnull formatVersion: Version) {
      serializeTo.append(objectToSerialize)
    }

    @Nonnull
    @Throws(IOException::class, VersionException::class, IOException::class)
    override fun deserialize(@Nonnull deserializeFrom: String, @Nonnull formatVersion: Version): String {
      return deserializeFrom
    }

    @Throws(IOException::class)
    override fun serialize(@Nonnull objectToSerialize: String, @Nonnull out: OutputStream) {
      out.write(objectToSerialize.toByteArray(StandardCharsets.UTF_8))
    }

    @Nonnull
    @Throws(IOException::class, VersionException::class)
    override fun deserialize(@Nonnull deserializeFrom: InputStream): String {
      throw UnsupportedOperationException()
    }
  }
}
