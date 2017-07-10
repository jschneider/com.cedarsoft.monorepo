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

package com.cedarsoft.serialization.jackson

import com.cedarsoft.serialization.AbstractStreamSerializer
import com.cedarsoft.serialization.SerializationException
import com.cedarsoft.version.Version
import com.cedarsoft.version.VersionException
import com.cedarsoft.version.VersionRange
import com.fasterxml.jackson.core.JsonEncoding
import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.JsonToken
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.ArrayList
import javax.annotation.WillNotClose

/**
 * @param <T> the type
 * *
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
</T> */
abstract class AbstractJacksonSerializer<T>
protected constructor(
  override val type: String,
  formatVersionRange: VersionRange) : AbstractStreamSerializer<T, JsonGenerator, JsonParser, JsonProcessingException>(formatVersionRange), JacksonSerializer<T> {

  override val isObjectType = true

  @Throws(SerializationException::class)
  override fun verifyType(type: String?) {
    if (this.type != type) {//$NON-NLS-1$
      throw SerializationException(SerializationException.Details.INVALID_TYPE, this.type, type)
    }
  }

  @Throws(IOException::class)
  override fun serialize(objectToSerialize: T, @WillNotClose out: OutputStream) {
    val jsonFactory = JacksonSupport.getJsonFactory()
    val generator = jsonFactory.createGenerator(out, JsonEncoding.UTF8)

    serialize(objectToSerialize, generator)
    generator.flush()
  }

  /**
   * Serializes the object to the given serializeTo.
   *
   *
   * The serializer is responsible for writing start/close object/array brackets if necessary.
   * This method also writes the @type property.

   * @param objectToSerialize    the object that is serialized
   * *
   * @param generator the serialize to object
   * *
   * @throws java.io.IOException if there is an io problem
   */
  @Throws(IOException::class)
  override fun serialize(objectToSerialize: T, generator: JsonGenerator) {
    if (isObjectType) {
      generator.writeStartObject()

      beforeTypeAndVersion(objectToSerialize, generator)
      writeTypeAndVersion(generator)
    }

    serialize(generator, objectToSerialize, formatVersion)

    if (isObjectType) {
      generator.writeEndObject()
    }
  }

  @Throws(IOException::class)
  protected open fun writeTypeAndVersion(generator: JsonGenerator) {
    generator.writeStringField(PROPERTY_TYPE, type)
    generator.writeStringField(PROPERTY_VERSION, formatVersion.format())
  }

  @Throws(IOException::class)
  protected fun beforeTypeAndVersion(`object`: T, serializeTo: JsonGenerator) {
  }

  @Throws(IOException::class, VersionException::class)
  override fun deserialize(`in`: InputStream): T {
    return deserialize(`in`, null)
  }

  @Throws(IOException::class, JsonProcessingException::class, SerializationException::class)
  override fun deserialize(parser: JsonParser): T {
    return deserializeInternal(parser, null)
  }

  @Throws(IOException::class, VersionException::class)
  fun deserialize(`in`: InputStream, version: Version?): T {
    val jsonFactory = JacksonSupport.getJsonFactory()
    val parser = createJsonParser(jsonFactory, `in`)

    val deserialized = deserializeInternal(parser, version)

    val parserWrapper = JacksonParserWrapper(parser)
    if (parserWrapper.nextToken() != null) {
      throw JsonParseException(parser, "No consumed everything " + parserWrapper.currentToken, parserWrapper.currentLocation)
    }

    parserWrapper.close()
    return deserialized
  }

  /**
   * This method creates the parser. This method may be overridden to create a FilteringJsonParser or something like that
   * @param jsonFactory the json factory
   * *
   * @param in the input stream
   * *
   * @return the created json parser
   * *
   * @throws java.io.IOException if there is an io problem
   */
  @Throws(IOException::class)
  protected fun createJsonParser(jsonFactory: JsonFactory, `in`: InputStream): JsonParser {
    return jsonFactory.createParser(`in`)
  }

  /**
   * If the format version override is not null, the type and version field are skipped
   * @param parser the parser
   * *
   * @param formatVersionOverride the format version override (usually "null")
   * *
   * @return the deserialized object
   * *
   * @throws java.io.IOException if there is an io problem
   */
  @Throws(IOException::class, JsonProcessingException::class, SerializationException::class)
  protected fun deserializeInternal(parser: JsonParser, formatVersionOverride: Version?): T {
    val parserWrapper = JacksonParserWrapper(parser)

    val version = prepareDeserialization(parserWrapper, formatVersionOverride)

    val deserialized = deserialize(parser, version)

    if (isObjectType) {
      if (parserWrapper.currentToken != JsonToken.END_OBJECT) {
        throw JsonParseException(parser, "No consumed everything " + parserWrapper.currentToken, parserWrapper.currentLocation)
      }
    }

    return deserialized
  }

  /**
   * Prepares the deserialization.

   * If the format version is set - the type and version properties are *not* read!
   * This can be useful for cases where this information is not available...

   * @param wrapper the wrapper
   * *
   * @param formatVersionOverride the format version
   * *
   * @return the format version
   * *
   * @throws java.io.IOException if there is an io problem
   */
  @Throws(IOException::class, SerializationException::class)
  protected open fun prepareDeserialization(wrapper: JacksonParserWrapper, formatVersionOverride: Version?): Version {
    if (isObjectType) {
      wrapper.nextToken(JsonToken.START_OBJECT)

      beforeTypeAndVersion(wrapper)

      if (formatVersionOverride == null) {
        wrapper.nextFieldValue(PROPERTY_TYPE)
        val readType = wrapper.text
        verifyType(readType)
        wrapper.nextFieldValue(PROPERTY_VERSION)
        val version = Version.parse(wrapper.text)
        verifyVersionReadable(version)
        return version
      } else {
        verifyVersionReadable(formatVersionOverride)
        return formatVersionOverride
      }
    } else {
      //Not an object type
      wrapper.nextToken()
      return formatVersion
    }
  }

  /**
   * Callback method that is called before the type and version are parsed

   * @param wrapper the wrapper
   * *
   * @throws java.io.IOException if there is an io exception
   */
  @Throws(IOException::class, JsonProcessingException::class, SerializationException::class)
  protected fun beforeTypeAndVersion(wrapper: JacksonParserWrapper) {
  }

  @Throws(IOException::class)
  protected fun <A> serializeArray(elements: Iterable<A>, type: Class<A>, serializeTo: JsonGenerator, formatVersion: Version) {
    serializeArray(elements, type, null, serializeTo, formatVersion)
  }

  @Throws(IOException::class)
  protected fun <A> serializeArray(elements: Iterable<A>, type: Class<A>, propertyName: String?, serializeTo: JsonGenerator, formatVersion: Version) {
    val serializer = getSerializer(type)
    val delegateVersion = delegatesMappings.versionMappings.resolveVersion(type, formatVersion)

    if (propertyName == null) {
      serializeTo.writeStartArray()
    } else {
      serializeTo.writeArrayFieldStart(propertyName)
    }
    for (element in elements) {
      if (serializer.isObjectType) {
        serializeTo.writeStartObject()
      }

      serializer.serialize(serializeTo, element, delegateVersion)

      if (serializer.isObjectType) {
        serializeTo.writeEndObject()
      }
    }
    serializeTo.writeEndArray()
  }

  @Throws(IOException::class)
  protected fun <A> deserializeArray(type: Class<A>, formatVersion: Version, deserializeFrom: JsonParser): List<A> {
    return deserializeArray(type, deserializeFrom, formatVersion)
  }

  @Throws(IOException::class)
  protected fun <A> deserializeArray(type: Class<A>, deserializeFrom: JsonParser, formatVersion: Version): List<A> {
    return deserializeArray(type, null, formatVersion, deserializeFrom)
  }

  @Throws(IOException::class)
  protected fun <A> deserializeArray(type: Class<A>, propertyName: String?, formatVersion: Version, deserializeFrom: JsonParser): List<A> {
    val parserWrapper = JacksonParserWrapper(deserializeFrom)
    if (propertyName == null) {
      parserWrapper.verifyCurrentToken(JsonToken.START_ARRAY)
    } else {
      parserWrapper.nextToken()
      parserWrapper.verifyCurrentToken(JsonToken.FIELD_NAME)
      val currentName = parserWrapper.currentName

      if (propertyName != currentName) {
        throw JsonParseException(parserWrapper.parser, "Invalid field. Expected <$propertyName> but was <$currentName>", parserWrapper.currentLocation)
      }
      parserWrapper.nextToken()
    }

    val deserialized = ArrayList<A>()
    while (deserializeFrom.nextToken() != JsonToken.END_ARRAY) {
      deserialized.add(deserialize(type, formatVersion, deserializeFrom))
    }
    return deserialized
  }

  /**
   * Has a strange order of parameters. Do not use anymore
   */
  @Deprecated("wrong order of parameters", replaceWith = ReplaceWith("deserializeArray(type, propertyName, formatVersion, deserializeFrom)"))
  @Throws(IOException::class)
  protected fun <A> deserializeArray(type: Class<A>, propertyName: String?, deserializeFrom: JsonParser, formatVersion: Version): List<A> {
    return deserializeArray(type, propertyName, formatVersion, deserializeFrom)
  }

  /**
   * Serializes the object if it is *not* null.
   * If the object is null nothing will be written.
   */
  @Throws(JsonProcessingException::class, IOException::class)
  fun <A> serializeIfNotNull(`object`: A?, type: Class<A>, propertyName: String, serializeTo: JsonGenerator, formatVersion: Version) {
    if (`object` == null) {
      return
    }

    serialize(`object`, type, propertyName, serializeTo, formatVersion)
  }

  /**
   * Serializes the object. Writes "null" if the object is null
   */
  @Throws(JsonProcessingException::class, IOException::class)
  fun <A> serialize(`object`: A?, type: Class<A>, propertyName: String, serializeTo: JsonGenerator, formatVersion: Version) {
    serializeTo.writeFieldName(propertyName)

    //Fast exit if the value is null
    if (`object` == null) {
      serializeTo.writeNull()
      return
    }

    val serializer = getSerializer(type)
    val delegateVersion = delegatesMappings.versionMappings.resolveVersion(type, formatVersion)
    if (serializer.isObjectType) {
      serializeTo.writeStartObject()
    }

    serializer.serialize(serializeTo, `object`, delegateVersion)

    if (serializer.isObjectType) {
      serializeTo.writeEndObject()
    }
  }

  @Throws(IOException::class, JsonProcessingException::class)
  protected fun <A> deserializeNullable(type: Class<A>, propertyName: String, formatVersion: Version, deserializeFrom: JsonParser): A? {
    val parserWrapper = JacksonParserWrapper(deserializeFrom)
    parserWrapper.nextFieldValue(propertyName)

    if (parserWrapper.currentToken == JsonToken.VALUE_NULL) {
      return null
    }

    return deserialize(type, formatVersion, deserializeFrom)
  }

  @Throws(IOException::class, JsonProcessingException::class)
  protected fun <A> deserialize(type: Class<A>, propertyName: String, formatVersion: Version, deserializeFrom: JsonParser): A {
    val parserWrapper = JacksonParserWrapper(deserializeFrom)
    parserWrapper.nextToken()
    parserWrapper.verifyCurrentToken(JsonToken.FIELD_NAME)
    val currentName = parserWrapper.currentName

    if (propertyName != currentName) {
      throw JsonParseException(parserWrapper.parser, "Invalid field. Expected <$propertyName> but was <$currentName>", parserWrapper.currentLocation)
    }
    parserWrapper.nextToken()
    return deserialize(type, formatVersion, deserializeFrom)
  }

  /**
   * Supports null values
   */
  @Throws(JsonProcessingException::class, IOException::class)
  fun <A> deserializeNullable(type: Class<A>, formatVersion: Version, deserializeFrom: JsonParser): A? {
    if (deserializeFrom.currentToken == JsonToken.VALUE_NULL) {
      return null
    }
    return super.deserialize(type, formatVersion, deserializeFrom)
  }

  override fun <A> getSerializer(type: Class<A>): JacksonSerializer<in A> {
    return super.getSerializer(type) as JacksonSerializer<in A>
  }

  @Throws(IOException::class)
  fun serializeEnum(enumValue: Enum<*>, propertyName: String, serializeTo: JsonGenerator) {
    serializeTo.writeStringField(propertyName, enumValue.name)
  }

  /**
   * Deserializes the enumeration

   * @param enumClass    the enum class
   * *
   * @param propertyName the property name
   * *
   * @param parser       the parser
   * *
   * @param <A>          the type
   * *
   * @return the deserialized enum
   * *
   * *
   * @throws IOException
  </A> */
  @Throws(IOException::class)
  fun <A : Enum<A>> deserializeEnum(enumClass: Class<A>, propertyName: String, parser: JsonParser): A {
    val wrapper = JacksonParserWrapper(parser)
    wrapper.nextFieldValue(propertyName)
    return java.lang.Enum.valueOf<A>(enumClass, parser.text)
  }

  @Throws(IOException::class)
  fun <A : Enum<A>> deserializeEnum(enumClass: Class<A>, parser: JsonParser): A {
    return java.lang.Enum.valueOf<A>(enumClass, parser.text)
  }

  @Deprecated("")
  @Throws(IOException::class)
  fun <A : Enum<A>> deserializeEnum(enumClass: Class<A>, propertyName: String, parser: JacksonParserWrapper): A {
    parser.nextFieldValue(propertyName)
    return java.lang.Enum.valueOf<A>(enumClass, parser.text)
  }

  companion object {
    @JvmField
    val FIELD_NAME_DEFAULT_TEXT = "$"
    @JvmField
    val PROPERTY_TYPE = "@type"
    @JvmField
    val PROPERTY_VERSION = "@version"
    @JvmField
    val PROPERTY_SUB_TYPE = "@subtype"
  }
}
