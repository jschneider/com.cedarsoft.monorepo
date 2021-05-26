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
package com.cedarsoft.serialization

import com.cedarsoft.version.Version
import com.cedarsoft.version.VersionMismatchException
import com.cedarsoft.version.VersionRange
import java.io.IOException
import java.util.SortedSet
import javax.annotation.Nonnull

/**
 * Contains several delegates mappings
 *
 * @param <S> the object to serialize to (e.g. a dom element or stream)
 * @param <D> the object to deserialize from ((e.g. a dom element or stream)
</D></S> */
class DelegatesMappings<S : Any, D : Any, O : Any, I : Any>(versionRange: VersionRange) {
  private val serializers: MutableMap<Class<*>, Serializer<*, *, *>> = HashMap()

  val versionMappings: VersionMappings<Class<*>> = VersionMappings(versionRange)

  fun <T : Any> add(serializer: PluggableSerializer<in T, S, D, O, I>): FluentFactory<T> {
    return FluentFactory<T>(serializer)
  }

  @Throws(IOException::class)
  fun <T : Any> serialize(objectSerialize: T, type: Class<T>, outputElement: S, formatVersion: Version) {
    val serializer = getSerializer(type)
    serializer.serialize(outputElement, objectSerialize, versionMappings.resolveVersion(type, formatVersion))
  }


  @Throws(SerializationException::class)
  fun <T : Any> getSerializer(type: Class<T>): PluggableSerializer<in T, S, D, O, I> {
    @Suppress("UNCHECKED_CAST")
    return serializers[type] as PluggableSerializer<in T, S, D, O, I>? ?: throw SerializationException(SerializationException.Details.NO_SERIALIZER_FOUND, type.name)
  }


  @Throws(IOException::class)
  fun <T : Any> deserialize(type: Class<T>, formatVersion: Version, deserializeFrom: D): T {
    val serializer = getSerializer(type)
    val deserialized = serializer.deserialize(deserializeFrom, versionMappings.resolveVersion(type, formatVersion))
    return deserialized as T
  }

  /**
   * Verifies the mappings
   *
   * @return true if the verification has been successful. Throws an exception if not
   */
  fun verify(): Boolean {
    versionMappings.verify { toVerify -> toVerify.name }

    for ((key, mapping) in versionMappings.getMappings()) {
      //Check the write version
      val serializer = getSerializer(key)
      if (serializer.formatVersion != mapping.delegateWriteVersion) {
        throw VersionMismatchException(serializer.formatVersion, mapping.delegateWriteVersion, "Invalid serialization/output version for <" + key.name + ">. ")
      }
    }
    return true
  }

  @get:Nonnull
  val mappings: Map<out Class<*>, VersionMapping>
    get() = versionMappings.getMappings()


  fun <T : Any> resolveVersion(key: Class<out T>, version: Version): Version {
    return versionMappings.resolveVersion(key, version)
  }

  fun getMapping(key: Class<*>): VersionMapping {
    return versionMappings.getMapping(key)
  }

  val mappedVersions: SortedSet<Version>
    get() = versionMappings.mappedVersions

  val versionRange: VersionRange
    get() = versionMappings.versionRange

  /**
   * Represents a serializer that does not yet contain a version mapping
   */
  inner class FluentFactory<T : Any>(private val serializer: PluggableSerializer<in T, S, D, O, I>) {
    /**
     * Returns the mapping that is responsible to serialize the given type
     */
    fun responsibleFor(key: Class<out T>): VersionMapping {
      val targetVersionRange = serializer.formatVersionRange
      val mapping = versionMappings.addMapping(key, targetVersionRange)
      serializers[key] = serializer
      return mapping
    }
  }

}
