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

import it.neckar.open.serialization.SerializationException
import com.fasterxml.jackson.core.Base64Variant
import com.fasterxml.jackson.core.FormatSchema
import com.fasterxml.jackson.core.JsonLocation
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.JsonStreamContext
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.core.ObjectCodec
import com.fasterxml.jackson.core.TreeNode
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.core.type.TypeReference
import java.io.IOException
import java.io.OutputStream
import java.io.Writer
import java.math.BigDecimal
import java.math.BigInteger

/**
 * Wrapper class for jackson
 */
class JacksonParserWrapper(val parser: JsonParser) {

  @Throws(IOException::class, JsonParseException::class)
  fun startObject() {
    nextToken(JsonToken.START_OBJECT)
  }

  @Throws(IOException::class, JsonParseException::class)
  fun endObject() {
    nextToken(JsonToken.END_OBJECT)
  }

  /**
   * Verifies the next field has the given name and prepares for read (by calling parser.nextToken).
   * @param fieldName the field name
   * @throws IOException if there is an io problem
   */
  @Throws(IOException::class)
  fun nextFieldValue(fieldName: String) {
    nextField(fieldName)
    parser.nextToken()
  }

  /**
   * Verifies that the next field starts.
   * When the content of the field shall be accessed, it is necessary to call parser.nextToken() afterwards.
   * @param fieldName the field name
   * @throws IOException if there is an io problem
   */
  @Throws(IOException::class)
  fun nextField(fieldName: String) {
    nextToken(JsonToken.FIELD_NAME)
    val currentName = parser.currentName

    if (fieldName != currentName) {
      throw JsonParseException(parser, "Invalid field. Expected <$fieldName> but was <$currentName>", parser.currentLocation)
    }
  }

  @Throws(IOException::class)
  fun nextToken(expected: JsonToken) {
    parser.nextToken()
    verifyCurrentToken(expected)
  }

  @Throws(JsonParseException::class)
  fun verifyCurrentToken(expected: JsonToken) {
    val current = parser.currentToken
    if (current != expected) {
      throw JsonParseException(parser, "Invalid token. Expected <$expected> but got <$current>", parser.currentLocation)
    }
  }

  @Throws(IOException::class)
  fun closeObject() {
    nextToken(JsonToken.END_OBJECT)
  }

  @Throws(JsonParseException::class)
  fun ensureObjectClosed() {
    val parserWrapper = JacksonParserWrapper(parser)

    if (parserWrapper.currentToken != JsonToken.END_OBJECT) {
      throw JsonParseException(parser, "No consumed everything " + parserWrapper.currentToken, parserWrapper.currentLocation)
    }
  }

  @Throws(IOException::class)
  fun ensureParserClosed() {
    val parserWrapper = JacksonParserWrapper(parser)
    if (parserWrapper.nextToken() != null) {
      throw JsonParseException(parser, "No consumed everything " + parserWrapper.currentToken, parserWrapper.currentLocation)
    }

    parserWrapper.close()
  }

  /**
   * Helper method that throws an exception if the value is null
   * @param deserializedValue the deserialized value
   * @param propertyName the property name
   */
  fun verifyDeserialized(deserializedValue: Any?, propertyName: String) {
    if (deserializedValue == null) {
      throw SerializationException(SerializationException.Details.PROPERTY_NOT_DESERIALIZED, propertyName)
    }
  }

  //Delegating

  @Throws(IOException::class, JsonParseException::class)
  fun getValueAsInt(defaultValue: Int): Int {
    return parser.getValueAsInt(defaultValue)
  }

  val valueAsLong: Long
    @Throws(IOException::class, JsonParseException::class)
    get() = parser.valueAsLong

  @Throws(IOException::class, JsonParseException::class)
  fun getValueAsLong(defaultValue: Long): Long {
    return parser.getValueAsLong(defaultValue)
  }

  val valueAsDouble: Double
    @Throws(IOException::class, JsonParseException::class)
    get() = parser.valueAsDouble

  @Throws(IOException::class, JsonParseException::class)
  fun getValueAsDouble(defaultValue: Double): Double {
    return parser.getValueAsDouble(defaultValue)
  }

  val valueAsBoolean: Boolean
    @Throws(IOException::class, JsonParseException::class)
    get() = parser.valueAsBoolean

  @Throws(IOException::class, JsonParseException::class)
  fun getValueAsBoolean(defaultValue: Boolean): Boolean {
    return parser.getValueAsBoolean(defaultValue)
  }

  @Throws(IOException::class, JsonProcessingException::class)
  fun <A> readValueAs(valueType: Class<A>): A {
    return parser.readValueAs(valueType)
  }

  @Throws(IOException::class, JsonProcessingException::class)
  fun <A> readValueAs(valueTypeRef: TypeReference<*>): A {
    return parser.readValueAs<A>(valueTypeRef)
  }

  @Throws(IOException::class, JsonProcessingException::class)
  fun readValueAsTree(): TreeNode {
    return parser.readValueAsTree<TreeNode>()
  }

  var codec: ObjectCodec
    get() = parser.codec
    set(c) {
      parser.codec = c
    }

  fun setSchema(schema: FormatSchema) {
    parser.schema = schema
  }

  fun canUseSchema(schema: FormatSchema): Boolean {
    return parser.canUseSchema(schema)
  }

  fun version(): Version {
    return parser.version()
  }

  val inputSource: Any
    get() = parser.inputSource

  @Throws(IOException::class)
  fun close() {
    parser.close()
  }

  @Throws(IOException::class)
  fun releaseBuffered(out: OutputStream): Int {
    return parser.releaseBuffered(out)
  }

  @Throws(IOException::class)
  fun releaseBuffered(w: Writer): Int {
    return parser.releaseBuffered(w)
  }

  fun enable(f: JsonParser.Feature): JsonParser {
    return parser.enable(f)
  }

  fun disable(f: JsonParser.Feature): JsonParser {
    return parser.disable(f)
  }

  fun configure(f: JsonParser.Feature, state: Boolean): JsonParser {
    return parser.configure(f, state)
  }

  fun isEnabled(f: JsonParser.Feature): Boolean {
    return parser.isEnabled(f)
  }

  fun enableFeature(f: JsonParser.Feature) {
    parser.enable(f)
  }

  fun disableFeature(f: JsonParser.Feature) {
    parser.disable(f)
  }

  fun isFeatureEnabled(f: JsonParser.Feature): Boolean {
    return parser.isEnabled(f)
  }

  @Throws(IOException::class, JsonParseException::class)
  fun nextToken(): JsonToken? {
    return parser.nextToken()
  }

  @Throws(IOException::class, JsonParseException::class)
  fun nextValue(): JsonToken {
    return parser.nextValue()
  }

  @Throws(IOException::class, JsonParseException::class)
  fun skipChildren(): JsonParser {
    return parser.skipChildren()
  }

  val isClosed: Boolean
    get() = parser.isClosed

  val currentToken: JsonToken
    get() = parser.currentToken

  fun hasCurrentToken(): Boolean {
    return parser.hasCurrentToken()
  }

  fun clearCurrentToken() {
    parser.clearCurrentToken()
  }

  val currentName: String
    @Throws(IOException::class, JsonParseException::class)
    get() = parser.currentName

  val parsingContext: JsonStreamContext
    get() = parser.parsingContext

  val tokenLocation: JsonLocation
    get() = parser.tokenLocation

  val currentLocation: JsonLocation
    get() = parser.currentLocation

  val lastClearedToken: JsonToken
    get() = parser.lastClearedToken

  val isExpectedStartArrayToken: Boolean
    get() = parser.isExpectedStartArrayToken

  val text: String
    @Throws(IOException::class, JsonParseException::class)
    get() = parser.text

  val textCharacters: CharArray
    @Throws(IOException::class, JsonParseException::class)
    get() = parser.textCharacters

  val textLength: Int
    @Throws(IOException::class, JsonParseException::class)
    get() = parser.textLength

  val textOffset: Int
    @Throws(IOException::class, JsonParseException::class)
    get() = parser.textOffset

  fun hasTextCharacters(): Boolean {
    return parser.hasTextCharacters()
  }

  val numberValue: Number
    @Throws(IOException::class, JsonParseException::class)
    get() = parser.numberValue

  val numberType: JsonParser.NumberType
    @Throws(IOException::class, JsonParseException::class)
    get() = parser.numberType

  val byteValue: Byte
    @Throws(IOException::class, JsonParseException::class)
    get() = parser.byteValue

  val shortValue: Short
    @Throws(IOException::class, JsonParseException::class)
    get() = parser.shortValue

  val intValue: Int
    @Throws(IOException::class, JsonParseException::class)
    get() = parser.intValue

  val longValue: Long
    @Throws(IOException::class, JsonParseException::class)
    get() = parser.longValue

  val bigIntegerValue: BigInteger
    @Throws(IOException::class, JsonParseException::class)
    get() = parser.bigIntegerValue

  val floatValue: Float
    @Throws(IOException::class, JsonParseException::class)
    get() = parser.floatValue

  val doubleValue: Double
    @Throws(IOException::class, JsonParseException::class)
    get() = parser.doubleValue

  val decimalValue: BigDecimal
    @Throws(IOException::class, JsonParseException::class)
    get() = parser.decimalValue

  val booleanValue: Boolean
    @Throws(IOException::class, JsonParseException::class)
    get() = parser.booleanValue

  val embeddedObject: Any
    @Throws(IOException::class, JsonParseException::class)
    get() = parser.embeddedObject

  @Throws(IOException::class, JsonParseException::class)
  fun getBinaryValue(b64variant: Base64Variant): ByteArray {
    return parser.getBinaryValue(b64variant)
  }

  val binaryValue: ByteArray
    @Throws(IOException::class, JsonParseException::class)
    get() = parser.binaryValue
}
