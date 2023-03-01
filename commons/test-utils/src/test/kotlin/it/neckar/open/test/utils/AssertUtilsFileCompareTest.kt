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
package it.neckar.open.test.utils

import it.neckar.open.crypt.Algorithm
import it.neckar.open.crypt.Hash.Companion.fromHex
import it.neckar.open.test.utils.AssertUtils.FailedFilesDir
import it.neckar.open.test.utils.AssertUtils.assertFileByHash
import it.neckar.open.test.utils.AssertUtils.assertFileByHashes
import it.neckar.open.test.utils.AssertUtils.createCopyFile
import org.apache.commons.io.FileUtils
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.charset.StandardCharsets
import javax.annotation.Nonnull

/**
 *
 */
@WithTempFiles
class AssertUtilsFileCompareTest {
  private var fileA: File? = null
  private var fileB: File? = null

  @BeforeEach
  fun setUp(@Nonnull tmp: TemporaryFolder) {
    fileA = tmp.newFile("a")
    FileUtils.writeStringToFile(fileA, "daContent", StandardCharsets.UTF_8)
    fileB = tmp.newFile("b")
    FileUtils.writeStringToFile(fileB, "daContentB", StandardCharsets.UTF_8)
  }

  @Test
  fun testCopyFile() {
    val dir = FailedFilesDir
    assertEquals(File(dir, "daPath" + File.separator + "daName"), createCopyFile("daPath", "daName"))
    assertEquals(File(dir, "daPath2" + File.separator + "other" + File.separator + "daName2"), createCopyFile("daPath2" + File.separator + "other", "daName2"))
  }

  @Test
  fun testBasic() {
    assertFileByHash(fromHex(Algorithm.MD5, "913aa4a45cea16f9714f109e7324159f"), fileA!!)
  }

  @Test
  fun testInvalid() {
    try {
      assertFileByHashes(fileA!!)
      fail("Where is the Exception")
    } catch (ignore: IllegalArgumentException) {
    }
  }

  @Test
  fun testOneOf() {
    assertFileByHashes(
      fileA!!, fromHex(Algorithm.MD5, "913aa4a45cea16f9714f109e7324159f"), fromHex(Algorithm.MD5, "AA"), fromHex(Algorithm.MD5, "BB")
    )
  }

  @Test
  fun testOneOf2() {
    assertFileByHashes(
      fileA!!, fromHex(Algorithm.MD5, "AA"), fromHex(Algorithm.MD5, "913aa4a45cea16f9714f109e7324159f"), fromHex(Algorithm.MD5, "BB")
    )
  }

  @Test
  fun testOneOf4() {
    assertFileByHashes(
      fileA!!, Algorithm.MD5, "AA", "913aa4a45cea16f9714f109e7324159f", "BB"
    )
  }

  @Test
  fun testOneOfFail() {
    try {
      assertFileByHashes(
        fileA!!, fromHex(Algorithm.MD5, "AA"), fromHex(Algorithm.MD5, "CC"), fromHex(Algorithm.MD5, "BB")
      )
      throw RuntimeException("expected assertion")
    } catch (e: AssertionError) {
      Assertions.assertThat(e.message).contains("Stored questionable file under test")
    }
  }

  @Test
  fun testFail() {
    try {
      assertFileByHash(fromHex(Algorithm.MD5, "AA"), fileA!!)
      throw RuntimeException("expected assertion")
    } catch (e: AssertionError) {
      Assertions.assertThat(e.message).contains("Stored questionable file under test")
    }
  }
}
