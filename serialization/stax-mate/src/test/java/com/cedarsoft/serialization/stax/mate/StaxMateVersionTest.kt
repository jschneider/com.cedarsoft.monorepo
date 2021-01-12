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
package com.cedarsoft.serialization.stax.mate

import com.cedarsoft.test.utils.*
import com.cedarsoft.version.UnsupportedVersionException
import com.cedarsoft.version.Version
import com.cedarsoft.version.VersionMismatchException
import com.cedarsoft.version.VersionRange
import com.cedarsoft.version.VersionRange.Companion.single
import org.codehaus.staxmate.out.SMOutputElement
import org.junit.Assert
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.xml.sax.SAXException
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.annotation.Nonnull
import javax.xml.stream.XMLStreamException
import javax.xml.stream.XMLStreamReader

/**
 *
 */
class StaxMateVersionTest {
  @Test
  @Throws(IOException::class, SAXException::class)
  fun testOld() {
    val serializer = OldIntegerSerializer()
    val out = ByteArrayOutputStream()
    serializer.serialize(7, out)
    AssertUtils.assertXMLEquals(out.toString(), "<integer xmlns=\"http://integer/1.0.0\" value=\"7\" />")
    Assert.assertEquals(serializer.deserialize(ByteArrayInputStream(out.toByteArray())), Integer.valueOf(7))
  }

  @Test
  @Throws(IOException::class, SAXException::class)
  fun testNew() {
    val serializer = NewIntegerSerializer()
    val out = ByteArrayOutputStream()
    serializer.serialize(7, out)
    AssertUtils.assertXMLEquals(out.toString(), "<integer xmlns=\"http://integer/2.0.0\">7</integer>")
    Assert.assertEquals(serializer.deserialize(ByteArrayInputStream(out.toByteArray())), Integer.valueOf(7))
  }

  @Test
  @Throws(IOException::class)
  fun testVersionsFail() {
    val serializer = OldIntegerSerializer()
    val out = ByteArrayOutputStream()
    serializer.serialize(7, out)
    try {
      NewIntegerSerializer().deserialize(ByteArrayInputStream(out.toByteArray()))
      Assert.fail("Where is the Exception")
    } catch (e: VersionMismatchException) {
      assertEquals(e.actual, Version(1, 0, 0))
      assertEquals(e.expected, single(2, 0, 0))
    }
    Assert.assertEquals(NewOldSerializer().deserialize(ByteArrayInputStream(out.toByteArray())), Integer.valueOf(7))
  }

  class NewOldSerializer : AbstractStaxMateSerializer<Int>("integer", "http://integer", VersionRange(Version(1, 0, 0), Version(2, 0, 0))) {
    @Throws(IOException::class, XMLStreamException::class)
    override fun serialize(@Nonnull serializeTo: SMOutputElement, @Nonnull objectToSerialize: Int, @Nonnull formatVersion: Version) {
      serializeTo.addCharacters(objectToSerialize.toString())
    }

    @Nonnull
    @Throws(IOException::class, XMLStreamException::class)
    override fun deserialize(@Nonnull deserializeFrom: XMLStreamReader, @Nonnull formatVersion: Version): Int {
      assert(isVersionReadable(formatVersion))
      return if (formatVersion == Version(1, 0, 0)) {
        val intValue = deserializeFrom.getAttributeValue(null, "value").toInt()
        closeTag(deserializeFrom)
        intValue
      } else if (formatVersion == Version(2, 0, 0)) {
        getText(deserializeFrom).toInt()
      } else {
        throw UnsupportedVersionException(formatVersion, formatVersionRange)
      }
    }
  }

  class NewIntegerSerializer : AbstractStaxMateSerializer<Int>("integer", "http://integer", VersionRange(Version(2, 0, 0), Version(2, 0, 0))) {
    @Throws(IOException::class, XMLStreamException::class)
    override fun serialize(@Nonnull serializeTo: SMOutputElement, @Nonnull objectToSerialize: Int, @Nonnull formatVersion: Version) {
      serializeTo.addCharacters(objectToSerialize.toString())
    }

    @Nonnull
    @Throws(IOException::class, XMLStreamException::class)
    override fun deserialize(@Nonnull deserializeFrom: XMLStreamReader, @Nonnull formatVersion: Version): Int {
      assert(isVersionReadable(formatVersion))
      return getText(deserializeFrom).toInt()
    }
  }

  class OldIntegerSerializer : AbstractStaxMateSerializer<Int>("integer", "http://integer", VersionRange(Version(1, 0, 0), Version(1, 0, 0))) {
    @Throws(IOException::class, XMLStreamException::class)
    override fun serialize(@Nonnull serializeTo: SMOutputElement, @Nonnull objectToSerialize: Int, @Nonnull formatVersion: Version) {
      assert(isVersionWritable(formatVersion))
      serializeTo.addAttribute("value", objectToSerialize.toString())
    }

    @Nonnull
    @Throws(IOException::class, XMLStreamException::class)
    override fun deserialize(@Nonnull deserializeFrom: XMLStreamReader, @Nonnull formatVersion: Version): Int {
      assert(isVersionReadable(formatVersion))
      val intValue = deserializeFrom.getAttributeValue(null, "value").toInt()
      closeTag(deserializeFrom)
      return intValue
    }
  }
}
