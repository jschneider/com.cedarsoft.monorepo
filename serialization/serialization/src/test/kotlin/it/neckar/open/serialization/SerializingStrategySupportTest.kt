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
import it.neckar.open.version.VersionRange
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyOrder
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.InputStream
import java.io.OutputStream

/**
 *
 */
class SerializingStrategySupportTest {
  private lateinit var support: SerializingStrategySupport<Int, StringBuilder, String, OutputStream, InputStream>

  @BeforeEach
  fun setUp() {
    support = SerializingStrategySupport(VersionRange.single(1, 0, 0))
  }

  @Test
  fun testIt() {
    assertEquals(0, support.getStrategies().size.toLong())
    try {
      support.findStrategy("daId")
      fail("Where is the Exception")
    } catch (e: NotFoundException) {
      Assertions.assertThat(e).hasMessage("No strategy found for id <daId>")
    }
  }

  @Test
  fun festFind() {
    assertEquals(0, support.getStrategies().size.toLong())
    try {
      support.findStrategy(77)
      fail("Where is the Exception")
    } catch (e: NotFoundException) {
      Assertions.assertThat(e).hasMessage("No strategy found for object <77>")
    }
  }

  @Test
  fun testVerify() {
    try {
      assertFalse(support.verify())
      fail("Where is the Exception")
    } catch (e: SerializationException) {
      Assertions.assertThat(e.message).contains("No strategies available. Verification not possible.")
    }
  }

  @Test
  fun testVersionMappings() {
    val strategy1: SerializingStrategy<Int, StringBuilder, String, OutputStream, InputStream> = mockk(relaxed = true) {
      every { formatVersionRange } returns VersionRange.single(0, 0, 1)
      every { id } returns "id1"
    }
    val strategy2: SerializingStrategy<Int, StringBuilder, String, OutputStream, InputStream> = mockk(relaxed = true) {
      every { formatVersionRange } returns VersionRange.single(0, 0, 2)
      every { id } returns "id2"
    }


    support.addStrategy(strategy1).map(1, 0, 0).toDelegateVersion(0, 0, 1)
    support.addStrategy(strategy2).map(1, 0, 0).toDelegateVersion(0, 0, 2)
    assertTrue(support.verify())
    assertEquals(1, support.versionMappings.mappedVersions.size.toLong())
    assertEquals(Version.valueOf(0, 0, 1), support.resolveVersion(strategy1, Version.valueOf(1, 0, 0)))
    assertEquals(Version.valueOf(0, 0, 2), support.resolveVersion(strategy2, Version.valueOf(1, 0, 0)))

    verifyOrder {
      strategy1.formatVersionRange
      strategy2.formatVersionRange
    }
  }

  @Test
  fun festFind3() {
    val strategy: SerializingStrategy<Int, StringBuilder, String, OutputStream, InputStream> = mockk(relaxed = true) {
      every { formatVersionRange } returns VersionRange.single(1, 0, 0)
      every { id } returns "daId"
      every { supports(77) } returns true
      every { supports(99) } returns false
    }

    assertNotNull(strategy)
    assertEquals(0, support.getStrategies().size.toLong())
    support.addStrategy(strategy)
    assertEquals(1, support.getStrategies().size.toLong())
    assertNotNull(support.findStrategy("daId"))
    try {
      support.findStrategy("otherId")
      fail("Where is the Exception")
    } catch (ignore: NotFoundException) {
    }
    assertNotNull(support.findStrategy(77))
    try {
      support.findStrategy(99)
      fail("Where is the Exception")
    } catch (ignore: NotFoundException) {
    }

    verifyOrder {
      strategy.formatVersionRange
      strategy.id
      strategy.id
      strategy.supports(77)
      strategy.supports(99)
    }
  }
}
