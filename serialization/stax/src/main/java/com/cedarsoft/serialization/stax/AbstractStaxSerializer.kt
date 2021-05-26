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

package com.cedarsoft.serialization.stax

import com.cedarsoft.serialization.SerializationException
import com.cedarsoft.serialization.stax.util.IndentingXMLStreamWriter
import com.cedarsoft.version.Version
import com.cedarsoft.version.VersionRange
import java.io.IOException
import java.io.OutputStream
import javax.xml.stream.XMLStreamException
import javax.xml.stream.XMLStreamWriter


/**
 * Abstract base class for serializer using stax.

 * @param <T> the type
</T> */
abstract class AbstractStaxSerializer<T : Any>
/**
 * Creates a new serializer
 * @param defaultElementName the default element name
 * @param nameSpaceUriBase   the name space uri base
 * @param formatVersionRange the format version range
 */
protected
constructor(
  defaultElementName: String,
  nameSpaceUriBase: String,
  formatVersionRange: VersionRange
) : AbstractStaxBasedSerializer<T, XMLStreamWriter>(defaultElementName, nameSpaceUriBase, formatVersionRange) {

  @Throws(Exception::class)
  override fun serialize(objectToSerialize: T, out: OutputStream) {
    try {
      val xmlOutputFactory = StaxSupport.getXmlOutputFactory()
      val writer = wrapWithIndent(xmlOutputFactory.createXMLStreamWriter(out))

      //Sets the name space
      val nameSpace = nameSpace
      writer.setDefaultNamespace(nameSpace)

      writer.writeStartElement(defaultElementName)
      writer.writeDefaultNamespace(nameSpace)

      serialize(writer, objectToSerialize, formatVersion)
      writer.writeEndElement()

      writer.close()
    } catch (e: XMLStreamException) {
      throw SerializationException(e, e.location, SerializationException.Details.XML_EXCEPTION, e.message)
    }
  }

  /**
   * Serializes the elements of a collection
   * @param objects       the objects that are serialized
   * @param type          the type
   * @param elementName   the element name
   * @param serializeTo   the object the elements are serialized to
   * @param formatVersion the format version
   * @throws XMLStreamException if there is an xml problem
   * @throws IOException if there is an io problem
   */
  @Throws(Exception::class)
  protected fun <CT : Any> serializeCollection(objects: Iterable<CT>, type: Class<CT>, elementName: String, serializeTo: XMLStreamWriter, formatVersion: Version) {
    val serializer = getSerializer(type)
    val resolvedVersion = delegatesMappings.resolveVersion(type, formatVersion)

    for (current in objects) {
      serializeTo.writeStartElement(elementName)
      serializer.serialize(serializeTo, current, resolvedVersion)
      serializeTo.writeEndElement()
    }
  }

  @Throws(Exception::class)
  protected fun <CT : Any> serializeCollection(objects: Iterable<CT>, type: Class<CT>, serializeTo: XMLStreamWriter, formatVersion: Version) {
    val serializer = getSerializer(type)
    val resolvedVersion = delegatesMappings.resolveVersion(type, formatVersion)

    for (current in objects) {
      serializeTo.writeStartElement(serializer.defaultElementName)
      serializer.serialize(serializeTo, current, resolvedVersion)
      serializeTo.writeEndElement()
    }
  }

  companion object {
    @JvmStatic
    fun wrapWithIndent(xmlStreamWriter: XMLStreamWriter): XMLStreamWriter {
      return try {
        IndentingXMLStreamWriter(xmlStreamWriter)
      } catch (ignore: Exception) {
        //We could not instantiate the writer
        xmlStreamWriter
      }
    }
  }
}
