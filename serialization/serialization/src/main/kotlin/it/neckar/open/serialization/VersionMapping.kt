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

import it.neckar.open.serialization.VersionMapping
import it.neckar.open.version.UnsupportedVersionException
import it.neckar.open.version.UnsupportedVersionRangeException
import it.neckar.open.version.Version
import it.neckar.open.version.Version.Companion.valueOf
import it.neckar.open.version.VersionException
import it.neckar.open.version.VersionMismatchException
import it.neckar.open.version.VersionRange
import it.neckar.open.version.VersionRange.Companion.from
import java.util.ArrayList
import java.util.Collections

/**
 * Contains the mapping for versions.
 *
 *
 * This class offers a mapping for every version from the source version range
 * to a version of the delegate version range.
 */
class VersionMapping(
  /**
   * Represents the version range of the delegating object (the source).
   * The complete range has to be mapped to one or more versions of the delegate.
   */
  val sourceVersionRange: VersionRange,
  /**
   * The supported version range of the delegate.
   */
  val delegateVersionRange: VersionRange
) {

  private val entries: MutableList<Entry> = ArrayList()

  fun getEntries(): Collection<Entry> {
    return Collections.unmodifiableCollection(entries)
  }


  fun map(range: VersionRange): FluentFactory {
    return FluentFactory(range, true)
  }


  fun map(version: Version): FluentFactory {
    return FluentFactory(from(version).single())
  }


  fun map(major: Int, minor: Int, build: Int): FluentFactory {
    return map(valueOf(major, minor, build))
  }

  /**
   * @param sourceRange     the source version range
   * @param delegateVersion the delegate version
   */
  fun addMapping(sourceRange: VersionRange, delegateVersion: Version) {
    if (!sourceVersionRange.containsCompletely(sourceRange)) {
      throw UnsupportedVersionRangeException(sourceRange, sourceVersionRange, "Invalid source range: ")
    }
    if (!delegateVersionRange.contains(delegateVersion)) {
      throw UnsupportedVersionException(delegateVersion, delegateVersionRange, "Invalid delegate version: ")
    }

    //Exists still a mapping?
    if (containsMappingIn(sourceRange)) {
      throw UnsupportedVersionRangeException(sourceRange, null, "The version range has still been mapped: ")
    }
    entries.add(Entry(sourceRange, delegateVersion))
  }

  private fun containsMappingIn(range: VersionRange): Boolean {
    for (entry in entries) {
      if (entry.versionRange.overlaps(range)) {
        return true
      }
    }
    return false
  }


  fun resolveVersion(version: Version): Version {
    for (entry in entries) {
      if (entry.versionRange.contains(version)) {
        return entry.delegateVersion
      }
    }
    throw UnsupportedVersionException(version, null, "No delegate version mapped for source version <$version>", false)
  }

  /**
   * Verifies the mapping
   */
  @Throws(VersionException::class)
  fun verify() {
    if (entries.isEmpty()) {
      throw VersionException("No mappings available")
    }

    //Check whether the minimum equals the expected version range minimum
    run {
      val currentMin = entries[0].versionRange.min
      if (currentMin != sourceVersionRange.min) {
        throw VersionMismatchException(sourceVersionRange.min, currentMin, "Lower border of source range not mapped: ")
      }
    }

    //Verify the last entry. Does the max version range fit?
    run {
      val last = entries[entries.size - 1]
      val currentMax = last.versionRange.max
      if (currentMax != sourceVersionRange.max) {
        throw VersionMismatchException(sourceVersionRange.max, currentMax, "Upper border of source range not mapped: ")
      }
    }
  }


  val delegateWriteVersion: Version
    get() {
      if (entries.isEmpty()) {
        throw SerializationException(SerializationException.Details.INVALID_STATE, "Contains no entries for delegate write version.")
      }
      return entries[entries.size - 1].delegateVersion
    }

  @Throws(UnsupportedVersionException::class)
  fun verifyMappedVersions(mappedVersions: Iterable<Version>) {
    for (mappedVersion in mappedVersions) {
      resolveVersion(mappedVersion)
    }
  }

  override fun toString(): String {
    return "VersionMapping{" +
      "from " + sourceVersionRange +
      " to " + delegateVersionRange +
      ": " + entries +
      '}'
  }

  class Entry internal constructor(
    val versionRange: VersionRange,
    val delegateVersion: Version
  )

  inner class FluentFactory @JvmOverloads constructor(private val range: VersionRange, private val toCalled: Boolean = false) {

    fun toDelegateVersion(major: Int, minor: Int, build: Int): VersionMapping {
      return toDelegateVersion(valueOf(major, minor, build))
    }


    fun toDelegateVersion(version: Version): VersionMapping {
      addMapping(range, version)
      return this@VersionMapping
    }


    fun to(major: Int, minor: Int, build: Int): FluentFactory {
      //check if we have still set a to version. Then it is probably a user fault
      if (toCalled) {
        throw SerializationException(SerializationException.Details.INVALID_STATE, "Duplicate call to <to>. Did you mean <toDelegateVersion> instead?")
      }
      return FluentFactory(from(range.min).to(major, minor, build), true)
    }
  }
}
