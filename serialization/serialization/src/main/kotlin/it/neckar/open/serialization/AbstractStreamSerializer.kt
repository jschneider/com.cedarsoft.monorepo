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

package it.neckar.open.serialization

import it.neckar.open.version.VersionRange
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

/**
 * Abstract base class for all kinds of serializers.
 * @param <T> the type of object this serializer is able to (de)serialize
 * @param <S> the object to serialize to
 * @param <D> the object to deserialize from
 */
abstract class AbstractStreamSerializer<T : Any, S : Any, D : Any>
/**
 * Creates a serializer.
 * @param formatVersionRange the version range. The max value is used as format version when written.
 */
protected constructor(formatVersionRange: VersionRange) : AbstractSerializer<T, S, D, OutputStream, InputStream>(formatVersionRange), StreamSerializer<T> {

  /**
   * Helper method that serializes to a byte array
   * @param objectToSerialize the object
   * @return the serialized object
   * @throws IOException if there is an io problem
   */
  @Throws(IOException::class)
  fun serializeToByteArray(objectToSerialize: T): ByteArray {
    val out = ByteArrayOutputStream()
    serialize(objectToSerialize, out)
    return out.toByteArray()
  }
}
