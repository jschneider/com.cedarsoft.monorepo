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
package it.neckar.open.serialization.ui

import it.neckar.open.serialization.VersionMappings
import it.neckar.open.version.VersionRange
import it.neckar.open.version.VersionRange.Companion.from
import org.junit.Assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.IOException

/**
 *
 */
class VersionMappingsVisualizerTest {
  private lateinit var mine: VersionRange
  private lateinit var versionMappings: VersionMappings<Class<*>>
  private lateinit var other: VersionRange

  @BeforeEach
  @Throws(Exception::class)
  fun setUp() {
    mine = from(1, 0, 0).to(2, 0, 0)
    versionMappings = VersionMappings(mine)
    other = from(7, 0, 0).to(7, 5, 9)
  }

  @Test
  @Throws(IOException::class)
  fun testIt() {
    versionMappings.add(Any::class.java, other)
      .map(1, 0, 0).toDelegateVersion(7, 0, 1)
      .map(1, 0, 1).toDelegateVersion(7, 0, 2)
      .map(1, 0, 2).to(1, 15, 0).toDelegateVersion(7, 1, 0)
      .map(2, 0, 0).to(2, 0, 0).toDelegateVersion(7, 5, 9)
    versionMappings.add(String::class.java, other)
      .map(1, 0, 0).toDelegateVersion(7, 1, 1)
      .map(1, 0, 1).toDelegateVersion(7, 0, 2)
      .map(1, 0, 2).to(1, 15, 0).toDelegateVersion(7, 1, 0)
      .map(2, 0, 0).to(2, 0, 0).toDelegateVersion(7, 5, 9)
    versionMappings.add(Int::class.java, other)
      .map(1, 0, 0).toDelegateVersion(7, 1, 1)
      .map(1, 0, 1).toDelegateVersion(7, 1, 12)
      .map(1, 0, 2).to(1, 15, 0).toDelegateVersion(7, 0, 91)
      .map(2, 0, 0).to(2, 0, 0).toDelegateVersion(7, 5, 9)
    Assert.assertEquals(
      """|         -->       int    Object    String
         |------------------------------------------
         |   1.0.0 -->     7.1.1     7.0.1     7.1.1
         |   1.0.1 -->    7.1.12     7.0.2     7.0.2
         |   1.0.2 -->    7.0.91     7.1.0     7.1.0
         |  1.15.0 -->         |         |         |
         |   2.0.0 -->     7.5.9     7.5.9     7.5.9
         |------------------------------------------
         |""".trimMargin(),
      VersionMappingsVisualizer(versionMappings, { o1, o2 -> o1.name.compareTo(o2.name) }, {
        val parts = it.name.split(".").toTypedArray()
        parts[parts.size - 1]
      }
      ).visualize())
  }
}
