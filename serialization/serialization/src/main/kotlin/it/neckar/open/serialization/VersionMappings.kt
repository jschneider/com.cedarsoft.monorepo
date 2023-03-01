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

import it.neckar.open.version.UnsupportedVersionException
import it.neckar.open.version.UnsupportedVersionRangeException
import it.neckar.open.version.Version
import it.neckar.open.version.VersionException
import it.neckar.open.version.VersionMismatchException
import it.neckar.open.version.VersionRange
import java.util.Collections
import java.util.SortedSet
import java.util.TreeSet

/**
 * Holds several VersionMappings.
 *
 * @param <T> the type of the key
</T> */
class VersionMappings<T : Any>
/**
 * Creates a new version mappings
 *
 * @param versionRange the version range for the source
 */(

  val versionRange: VersionRange
) {

  private val mappings: MutableMap<T, VersionMapping> = HashMap()

  /**
   * Returns all available mappings
   *
   * @return the mappings
   */

  fun getMappings(): Map<out T, VersionMapping> {
    return Collections.unmodifiableMap(mappings)
  }

  /**
   * Resolves the version
   *
   * @param key     the key
   * @param version the version
   * @return the mapped version
   */

  fun resolveVersion(key: T, version: Version): Version {
    return getMapping(key).resolveVersion(version)
  }

  /**
   * Returns the mapping for the given key
   *
   * @param key the key
   * @return the version mapping for the key
   */

  fun getMapping(key: T): VersionMapping {
    return mappings[key] ?: throw SerializationException(SerializationException.Details.NO_MAPPING_FOUND, key)
  }


  fun addMapping(key: T, targetVersionRange: VersionRange): VersionMapping {
    require(!mappings.containsKey(key)) { "An entry for the key <$key> has still been added" }
    val mapping = VersionMapping(versionRange, targetVersionRange)
    mappings[key] = mapping
    return mapping
  }

  /**
   * Returns the mapped versions
   *
   * @return a set with all mapped versions
   */

  val mappedVersions: SortedSet<Version>
    get() {
      val keyVersions: SortedSet<Version> = TreeSet()
      for (mapping in getMappings().values) {
        keyVersions.add(mapping.sourceVersionRange.min)
        keyVersions.add(mapping.sourceVersionRange.max)
        for (entry in mapping.getEntries()) {
          keyVersions.add(entry.versionRange.min)
          keyVersions.add(entry.versionRange.max)
        }
      }
      return keyVersions
    }


  fun add(key: T, targetVersionRange: VersionRange): VersionMapping {
    return addMapping(key, targetVersionRange)
  }

  fun verify(toString: ToString<T>): Boolean {
    return verify(toString::convert)
  }

  @JvmOverloads
  fun verify(toString: (T) -> String = { it.toString() }): Boolean {
    val mappedVersions = mappedVersions
    if (mappings.isEmpty()) {
      throw VersionException("No mappings available")
    }
    for ((key, mapping) in mappings) {

      //Check for every entry whether the version ranges fit
      if (mapping.sourceVersionRange != versionRange) {
        throw UnsupportedVersionRangeException(versionRange, mapping.sourceVersionRange, "Invalid mapping for <" + toString(key) + ">. ")
      }

      //Verify the mapping itself
      try {
        mapping.verify()
        mapping.verifyMappedVersions(mappedVersions)
      } catch (e: VersionMismatchException) {
        val newException: RuntimeException = VersionMismatchException(e.expected, e.actual, "Invalid mapping for <" + toString(key) + ">: " + e.message, false)
        newException.stackTrace = e.stackTrace
        throw newException
      } catch (e: UnsupportedVersionException) {
        val newException: RuntimeException = UnsupportedVersionException(e.actual, e.supportedRange, "Invalid mapping for <" + toString(key) + ">: " + e.message, false)
        newException.stackTrace = e.stackTrace
        throw newException
      }
    }
    return true
  }
}
