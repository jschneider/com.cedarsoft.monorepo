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
package it.neckar.open.test.utils.matchers

import it.neckar.open.test.utils.TemporaryFolder
import it.neckar.open.test.utils.WithTempFiles
import it.neckar.open.test.utils.matchers.ContainsFileMatcher.Companion.containsFiles
import it.neckar.open.test.utils.matchers.ContainsFileMatcher.Companion.empty
import it.neckar.open.test.utils.matchers.ContainsFileMatcher.Companion.toTree
import com.google.common.io.Files
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.io.IOException

/**
 *
 */
@WithTempFiles
class ContainsFileMatcherTest {

  private lateinit var tmp: TemporaryFolder

  @BeforeEach
  fun setUp(tmp: TemporaryFolder) {
    this.tmp = tmp
  }

  @Test
  fun testBasic() {
    Files.touch(tmp.newFile("a"))
    assertThat(tmp.getRoot()).matches(containsFiles("a"))
    Files.touch(tmp.newFile("b"))
    assertThat(tmp.getRoot()).matches(containsFiles("a", "b"))
  }

  @Test
  fun testSubDir() {
    Files.touch(File(tmp.newFolder("dir"), "a"))
    assertThat(tmp.getRoot()).matches(containsFiles("dir/a"))
  }

  @Test
  fun testEmpty() {
    assertThat(tmp.getRoot()).matches(empty())
    assertThrows(AssertionError::class.java) {
      Files.touch(tmp.newFile("a"))
      assertThat(tmp.getRoot()).matches(empty(), "to be empty")
    }
  }

  @Test
  fun testList() {
    run {
      val dir = tmp.newFolder("dir")
      Files.touch(File(dir, "b"))
      Files.touch(File(dir, "a"))
      Files.touch(File(dir, "c"))
    }
    run {
      val dir = tmp.newFolder("dir2")
      Files.touch(File(dir, "a"))
      Files.touch(File(dir, "c"))
      Files.touch(File(dir, "b"))
    }
    val tree = toTree(tmp.getRoot())
    assertTrue(tree.contains("dir" + File.separator + "a"))
    assertTrue(tree.contains("dir" + File.separator + "b"))
    assertTrue(tree.contains("dir" + File.separator + "c"))
    assertTrue(tree.contains("dir2" + File.separator + "a"))
    assertTrue(tree.contains("dir2" + File.separator + "b"))
    assertTrue(tree.contains("dir2" + File.separator + "c"))
  }

  @Test
  @Throws(IOException::class)
  fun testNontExistent() {
    try {
      assertThat(tmp.getRoot()).matches(containsFiles("a"))
      throw RuntimeException("expected assertion")
    } catch (ignore: AssertionError) {
    }
  }
}
