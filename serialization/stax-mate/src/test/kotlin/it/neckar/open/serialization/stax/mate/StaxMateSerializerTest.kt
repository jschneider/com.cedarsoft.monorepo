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
package it.neckar.open.serialization.stax.mate

import it.neckar.open.serialization.SerializationException
import it.neckar.open.serialization.StreamSerializer
import it.neckar.open.serialization.test.utils.AbstractXmlSerializerTest2
import it.neckar.open.serialization.test.utils.Entry
import it.neckar.open.version.Version
import it.neckar.open.version.VersionException
import it.neckar.open.version.VersionMismatchException
import it.neckar.open.version.VersionRange
import it.neckar.open.xml.XmlCommons
import org.codehaus.staxmate.out.SMOutputElement
import org.junit.Assert
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets
import javax.annotation.Nonnull
import javax.xml.stream.XMLStreamException
import javax.xml.stream.XMLStreamReader

/**
 *
 */
class StaxMateSerializerTest : AbstractXmlSerializerTest2<String>() {
  @Nonnull
  override fun getSerializer(): StreamSerializer<String> {
    return object : AbstractStaxMateSerializer<String>("aString", "http://www.lang.java/String", VersionRange(Version(1, 5, 3), Version(1, 5, 3))) {
      @Throws(XMLStreamException::class)
      override fun serialize(@Nonnull serializeTo: SMOutputElement, @Nonnull objectToSerialize: String, @Nonnull formatVersion: Version) {
        assert(isVersionWritable(formatVersion))
        serializeTo.addCharacters(objectToSerialize)
      }

      @Nonnull
      @Throws(XMLStreamException::class)
      override fun deserialize(@Nonnull deserializeFrom: XMLStreamReader, @Nonnull formatVersion: Version): String {
        assert(isVersionReadable(formatVersion))
        deserializeFrom.next()
        val text = deserializeFrom.text
        closeTag(deserializeFrom)
        return text
      }
    }
  }

  @Throws(Exception::class)
  protected override fun verifySerialized(@Nonnull entry: Entry<String>, @Nonnull serialized: ByteArray) {
    super.verifySerialized(entry, serialized)
    Assert.assertTrue(XmlCommons.format(String(serialized, StandardCharsets.UTF_8)), String(serialized, StandardCharsets.UTF_8).contains("xmlns=\"http://www.lang.java/String/1.5.3\""))
  }

  override fun verifyDeserialized(@Nonnull deserialized: String, @Nonnull original: String) {
    super.verifyDeserialized(deserialized, original)
    Assert.assertEquals("asdf", deserialized)
  }

  @Test
  fun testNoVersion() {
    Assertions.assertThrows(VersionException::class.java) { getSerializer().deserialize(ByteArrayInputStream("<aString>asdf</aString>".toByteArray(StandardCharsets.UTF_8))) }
  }

  @Test
  fun testWrongVersion() {
    Assertions.assertThrows(VersionMismatchException::class.java) { getSerializer().deserialize(ByteArrayInputStream("<aString xmlns=\"http://www.lang.java/String/0.9.9\">asdf</aString>".toByteArray(StandardCharsets.UTF_8))) }
  }

  @Test
  fun testWrongNamespaceVersion() {
    Assertions.assertThrows(SerializationException::class.java) { getSerializer().deserialize(ByteArrayInputStream("<aString xmlns=\"http://www.lang.invalid.java/String/1.5.3\">asdf</aString>".toByteArray(StandardCharsets.UTF_8))) }
  }

  companion object {
    @JvmField
    val ENTRY1: Entry<out String> = create("asdf", "<aString xmlns=\"http://www.lang.java/String/1.5.3\">asdf</aString>")
  }
}
