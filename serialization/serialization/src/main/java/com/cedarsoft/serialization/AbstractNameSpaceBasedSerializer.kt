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

package com.cedarsoft.serialization

import com.cedarsoft.version.Version
import com.cedarsoft.version.VersionException
import com.cedarsoft.version.VersionRange

/**
 * @param <T> the type of object this serializer is able to (de)serialize
 * @param <S> the object to serialize to
 * @param <D> the object to deserialize from
 */
abstract class AbstractNameSpaceBasedSerializer<T : Any, S : Any, D : Any> protected constructor(
  /**
   * Returns the name space uri without the form at version
   * @return the name space uri base
   */
  val nameSpaceBase: String, formatVersionRange: VersionRange
) : AbstractStreamSerializer<T, S, D>(formatVersionRange) {

  /**
   * Returns the name space uri (including the version)
   * @return the name space uri
   */
  val nameSpace: String
    get() = createNameSpace(formatVersion)

  /**
   * Creates the namespace uri including the format version
   * @param formatVersion the format version
   * @return the namespace uri
   */
  fun createNameSpace(formatVersion: Version): String {
    return nameSpaceBase + "/" + formatVersion.format()
  }

  /**
   * Verifies the namespace uri
   * throws com.cedarsoft.version.VersionException          the if the version does not fit the expected range
   * @param namespace the namespace uri
   * @throws SerializationException if the namespace is invalid
   */
  @Throws(SerializationException::class, VersionException::class)
  open fun verifyNamespace(namespace: String?) {
    if (namespace == null || namespace.trim { it <= ' ' }.isEmpty()) {
      throw VersionException("No version information available")
    }
    val expectedBase = nameSpaceBase
    if (!namespace.startsWith(expectedBase)) {
      throw SerializationException(SerializationException.Details.INVALID_NAME_SPACE, "$expectedBase/$formatVersion", namespace)
    }
  }

  /**
   * Parses the version from the namespace and verifies the namespace and version
   * @param namespaceURI the namespace uri
   * @return the parsed and verified version
   * @throws SerializationException if the namespace is invalid
   */
  @Throws(SerializationException::class, VersionException::class)
  open fun parseAndVerifyNameSpace(namespaceURI: String?): Version {
    //Verify the name space
    verifyNamespace(namespaceURI)

    //Parse and verify the version
    val formatVersion = parseVersionFromNamespace(namespaceURI)
    verifyVersionReadable(formatVersion)
    return formatVersion
  }

  companion object {
    /**
     * Parses the version from a namespace uri.
     * throws com.cedarsoft.version.VersionException if the namespace uri does not contain any version information   *
     * @param namespaceURI the namespace uri (the version has to be the last part split by "/"
     * @return the parsed version
     */
    @JvmStatic
    @Throws(IllegalArgumentException::class, VersionException::class)
    fun parseVersionFromNamespace(namespaceURI: String?): Version {
      if (namespaceURI.isNullOrEmpty()) {
        throw VersionException("No version information found")
      }

      val index = namespaceURI.lastIndexOf('/')
      val versionString = namespaceURI.substring(index + 1)
      return Version.parse(versionString)
    }
  }
}
