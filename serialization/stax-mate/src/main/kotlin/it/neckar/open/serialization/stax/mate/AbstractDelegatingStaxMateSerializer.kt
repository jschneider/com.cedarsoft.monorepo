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
import it.neckar.open.serialization.SerializingStrategy
import it.neckar.open.serialization.SerializingStrategySupport
import it.neckar.open.serialization.VersionMapping
import it.neckar.open.version.Version
import it.neckar.open.version.VersionRange
import org.codehaus.staxmate.out.SMOutputElement
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import javax.xml.stream.XMLStreamException
import javax.xml.stream.XMLStreamReader

/**
 * Abstract base class for a serializers that uses strategies to serialize objects.
 * @param <T> the type
</T> */
abstract class AbstractDelegatingStaxMateSerializer<T : Any>
/**
 * Creates a new serializer
 * @param defaultElementName the default element name
 * @param nameSpaceUriBase   the name space uri base
 * @param formatVersionRange the format version name
 */
protected constructor(defaultElementName: String, nameSpaceUriBase: String, formatVersionRange: VersionRange) : AbstractStaxMateSerializer<T>(defaultElementName, nameSpaceUriBase, formatVersionRange) {
  val serializingStrategySupport: SerializingStrategySupport<T, SMOutputElement, XMLStreamReader, OutputStream, InputStream> = SerializingStrategySupport(formatVersionRange)

  /**
   * Returns the strategies
   * @return the strategies
   */
  val strategies: Collection<SerializingStrategy<out T, SMOutputElement, XMLStreamReader, OutputStream, InputStream>>
    get() = serializingStrategySupport.getStrategies()

  fun addStrategy(strategy: SerializingStrategy<out T, SMOutputElement, XMLStreamReader, OutputStream, InputStream>): VersionMapping {
    return serializingStrategySupport.addStrategy(strategy)
  }

  @Throws(IOException::class)
  override fun serialize(serializeTo: SMOutputElement, objectToSerialize: T, formatVersion: Version) {
    assert(isVersionWritable(formatVersion))

    try {
      val strategy: SerializingStrategy<T, SMOutputElement, XMLStreamReader, OutputStream, InputStream> = serializingStrategySupport.findStrategy(objectToSerialize)
      val resolvedVersion = serializingStrategySupport.resolveVersion(strategy, formatVersion)
      serializeTo.addAttribute(ATTRIBUTE_TYPE, strategy.id)

      strategy.serialize(serializeTo, objectToSerialize, resolvedVersion)
    } catch (e: XMLStreamException) {
      throw SerializationException(e, e.location, SerializationException.Details.XML_EXCEPTION, e.message)
    }

  }

  @Throws(IOException::class, XMLStreamException::class)
  override fun deserialize(deserializeFrom: XMLStreamReader, formatVersion: Version): T {
    assert(isVersionReadable(formatVersion))
    val type = deserializeFrom.getAttributeValue(null, ATTRIBUTE_TYPE) ?: throw SerializationException(SerializationException.Details.NO_TYPE_ATTRIBUTE)

    val strategy = serializingStrategySupport.findStrategy(type)
    val resolvedVersion = serializingStrategySupport.resolveVersion(strategy, formatVersion)
    return strategy.deserialize(deserializeFrom, resolvedVersion)
  }

  companion object {
    private const val ATTRIBUTE_TYPE = "type"
  }
}

