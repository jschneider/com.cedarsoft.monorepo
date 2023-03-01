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
package it.neckar.open.serialization

import it.neckar.open.version.Version
import it.neckar.open.version.VersionMismatchException
import it.neckar.open.version.VersionRange
import java.io.IOException

/**
 * Abstract base class for all kinds of serializers.
 *
 * @param <T> the type of object this serializer is able to (de)serialize
 * @param <S> the object to serialize to
 * @param <D> the object to deserialize from
 */
abstract class AbstractSerializer<T : Any, S : Any, D : Any, O : Any, I : Any> protected constructor(
  /**
   * The format version range for this serializer
   */
  final override val formatVersionRange: VersionRange
) : PluggableSerializer<T, S, D, O, I> {

  val delegatesMappings: DelegatesMappings<S, D, O, I> = DelegatesMappings(formatVersionRange)

  override val formatVersion: Version
    get() = formatVersionRange.max

  /**
   * Verifies the format version is supported
   *
   * @param formatVersion the format version
   */
  fun verifyVersionReadable(formatVersion: Version) {
    if (!isVersionReadable(formatVersion)) {
      throw VersionMismatchException(formatVersionRange, formatVersion)
    }
  }

  fun isVersionReadable(formatVersion: Version): Boolean {
    return formatVersionRange.contains(formatVersion)
  }

  /**
   * Verifies whether the format version is writable
   *
   * @param formatVersion the format version
   */
  fun verifyVersionWritable(formatVersion: Version) {
    if (!isVersionWritable(formatVersion)) {
      throw VersionMismatchException(this.formatVersion, formatVersion)
    }
  }

  fun isVersionWritable(formatVersion: Version?): Boolean {
    return this.formatVersion == formatVersion
  }


  fun <DT : Any> add(pluggableSerializer: PluggableSerializer<in DT, S, D, O, I>): DelegatesMappings<S, D, O, I>.FluentFactory<DT> {
    return delegatesMappings.add(pluggableSerializer)
  }

  @Throws(IOException::class)
  fun <DT : Any> serialize(toSerialize: DT, type: Class<DT>, deserializeTo: S, formatVersion: Version) {
    delegatesMappings.serialize(toSerialize, type, deserializeTo, formatVersion)
  }


  open fun <DT : Any> getSerializer(type: Class<DT>): PluggableSerializer<in DT, S, D, O, I> {
    return delegatesMappings.getSerializer(type)
  }


  @Throws(IOException::class)
  fun <DT : Any> deserialize(type: Class<DT>, formatVersion: Version, deserializeFrom: D): DT {
    return delegatesMappings.deserialize(type, formatVersion, deserializeFrom)
  }

  companion object {
    /**
     * Helper method that can be used to ensure the right format version for each delegate.
     *
     * @param delegate              the delegate
     * @param expectedFormatVersion the expected format version
     */
    fun verifyDelegatingSerializerVersion(delegate: Serializer<*, *, *>, expectedFormatVersion: Version) {
      val actualVersion = delegate.formatVersion
      if (actualVersion != expectedFormatVersion) {
        throw SerializationException(SerializationException.Details.INVALID_VERSION, expectedFormatVersion, actualVersion)
      }
    }
  }

}
