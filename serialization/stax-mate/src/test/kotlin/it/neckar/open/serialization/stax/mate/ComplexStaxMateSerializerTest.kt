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

import it.neckar.open.serialization.StreamSerializer
import it.neckar.open.serialization.test.utils.AbstractXmlSerializerTest2
import it.neckar.open.serialization.test.utils.Entry
import it.neckar.open.version.Version
import it.neckar.open.version.VersionRange
import org.codehaus.staxmate.out.SMOutputElement
import org.junit.jupiter.api.Assertions
import javax.annotation.Nonnull
import javax.xml.stream.XMLStreamException
import javax.xml.stream.XMLStreamReader

/**
 *
 */
class ComplexStaxMateSerializerTest : AbstractXmlSerializerTest2<String>() {
  @Nonnull
  override fun getSerializer(): StreamSerializer<String> {
    val stringSerializer: AbstractStaxMateSerializer<String> = object : AbstractStaxMateSerializer<String>("asdf", "asdf", VersionRange(Version(1, 0, 0), Version(1, 0, 0))) {
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
    return object : AbstractStaxMateSerializer<String>("aString", "asdf", VersionRange(Version(1, 0, 0), Version(1, 0, 0))) {
      @Throws(Exception::class)
      override fun serialize(@Nonnull serializeTo: SMOutputElement, @Nonnull objectToSerialize: String, @Nonnull formatVersion: Version) {
        stringSerializer.serialize(serializeTo.addElement(serializeTo.namespace, "sub"), objectToSerialize, formatVersion)
        serializeTo.addElement(serializeTo.namespace, "emptyChild").addCharacters("")
      }

      @Nonnull
      @Throws(Exception::class)
      override fun deserialize(@Nonnull deserializeFrom: XMLStreamReader, @Nonnull formatVersion: Version): String {
        assert(isVersionReadable(formatVersion))
        nextTag(deserializeFrom, "sub")
        val string = stringSerializer.deserialize(deserializeFrom, formatVersion)
        Assertions.assertEquals("", getChildText(deserializeFrom, "emptyChild"))
        closeTag(deserializeFrom)
        return string
      }
    }
  }

  companion object {
    val ENTRY1: Entry<out String> = create("asdf", "<aString><sub>asdf</sub><emptyChild/></aString>")
  }
}
