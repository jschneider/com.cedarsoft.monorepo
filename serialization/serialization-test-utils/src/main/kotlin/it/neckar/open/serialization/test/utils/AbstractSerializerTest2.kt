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
package it.neckar.open.serialization.test.utils

import it.neckar.open.serialization.Serializer
import it.neckar.open.test.utils.ByTypeSource
import org.apache.commons.io.IOUtils
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Assert
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.URL
import java.util.stream.Stream

/**
 * Abstract base class for serializer tests.
 *
 * @param <T> the type of domain object
</T> */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class AbstractSerializerTest2<T : Any> protected constructor(
  private val reflectionEqualsCheckEnabled: Boolean = false
) {
  @ParameterizedTest
  @MethodSource("provideData")
  @ByTypeSource(type = Entry::class)
  @Throws(Exception::class)
  fun testSerializer(entry: Entry<*>) {
    val serializer = getSerializer()

    //Serialize
    val objectToSerialize = entry.objectToSerialize as T
    val serialized = serialize(serializer, objectToSerialize)

    //Verify
    val entryCast = entry as Entry<T>
    verifySerialized(entryCast, serialized)
    verifyDeserialized(serializer.deserialize(ByteArrayInputStream(serialized)), objectToSerialize)
  }


  @Throws(Exception::class)
  protected fun serialize(serializer: Serializer<T, OutputStream, InputStream>, objectToSerialize: T): ByteArray {
    val out = ByteArrayOutputStream()
    serializer.serialize(objectToSerialize, out)
    return out.toByteArray()
  }

  /**
   * Provides some test data.
   * May be overridden by subclasses
   */
  protected fun provideData(): Stream<out Entry<out T>> {
    return Stream.empty()
  }

  @Throws(Exception::class)
  protected abstract fun verifySerialized(entry: Entry<T>, serialized: ByteArray)

  /**
   * Returns the serializer
   *
   * @return the serializer
   */
  @Throws(Exception::class)
  protected abstract fun getSerializer(): Serializer<T, OutputStream, InputStream>

  /**
   * Verifies the deserialized object
   *
   * @param deserialized the deserialized object
   * @param original     the original
   */
  protected open fun verifyDeserialized(deserialized: T, original: T) {
    Assert.assertEquals(original, deserialized)
    if (reflectionEqualsCheckEnabled) {
      MatcherAssert.assertThat(deserialized, CoreMatchers.`is`(ReflectionEquals(original)))
    }
  }

  companion object {
    @JvmStatic
    fun <T> create(objectToSerialize: T, expected: ByteArray): Entry<out T> {
      return Entry(objectToSerialize, expected)
    }

    @JvmStatic
    fun <T> create(objectToSerialize: T, expected: URL): Entry<out T> {
      return try {
        Entry(objectToSerialize, IOUtils.toByteArray(expected.openStream()))
      } catch (e: IOException) {
        throw RuntimeException(e)
      }
    }

    @JvmStatic
    fun <T> create(objectToSerialize: T, expected: InputStream): Entry<out T> {
      return try {
        Entry(objectToSerialize, IOUtils.toByteArray(expected))
      } catch (e: IOException) {
        throw RuntimeException(e)
      }
    }
  }
}
