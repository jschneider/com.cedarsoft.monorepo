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

import assertk.*
import assertk.assertions.*
import it.neckar.open.version.UnsupportedVersionException
import it.neckar.open.version.UnsupportedVersionRangeException
import it.neckar.open.version.Version.Companion.valueOf
import it.neckar.open.version.VersionException
import it.neckar.open.version.VersionRange.Companion.from
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 *
 */
class VersionMappingsTest {
  private val mine = from(1, 0, 0).to(2, 0, 0)
  private var mapping: VersionMappings<Class<*>>? = null

  @BeforeEach
  fun setup() {
    mapping = VersionMappings(mine)
  }

  @Test
  fun testValidate() {
    mapping!!.add(String::class.java, from(7, 0, 0).to(8, 0, 0))
      .map(1, 0, 0).toDelegateVersion(7, 0, 1)
      .map(1, 6, 0).to(1, 9, 0).toDelegateVersion(8, 0, 0)
    try {
      mapping!!.verify()
      Assert.fail("Where is the Exception")
    } catch (e: VersionException) {
      assertThat(e).hasMessage("Invalid mapping for <class java.lang.String>: Upper border of source range not mapped: Expected [2.0.0] but was [1.9.0]")
    }
  }

  @Test
  fun testValidate2() {
    mapping!!.add(String::class.java, from(7, 0, 0).to(8, 0, 0))
      .map(1, 0, 1).toDelegateVersion(7, 0, 1)
      .map(1, 6, 0).to(2, 0, 0).toDelegateVersion(8, 0, 0)
    try {
      mapping!!.verify()
      Assert.fail("Where is the Exception")
    } catch (e: VersionException) {
      assertThat(e).hasMessage("Invalid mapping for <class java.lang.String>: Lower border of source range not mapped: Expected [1.0.0] but was [1.0.1]")
    }
  }

  @Test
  fun testBasicMapping() {
    mapping!!.add(String::class.java, from(7, 0, 0).to(8, 0, 0))
      .map(1, 0, 0).toDelegateVersion(7, 0, 1)
      .map(1, 0, 1).toDelegateVersion(7, 0, 2)
      .map(1, 0, 2).to(1, 5, 0).toDelegateVersion(7, 1, 0)
      .map(1, 6, 0).to(2, 0, 0).toDelegateVersion(8, 0, 0)
    Assert.assertThat(mapping!!.resolveVersion(String::class.java, valueOf(1, 0, 0)), CoreMatchers.`is`(valueOf(7, 0, 1)))
    Assert.assertThat(mapping!!.resolveVersion(String::class.java, valueOf(1, 0, 1)), CoreMatchers.`is`(valueOf(7, 0, 2)))
    Assert.assertThat(mapping!!.resolveVersion(String::class.java, valueOf(1, 0, 2)), CoreMatchers.`is`(valueOf(7, 1, 0)))
    Assert.assertThat(mapping!!.resolveVersion(String::class.java, valueOf(1, 0, 3)), CoreMatchers.`is`(valueOf(7, 1, 0)))
    Assert.assertThat(mapping!!.resolveVersion(String::class.java, valueOf(1, 0, 4)), CoreMatchers.`is`(valueOf(7, 1, 0)))
    Assert.assertThat(mapping!!.resolveVersion(String::class.java, valueOf(1, 5, 0)), CoreMatchers.`is`(valueOf(7, 1, 0)))
    Assert.assertThat(mapping!!.resolveVersion(String::class.java, valueOf(1, 6, 0)), CoreMatchers.`is`(valueOf(8, 0, 0)))
    Assert.assertThat(mapping!!.resolveVersion(String::class.java, valueOf(1, 6, 1)), CoreMatchers.`is`(valueOf(8, 0, 0)))
    Assert.assertThat(mapping!!.resolveVersion(String::class.java, valueOf(1, 99, 99)), CoreMatchers.`is`(valueOf(8, 0, 0)))
    Assert.assertThat(mapping!!.resolveVersion(String::class.java, valueOf(2, 0, 0)), CoreMatchers.`is`(valueOf(8, 0, 0)))
    try {
      Assert.assertThat(mapping!!.resolveVersion(String::class.java, valueOf(1, 5, 1)), CoreMatchers.`is`(valueOf(7, 1, 0)))
      Assert.fail("Where is the Exception")
    } catch (e: UnsupportedVersionException) {
      assertThat(e).hasMessage("No delegate version mapped for source version <1.5.1>")
    }
  }

  @Test
  fun testDuplicate() {
    try {
      mapping!!.add(String::class.java, from(7, 0, 0).to(8, 0, 0))
        .map(1, 0, 0).toDelegateVersion(7, 0, 1)
        .map(1, 0, 0).toDelegateVersion(7, 0, 2)
      Assert.fail("Where is the Exception")
    } catch (e: VersionException) {
      assertThat(e).hasMessage("The version range has still been mapped: Was <[1.0.0-1.0.0]>")
    }
  }

  @Test
  fun testDuplicate2() {
    try {
      mapping!!.add(String::class.java, from(7, 0, 0).to(8, 0, 0))
        .map(1, 0, 0).toDelegateVersion(7, 0, 1)
        .map(1, 0, 0).to(2, 0, 0).toDelegateVersion(7, 0, 2)
      Assert.fail("Where is the Exception")
    } catch (e: UnsupportedVersionRangeException) {
      assertThat(e).hasMessage("The version range has still been mapped: Was <[1.0.0-2.0.0]>")
    }
  }

  @Test
  fun testDuplicate3() {
    try {
      mapping!!.add(String::class.java, from(7, 0, 0).to(8, 0, 0))
        .map(1, 0, 1).toDelegateVersion(7, 0, 1)
        .map(1, 0, 0).to(2, 0, 0).toDelegateVersion(7, 0, 2)
      Assert.fail("Where is the Exception")
    } catch (e: UnsupportedVersionRangeException) {
      assertThat(e).hasMessage("The version range has still been mapped: Was <[1.0.0-2.0.0]>")
    }
  }

  @Test
  fun testDuplicate4() {
    try {
      mapping!!.add(String::class.java, from(7, 0, 0).to(8, 0, 0))
        .map(2, 0, 0).toDelegateVersion(7, 0, 1)
        .map(1, 0, 0).to(2, 0, 0).toDelegateVersion(7, 0, 2)
      Assert.fail("Where is the Exception")
    } catch (e: UnsupportedVersionRangeException) {
      assertThat(e).hasMessage("The version range has still been mapped: Was <[1.0.0-2.0.0]>")
    }
  }

  @Test
  fun testDuplicate1() {
    mapping!!.addMapping(String::class.java, from(7, 0, 0).to())
    try {
      mapping!!.addMapping(String::class.java, from(7, 0, 0).to())
      Assert.fail("Where is the Exception")
    } catch (ignore: IllegalArgumentException) {
    }
  }
}
