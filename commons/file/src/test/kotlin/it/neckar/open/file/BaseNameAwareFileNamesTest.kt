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
package it.neckar.open.file

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 *
 */
class BaseNameAwareFileNamesTest {
  private lateinit var report: BaseNameAwareFileNames

  @Test
  fun testDuplicate() {
    report.add(FileName("base1", ".", "txt"))
    try {
      report.add(FileName("base1", ".", "txt"))
      fail("Where is the Exception")
    } catch (ignore: Exception) {
    }
  }

  @Test
  fun testIt() {
    val report = BaseNameAwareFileNames()
    report.add(FileName("base1", ".", "txt"))
    report.add(FileName("base1", ".", "txt2"))
    report.add(FileName("base1", ".", "txt3"))
    report.add(FileName("base2", ".", "txt"))
    report.add(FileName("base3", ".", "txt"))
    val sameBaseNameEntry = report.getEntry(BaseName("base1"))
    assertNotNull(sameBaseNameEntry)
    assertEquals(3, sameBaseNameEntry.getFileNames().size.toLong())
  }

  @BeforeEach
  fun setUp() {
    report = BaseNameAwareFileNames()
  }
}
