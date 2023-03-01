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

package it.neckar.open.serialization.jackson

import it.neckar.open.serialization.SerializingStrategy
import it.neckar.open.serialization.SerializingStrategySupport
import it.neckar.open.serialization.VersionMapping
import it.neckar.open.version.Version
import it.neckar.open.version.VersionException
import it.neckar.open.version.VersionRange
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.JsonToken
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

/**
 * @param <T> the type
 */
abstract class AbstractDelegatingJacksonSerializer<T : Any>
protected constructor(
  nameSpaceUriBase: String, formatVersionRange: VersionRange
) : AbstractJacksonSerializer<T>(nameSpaceUriBase, formatVersionRange) {

  val serializingStrategySupport: SerializingStrategySupport<T, JsonGenerator, JsonParser, OutputStream, InputStream> = SerializingStrategySupport(formatVersionRange)

  val strategies: Collection<SerializingStrategy<out T, JsonGenerator, JsonParser, OutputStream, InputStream>>
    get() = serializingStrategySupport.getStrategies()

  @Throws(IOException::class, VersionException::class, JsonProcessingException::class)
  override fun serialize(serializeTo: JsonGenerator, objectToSerialize: T, formatVersion: Version) {
    assert(isVersionWritable(formatVersion))

    val strategy = serializingStrategySupport.findStrategy(objectToSerialize)
    val resolvedVersion = serializingStrategySupport.resolveVersion(strategy, formatVersion)
    serializeTo.writeStringField(PROPERTY_SUB_TYPE, strategy.id)

    strategy.serialize(serializeTo, objectToSerialize, resolvedVersion)
  }

  @Throws(IOException::class, VersionException::class, JsonProcessingException::class)
  override fun deserialize(deserializeFrom: JsonParser, formatVersion: Version): T {
    assert(isVersionReadable(formatVersion))

    val parserWrapper = JacksonParserWrapper(deserializeFrom)
    parserWrapper.nextToken()
    parserWrapper.verifyCurrentToken(JsonToken.FIELD_NAME)
    val currentName = parserWrapper.currentName

    if (PROPERTY_SUB_TYPE != currentName) {
      throw JsonParseException(parserWrapper.parser, "Invalid field. Expected <$PROPERTY_SUB_TYPE> but was <$currentName>", parserWrapper.currentLocation)
    }
    parserWrapper.nextToken()
    val type = deserializeFrom.text ?: throw JsonParseException(parserWrapper.parser, "Attribute$PROPERTY_SUB_TYPE not found. Cannot find strategy.", deserializeFrom.currentLocation)

    val strategy = serializingStrategySupport.findStrategy(type)
    val resolvedVersion = serializingStrategySupport.resolveVersion(strategy, formatVersion)
    return strategy.deserialize(deserializeFrom, resolvedVersion)
  }

  fun addStrategy(strategy: SerializingStrategy<out T, JsonGenerator, JsonParser, OutputStream, InputStream>): VersionMapping {
    return serializingStrategySupport.addStrategy(strategy)
  }
}
