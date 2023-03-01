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
package it.neckar.open.serialization.jackson.filter

import it.neckar.open.serialization.jackson.JacksonParserWrapper
import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonToken
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

/**
 */
class FilteringJsonParserTest {
  @Test
  fun testListener() {
    val filter = Filter { parser -> parser.currentName.startsWith("_") }

    val listenerMock: FilteredParserListener = mockk<FilteredParserListener>(relaxed = true)

    val inputStream = javaClass.getResourceAsStream("filter.json")
    val parser = FilteringJsonParser(JsonFactory().createParser(inputStream), filter)

    val wrapper = JacksonParserWrapper(parser)
    parser.addListener(listenerMock)

    inputStream.use { _ ->
      wrapper.nextToken(JsonToken.START_OBJECT)
      wrapper.nextField("id")
      wrapper.nextToken(JsonToken.VALUE_STRING)
      wrapper.nextField("key")
      wrapper.nextToken(JsonToken.START_OBJECT)
      wrapper.nextField("description")
      wrapper.nextToken(JsonToken.VALUE_STRING)
      wrapper.nextToken(JsonToken.END_OBJECT)
      wrapper.nextField("value")
      wrapper.nextToken(JsonToken.START_OBJECT)
      wrapper.nextField("@version")
      wrapper.nextToken(JsonToken.VALUE_STRING)
      wrapper.nextField("aValue")
      wrapper.nextToken(JsonToken.VALUE_NUMBER_INT)
      wrapper.nextToken(JsonToken.END_OBJECT)
      wrapper.nextToken(JsonToken.END_OBJECT)
      wrapper.ensureObjectClosed()
    }

    verify {
      listenerMock.skippingField(parser, "_filter1")
      listenerMock.skippingFieldValue(parser, "_filter1")
      listenerMock.skippingField(parser, "_filter2")
      listenerMock.skippingFieldValue(parser, "_filter2")
      listenerMock.skippingField(parser, "_filter3")
      listenerMock.skippingFieldValue(parser, "_filter3")
      listenerMock.skippingField(parser, "_filter4")
      listenerMock.skippingFieldValue(parser, "_filter4")
      listenerMock.skippingField(parser, "_filter5")
      listenerMock.skippingFieldValue(parser, "_filter5")
      listenerMock.skippingField(parser, "_filter6")
      listenerMock.skippingFieldValue(parser, "_filter6")
    }
    confirmVerified(listenerMock)
  }

  @Test
  fun testIt() {
    val jsonFactory = JsonFactory()
    val `in` = javaClass.getResourceAsStream("filter.json")
    try {
      val parser = FilteringJsonParser(jsonFactory.createParser(`in`)) { parser -> parser.currentName.startsWith("_") }
      val wrapper = JacksonParserWrapper(parser)
      wrapper.nextToken(JsonToken.START_OBJECT)
      wrapper.nextField("id")
      wrapper.nextToken(JsonToken.VALUE_STRING)
      wrapper.nextField("key")
      wrapper.nextToken(JsonToken.START_OBJECT)
      wrapper.nextField("description")
      wrapper.nextToken(JsonToken.VALUE_STRING)
      wrapper.nextToken(JsonToken.END_OBJECT)
      wrapper.nextField("value")
      wrapper.nextToken(JsonToken.START_OBJECT)
      wrapper.nextField("@version")
      wrapper.nextToken(JsonToken.VALUE_STRING)
      wrapper.nextField("aValue")
      wrapper.nextToken(JsonToken.VALUE_NUMBER_INT)
      wrapper.nextToken(JsonToken.END_OBJECT)
      wrapper.nextToken(JsonToken.END_OBJECT)
      wrapper.ensureObjectClosed()
    } finally {
      `in`.close()
    }
  }
}
