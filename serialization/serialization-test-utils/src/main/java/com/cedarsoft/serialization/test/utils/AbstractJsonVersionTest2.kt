/**
 * Copyright (C) cedarsoft GmbH.
 *
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *
 * http://www.cedarsoft.org/gpl3ce
 * (GPL 3 with Classpath Exception)
 *
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 *
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */
package com.cedarsoft.serialization.test.utils

import com.cedarsoft.serialization.Serializer
import com.cedarsoft.version.Version
import org.apache.commons.io.IOUtils
import org.fest.reflect.core.Reflection
import java.io.IOException
import java.net.URL
import java.nio.charset.StandardCharsets

/**
 * It is necessary to define at least one DataPoint
 * <pre>&#064;DataPoint<br></br>public static final VersionEntry ENTRY1 = create(<br></br> Version.valueOf( 1, 0, 0 ),<br></br> &quot;&lt;json/&gt;&quot; );</pre>
 *
 * @param <T> the type
</T> */
abstract class AbstractJsonVersionTest2<T : Any> : AbstractVersionTest2<T>() {
  class JsonVersionEntry(
    override val version: Version,
    private val json: ByteArray
  ) : VersionEntry {

    constructor(version: Version, json: String) : this(version, json.toByteArray(StandardCharsets.UTF_8)) {}

    @Throws(Exception::class)
    override fun getSerialized(serializer: Serializer<*, *, *>): ByteArray {
      val method = Reflection.method("isObjectType")
      val isObjectType = method.withReturnType(java.lang.Boolean.TYPE).`in`(serializer).invoke()
      return if (isObjectType) {
        AbstractJsonSerializerTest2.addTypeInformation(AbstractJsonSerializerTest2.getType(serializer), version, json)
          .toByteArray(StandardCharsets.UTF_8)
      } else json
    }

    override fun toString(): String {
      return "Entry for <$version>"
    }
  }

  companion object {
    @JvmStatic
    protected fun create(version: Version, json: String): VersionEntry {
      return JsonVersionEntry(version, json)
    }

    @JvmStatic
    protected fun create(version: Version, expected: URL): VersionEntry {
      return try {
        JsonVersionEntry(version, IOUtils.toByteArray(expected.openStream()))
      } catch (e: IOException) {
        throw RuntimeException(e)
      }
    }
  }
}
