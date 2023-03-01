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

package it.neckar.open.serialization.stax.mate

import it.neckar.open.serialization.SerializationException
import it.neckar.open.serialization.stax.AbstractStaxBasedSerializer
import it.neckar.open.version.Version
import it.neckar.open.version.VersionRange
import org.codehaus.staxmate.out.SMOutputElement
import java.io.IOException
import java.io.OutputStream
import javax.xml.stream.XMLStreamException

/**
 * Abstract base class for stax mate based serializers
 * @param <T> the type
 */
abstract class AbstractStaxMateSerializer<T : Any>
protected constructor(
  defaultElementName: String,
  nameSpaceUriBase: String,
  formatVersionRange: VersionRange
) : AbstractStaxBasedSerializer<T, SMOutputElement>(defaultElementName, nameSpaceUriBase, formatVersionRange) {

  @Throws(IOException::class)
  override fun serialize(objectToSerialize: T, out: OutputStream) {
    try {
      val factory = StaxMateSupport.getSmOutputFactory()
      val doc = factory.createOutputDocument(out)
      doc.setIndentation(INDENT_STR, 1, 2)

      val nameSpaceUri = nameSpace
      val nameSpace = doc.getNamespace(nameSpaceUri)

      val root = doc.addElement(nameSpace, defaultElementName)
      serialize(root, objectToSerialize, formatVersion)
      doc.closeRoot()
    } catch (e: XMLStreamException) {
      throw SerializationException(e, e.location, SerializationException.Details.XML_EXCEPTION, e.message)
    }
  }

  /**
   * Serializes the given object into a sub element of serializeTo
   * @param objectToSerialize        the object that is serialized
   * @param type          the type
   * @param subElementName  the name of the sub element
   * @param serializeTo   the parent element
   * @param formatVersion the format version
   * @param <A>           the type
   * @throws XMLStreamException if there is an xml problem
   * @throws IOException if there is an io problem
  </A> */
  @Throws(XMLStreamException::class, IOException::class)
  fun <A : Any> serialize(objectToSerialize: A, type: Class<A>, subElementName: String, serializeTo: SMOutputElement, formatVersion: Version) {
    val element = serializeTo.addElement(serializeTo.namespace, subElementName)
    serialize(objectToSerialize, type, element, formatVersion)
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
  @Throws(XMLStreamException::class, IOException::class)
  protected fun <A : Any> serializeCollection(objects: Iterable<A>, type: Class<A>, elementName: String, serializeTo: SMOutputElement, formatVersion: Version) {
    val serializer = getSerializer(type)
    val resolvedVersion = delegatesMappings.resolveVersion(type, formatVersion)

    for (current in objects) {
      val doorElement = serializeTo.addElement(serializeTo.namespace, elementName)
      serializer.serialize(doorElement, current, resolvedVersion)
    }
  }

  @Throws(XMLStreamException::class, IOException::class)
  protected fun <A : Any> serializeCollection(objects: Iterable<A>, type: Class<A>, serializeTo: SMOutputElement, formatVersion: Version) {
    val serializer = getSerializer(type)
    val resolvedVersion = delegatesMappings.resolveVersion(type, formatVersion)

    for (current in objects) {
      val doorElement = serializeTo.addElement(serializeTo.namespace, serializer.defaultElementName)
      serializer.serialize(doorElement, current, resolvedVersion)
    }
  }

  /**
   * Serializes the elements of the collection to a own sub element
   * @param objects               the objects that are serialized
   * @param type                  the type
   * @param collectionElementName the collection element name
   * @param elementName           the element name
   * @param serializeTo           the object the elements are serialized to
   * @param formatVersion         the format version
   * @throws XMLStreamException if there is an xml problem
   * @throws IOException if there is an io problem
   */
  @Throws(XMLStreamException::class, IOException::class)
  protected fun <A : Any> serializeCollectionToElement(objects: Iterable<A>, type: Class<A>, collectionElementName: String, elementName: String, serializeTo: SMOutputElement, formatVersion: Version) {
    val collectionElement = serializeTo.addElement(serializeTo.namespace, collectionElementName)
    serializeCollection(objects, type, elementName, collectionElement, formatVersion)
  }

  @Throws(XMLStreamException::class, IOException::class)
  protected fun <A : Any> serializeCollectionToElement(objects: Iterable<A>, type: Class<A>, collectionElementName: String, serializeTo: SMOutputElement, formatVersion: Version) {
    val collectionElement = serializeTo.addElement(serializeTo.namespace, collectionElementName)
    serializeCollection(objects, type, collectionElement, formatVersion)
  }

  @Throws(XMLStreamException::class)
  protected fun serializeToElementWithCharacters(elementName: String, characters: String, serializeTo: SMOutputElement) {
    serializeTo.addElementWithCharacters(serializeTo.namespace, elementName, characters)
  }

  /**
   * Serializes an enum (as attribute)
   * @param enumValue    the num value
   * @param propertyName the property name
   * @param serializeTo  the object to serialize to
   * @throws XMLStreamException
   */
  @Throws(XMLStreamException::class)
  fun serializeEnum(enumValue: Enum<*>, propertyName: String, serializeTo: SMOutputElement) {
    serializeTo.addAttribute(propertyName, enumValue.name)
  }

  companion object {
    @JvmStatic
    private val INDENT_STR = "\n                            "
  }
}
