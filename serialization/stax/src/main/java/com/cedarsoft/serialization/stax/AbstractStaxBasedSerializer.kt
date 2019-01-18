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

import com.cedarsoft.serialization.AbstractXmlSerializer
import com.cedarsoft.serialization.SerializationException
import com.cedarsoft.version.Version
import com.cedarsoft.version.VersionException
import com.cedarsoft.version.VersionRange
import java.io.IOException
import java.io.InputStream
import java.util.ArrayList
import javax.xml.stream.XMLStreamConstants
import javax.xml.stream.XMLStreamException
import javax.xml.stream.XMLStreamReader

/**
 * Abstract base class for stax based serializers.
 *
 *
 * ATTENTION:
 * Serializers based on stax must consume all events for their tag (including END_ELEMENT).<br></br>
 * This is especially true for com.cedarsoft.serialization.PluggableSerializers.
 * @param <T> the type
 * @param <S> the object to serialize to
 */
abstract class AbstractStaxBasedSerializer<T, S>
/**
 * Creates a new serializer
 * @param defaultElementName the name for the root element, if this serializers is not used as delegate. For delegating serializers that value is not used.
 * @param nameSpaceUriBase   the name space uri base
 * @param formatVersionRange the supported format version range. The upper bound represents the format that is written. All Versions within the range can be read.
 */
protected constructor(
  defaultElementName: String,
  nameSpaceUriBase: String,
  formatVersionRange: VersionRange
) : AbstractXmlSerializer<T, S, XMLStreamReader, XMLStreamException>(defaultElementName, nameSpaceUriBase, formatVersionRange), StaxBasedSerializer<T, S> {

  @Throws(IOException::class, VersionException::class)
  override fun deserialize(deserializeFrom: InputStream): T {
    try {
      val reader = StaxSupport.getXmlInputFactory().createXMLStreamReader(deserializeFrom)

      val result = reader.nextTag()
      if (result != XMLStreamConstants.START_ELEMENT) {
        throw SerializationException(reader.location, SerializationException.Details.INVALID_START_ELEMENT, result)
      }

      //Now build the deserialization context
      val deserialized = deserialize(reader, parseAndVerifyNameSpace(reader.namespaceURI))

      if (!reader.isEndElement) {
        throw SerializationException(reader.location, SerializationException.Details.NOT_CONSUMED_EVERYTHING, javaClass.name)
      }
      if (nextEndDocument(reader)) {
        throw SerializationException(reader.location, SerializationException.Details.NOT_CONSUMED_EVERYTHING, javaClass.name)
      }

      return deserialized
    } catch (e: XMLStreamException) {
      throw SerializationException(e, e.location, SerializationException.Details.XML_EXCEPTION, e.message)
    }
  }

  /**
   * Ensures that the current tag equals the given tag name and namespace
   * @param streamReader the stream reader
   * @param tagName      the tag name
   * @param namespace    the (optional) namespace (if the ns is null, no check will be performed)
   */
  @Throws(XMLStreamException::class)
  @JvmOverloads protected fun ensureTag(streamReader: XMLStreamReader, tagName: String, namespace: String? = null) {
    val qName = streamReader.name

    if (!doesNamespaceFit(streamReader, namespace)) {
      throw XMLStreamException("Invalid namespace for <" + qName.localPart + ">. Was <" + qName.namespaceURI + "> but expected <" + namespace + "> @ " + streamReader.location)
    }

    val current = qName.localPart
    if (current != tagName) {
      throw XMLStreamException("Invalid tag. Was <" + current + "> but expected <" + tagName + "> @ " + streamReader.location)
    }
  }

  /**
   * Returns whether the namespace fits
   * @param streamReader the stream reader that is used to extract the namespace
   * @param namespace    the expected namespace (or null if the check shall be skipped)
   * @return true if the namespace fits (or the expected namespace is null), false otherwise
   */
  protected fun doesNamespaceFit(streamReader: XMLStreamReader, namespace: String?): Boolean {
    if (namespace == null) {
      return true
    }

    val qName = streamReader.name
    val nsUri = qName.namespaceURI
    return nsUri == namespace
  }

  /**
   * Returns the child text
   * @param reader    the reader
   * @param tagName   the tag name
   * @param namespace the (optional) namespace that is only verified, if it is not null
   * @return the text of the child with the given tag name
   * @throws XMLStreamException
   */
  @Throws(XMLStreamException::class)
  @JvmOverloads protected fun getChildText(reader: XMLStreamReader, tagName: String, namespace: String? = null): String {
    nextTag(reader, tagName)
    ensureTag(reader, tagName, namespace)
    return getText(reader)
  }

  /**
   * Closes the tag
   * @param reader the reader
   * @param skipElementsWithOtherNamespaces whether tos kip elements with other namespaces
   * @throws XMLStreamException
   */
  @Throws(XMLStreamException::class)
  @JvmOverloads protected fun closeTag(reader: XMLStreamReader, skipElementsWithOtherNamespaces: Boolean = true) {
    val result = reader.nextTag()
    if (result == XMLStreamConstants.END_ELEMENT) {
      return
    }

    if (!skipElementsWithOtherNamespaces || result != XMLStreamConstants.START_ELEMENT) {
      throw XMLStreamException("Invalid result. Expected <END_ELEMENT> but was <" + StaxSupport.getEventName(result) + "> @ " + reader.location)
    }

    if (doesNamespaceFit(reader, nameSpace)) {
      throw XMLStreamException("Invalid result. Expected <END_ELEMENT> but was <" + StaxSupport.getEventName(result) + "> @ " + reader.location)
    }

    skipCurrentTag(reader)
    closeTag(reader)
  }

  @Throws(XMLStreamException::class)
  protected fun nextEndDocument(reader: XMLStreamReader): Boolean {
    var next = reader.next()
    //Skip comments
    while (next == XMLStreamConstants.COMMENT) {
      next = reader.next()
    }

    return next != XMLStreamConstants.END_DOCUMENT
  }

  /**
   * Opens the next tag
   * @param reader    the reader
   * @param tagName   the tag name
   * @param namespace the (optional) namespace (if the ns is null, no check will be performed)
   * @param skipElementsWithOtherNamespaces whether to skip unknown namespaces
   * @throws XMLStreamException
   */
  @Throws(XMLStreamException::class)
  @JvmOverloads protected fun nextTag(reader: XMLStreamReader, tagName: String, namespace: String? = null, skipElementsWithOtherNamespaces: Boolean = true) {
    val result = reader.nextTag()
    if (result != XMLStreamConstants.START_ELEMENT) {
      throw XMLStreamException("Invalid result. Expected <START_ELEMENT> but was <" + StaxSupport.getEventName(result) + ">" + " @ " + reader.location)
    }

    if (skipElementsWithOtherNamespaces && !doesNamespaceFit(reader, namespace)) {
      skipCurrentTag(reader)
      nextTag(reader, tagName, namespace, skipElementsWithOtherNamespaces)
    } else {
      ensureTag(reader, tagName, namespace)
    }
  }

  /**
   * Skips the current tag (will skip all children and close the tag itself)
   * @param reader the reader
   * @throws XMLStreamException
   */
  @Throws(XMLStreamException::class)
  protected fun skipCurrentTag(reader: XMLStreamReader) {
    var counter = 1

    while (counter > 0) {
      val result = reader.next()

      if (result == XMLStreamConstants.END_ELEMENT) {
        counter--
      } else if (result == XMLStreamConstants.START_ELEMENT) {
        counter++
      }
    }
  }

  /**
   * Attention! The current element will be closed!
   * @param streamReader the stream reader
   * @param callback     the callback
   * @throws XMLStreamException if there is an xml problem
   * @throws IOException if there is an io problem
   */
  @Throws(XMLStreamException::class, IOException::class)
  protected fun visitChildren(streamReader: XMLStreamReader, callback: CB) {
    while (streamReader.nextTag() != XMLStreamConstants.END_ELEMENT) {
      val tagName = streamReader.name.localPart
      callback.tagEntered(streamReader, tagName)
    }
  }

  /**
   * Convenience method
   * @param deserializeFrom where it is deserialized from
   * @param type            the type
   * @param formatVersion   the format version
   * @return the deserialized objects
   * @throws XMLStreamException if there is an xml problem
   * @throws IOException if there is an io problem
   */
  @Throws(XMLStreamException::class, IOException::class)
  protected fun <L> deserializeCollection(deserializeFrom: XMLStreamReader, type: Class<L>, formatVersion: Version): List<L> {
    return deserializeCollection(type, formatVersion, deserializeFrom)
  }

  /**
   * Deserializes a collection
   * @param type the type
   * @param formatVersion the format version
   * @param deserializeFrom deserialize from
   * @param <L> the type
   * @return the deserialized collection
   * @throws XMLStreamException if there is an xml problem
   * @throws IOException if there is an io problem
  </L> */
  @Throws(XMLStreamException::class, IOException::class)
  protected fun <L> deserializeCollection(type: Class<L>, formatVersion: Version, deserializeFrom: XMLStreamReader): List<L> {
    val deserializedObjects = ArrayList<L>()

    visitChildren(deserializeFrom, object : CB {
      @Throws(XMLStreamException::class, IOException::class)
      override fun tagEntered(deserializeFrom: XMLStreamReader, tagName: String) {
        deserializedObjects.add(deserialize(type, formatVersion, deserializeFrom))
      }
    })

    return deserializedObjects
  }

  /**
   * Convenience method to deserialize multiple collections without having to deal with callbacks
   * @param deserializeFrom    the object to deserialize from
   * @param formatVersion      the format version
   * @param collectionsMapping the collections mapping that holds references to the collections that are filled
   * @throws XMLStreamException if there is an xml problem
   * @throws IOException if there is an io problem
   */
  @Throws(XMLStreamException::class, IOException::class)
  protected fun deserializeCollections(deserializeFrom: XMLStreamReader, formatVersion: Version, collectionsMapping: CollectionsMapping) {
    visitChildren(deserializeFrom, object : CB {
      @Throws(XMLStreamException::class, IOException::class)
      override fun tagEntered(deserializeFrom: XMLStreamReader, tagName: String) {
        @Suppress("UNCHECKED_CAST")
        val entry = collectionsMapping.getEntry(tagName) as CollectionsMapping.Entry<Any>
        val deserialized = deserialize(entry.type, formatVersion, deserializeFrom)
        entry.targetCollection.add(deserialized)
      }
    })
  }

  /**
   * Deserializes the enum
   * @param enumType        the enum type
   * @param propertyName    the property name
   * @param deserializeFrom the object to deserialize from
   * @param <E>             the type
   * @return the deserialized enum
  </E> */
  fun <E : Enum<E>> deserializeEnum(enumType: Class<E>, propertyName: String, deserializeFrom: XMLStreamReader): E {
    val enumValue = deserializeFrom.getAttributeValue(null, propertyName)
    return java.lang.Enum.valueOf<E>(enumType, enumValue)
  }

  /**
   * Callback interface used when visiting the children (com.cedarsoft.serialization.stax.AbstractStaxBasedSerializer#visitChildren(XMLStreamReader, com.cedarsoft.serialization.stax.AbstractStaxBasedSerializer.CB))
   */
  @FunctionalInterface
  interface CB {
    /**
     * Is called for each child.
     * ATTENTION: This method *must* close the tag
     * @param deserializeFrom the reader
     * @param tagName         the tag name
     * @throws XMLStreamException if there is an xml problem
     * @throws IOException if there is an io problem
     */
    @Throws(XMLStreamException::class, IOException::class)
    fun tagEntered(deserializeFrom: XMLStreamReader, tagName: String)
  }

  companion object {
    /**
     * Returns the text and closes the tag
     * @param reader the reader
     * @return the text
     * @throws XMLStreamException
     */
    @JvmStatic
    @Throws(XMLStreamException::class)
    fun getText(reader: XMLStreamReader): String {
      val content = StringBuilder()

      for (result in generateSequence { reader.next().takeUnless { it == XMLStreamConstants.END_ELEMENT } }) {
        if (result != XMLStreamConstants.CHARACTERS) {
          throw XMLStreamException("Invalid result: " + result + " @ " + reader.location)
        }
        content.append(reader.text)
      }

      return content.toString()
    }
  }
}
