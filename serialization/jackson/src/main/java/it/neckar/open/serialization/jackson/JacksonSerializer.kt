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

import it.neckar.open.serialization.PluggableSerializer
import it.neckar.open.serialization.SerializationException
import it.neckar.open.serialization.StreamSerializer
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import java.io.InputStream
import java.io.OutputStream

/**
 * @param <T> the type of object this serializer is able to (de)serialize
</T> */
interface JacksonSerializer<T : Any> : PluggableSerializer<T, JsonGenerator, JsonParser, OutputStream, InputStream>, StreamSerializer<T> {
  /**
   * Returns the type
   * @return the type
   */
  val type: String

  /**
   * Whether it is an object type. If true, the object braces are generated where necessary.
   * @return whether it is an object type
   */
  val isObjectType: Boolean

  /**
   * Serializes the object to the given generator.
   * The serializer is responsible for writing start/close object/array brackets if necessary.
   * This method also writes the @type property.
   * @param objectToSerialize    the object that is serialized
   * @param generator the generator
   * @throws java.io.IOException if there is an io problem
   */
  @Throws(Exception::class)
  fun serialize(objectToSerialize: T, generator: JsonGenerator)

  /**
   * Deserializes the object from the given parser.
   * This method deserializes the @type property.
   * @param parser the parser
   * @return the deserialized object
   * @throws java.io.IOException if there is an io problem
   */
  @Throws(Exception::class)
  fun deserialize(parser: JsonParser): T

  /**
   * Verifies the name space
   * @param type the type
   */
  @Throws(SerializationException::class)
  fun verifyType(type: String)
}
