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
package com.cedarsoft.serialization.test.utils

import com.cedarsoft.serialization.Serializer
import com.cedarsoft.test.utils.ByTypeSource
import com.cedarsoft.version.Version
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.stream.Stream

/**
 * Abstract test class for testing the support for multiple format versions
 * Attention: it is necessary to define at least one DataPoint:
 * <pre>&#064;DataPoint<br></br>public static final Entry&lt;?&gt; entry1 = create(<br></br> Version.valueOf( 1, 0, 0 ),<br></br> serializedAsByteArray; );</pre>
 *
 * @param <T> the type that is deserialized
</T> */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class AbstractVersionTest2<T : Any> {
  /**
   * This method checks old serialized objects
   *
   * @throws Exception if there is any error
   */
  @ParameterizedTest
  @MethodSource("provideData")
  @ByTypeSource(type = VersionEntry::class)
  @Throws(Exception::class)
  fun testVersion(entry: VersionEntry) {
    val serializer = serializer
    val version = entry.version
    val serialized = entry.getSerialized(serializer)
    val deserialized = serializer.deserialize(ByteArrayInputStream(serialized))
    verifyDeserialized(deserialized, version)
  }

  /**
   * Provides some test data.
   * May be overridden by sub classes
   */

  protected fun provideData(): Stream<out VersionEntry> {
    return Stream.empty()
  }

  /**
   * Returns the serializer
   *
   * @return the serializer
   * @throws Exception if there is any error
   */
  @get:Throws(Exception::class)
  protected abstract val serializer: Serializer<T, OutputStream, InputStream>

  /**
   * Verifies the deserialized object.
   *
   * @param deserialized the deserialized object
   * @param version      the version
   */
  @Throws(Exception::class)
  protected abstract fun verifyDeserialized(deserialized: T, version: Version?)
}
