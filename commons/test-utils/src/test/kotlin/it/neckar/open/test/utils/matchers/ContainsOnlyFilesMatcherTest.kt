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
import it.neckar.open.test.utils.matchers.ContainsOnlyFilesMatcher.Companion.containsOnlyFiles
import it.neckar.open.test.utils.matchers.ContainsOnlyFilesMatcher.Companion.toTree
import com.google.common.io.Files
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Fail
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import javax.annotation.Nonnull

/**
 *
 */
@WithTempFiles
class ContainsOnlyFilesMatcherTest {
  private lateinit var tmp: TemporaryFolder

  @BeforeEach
  fun setUp(@Nonnull tmp: TemporaryFolder) {
    this.tmp = tmp
  }

  @Test
  fun testOnly() {
    Files.touch(File(tmp.newFolder("dir"), "a"))
    assertThat(tmp.getRoot()).describedAs(toTree(tmp.getRoot())).matches(containsOnlyFiles("dir/a"))
    try {
      Files.touch(File(tmp.newFolder("dir2"), "a2"))
      assertThat(tmp.getRoot()).describedAs(toTree(tmp.getRoot())).matches(containsOnlyFiles("dir/a"))
      Fail.fail<Any>("Where is the Exception")
    } catch (ignore: AssertionError) {
    }
  }

  @Test
  fun testNone() {
    try {
      assertThat(tmp.getRoot()).describedAs(toTree(tmp.getRoot())).matches(containsOnlyFiles("dir/a"))
      Fail.fail<Any>("Where is the Exception")
    } catch (ignore: AssertionError) {
    }
  }

  @Test
  fun testWrong() {
    Files.touch(File(tmp.newFolder("dir"), "a"))
    try {
      assertThat(tmp.getRoot()).describedAs(toTree(tmp.getRoot())).matches(containsOnlyFiles("dir/b"))
      Fail.fail<Any>("Where is the Exception")
    } catch (ignore: AssertionError) {
    }
  }
}
