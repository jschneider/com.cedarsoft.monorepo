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
package it.neckar.open.serialization.stax.mate.test

import it.neckar.open.serialization.test.utils.AbstractXmlSerializerTest2
import it.neckar.open.serialization.test.utils.Entry
import it.neckar.open.serialization.ui.VersionMappingsVisualizer
import it.neckar.open.version.Version.Companion.valueOf
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import javax.annotation.Nonnull

/**
 *
 */
class BallSerializerTest : AbstractXmlSerializerTest2<Ball>() {
  @Nonnull
  @Throws(Exception::class)
  override fun getSerializer(): BallSerializer {
    return BallSerializer()
  }

  @Test
  @Throws(Exception::class)
  fun testAsccii() {
    Assertions.assertEquals(2, getSerializer().serializingStrategySupport.versionMappings.getMappings().size)

    Assertions.assertEquals(
      """
        |         -->  basketBa  tennisBa
        |--------------------------------
        |   1.0.0 -->     2.0.0     1.5.0
        |   1.1.0 -->     2.0.1     1.5.1
        |--------------------------------
        |
        """.trimMargin(),
      VersionMappingsVisualizer.toString(
        getSerializer().serializingStrategySupport.versionMappings
      ) {
        it.id
      }
    )
  }

  @Test
  @Throws(Exception::class)
  fun testVersion() {
    val serializer = getSerializer()
    Assertions.assertTrue(serializer.isVersionReadable(valueOf(1, 0, 0)))
    Assertions.assertFalse(serializer.isVersionReadable(valueOf(1, 2, 1)))
    Assertions.assertFalse(serializer.isVersionReadable(valueOf(0, 9, 9)))
    Assertions.assertTrue(serializer.isVersionWritable(valueOf(1, 1, 0)))
    Assertions.assertFalse(serializer.isVersionWritable(valueOf(1, 1, 1)))
    Assertions.assertFalse(serializer.isVersionWritable(valueOf(1, 0, 9)))
  }

  companion object {
    @JvmField
    val ENTRY1: Entry<out Ball> = create(
      Ball.TennisBall(7), "<ball type=\"tennisBall\" id=\"7\" />"
    )

    @JvmField
    val ENTRY2: Entry<out Ball> = create(
      Ball.BasketBall("asdf"), "<ball type=\"basketBall\" theId=\"asdf\"></ball>"
    )
  }
}
