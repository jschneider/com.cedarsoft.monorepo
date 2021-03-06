/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce
 *         (GPL 3 with Classpath Exception)
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

package com.cedarsoft.test.utils.matchers;

import static com.cedarsoft.test.utils.matchers.ContainsFileMatcher.containsFiles;
import static com.cedarsoft.test.utils.matchers.ContainsFileMatcher.empty;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nonnull;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import com.cedarsoft.test.utils.TemporaryFolder;
import com.cedarsoft.test.utils.WithTempFiles;
import com.google.common.io.Files;

/**
 *
 */
@SuppressWarnings("TryFailThrowable")
@WithTempFiles
public class ContainsFileMatcherTest {
  private TemporaryFolder tmp;

  @BeforeEach
  void setUp(@Nonnull TemporaryFolder tmp) {
    this.tmp = tmp;
  }

  @Test
  public void testBasic() throws IOException {
    Files.touch( tmp.newFile( "a" ) );
    Assertions.assertThat(tmp.getRoot()).matches(containsFiles("a"));
    Files.touch( tmp.newFile( "b" ) );
    Assertions.assertThat(tmp.getRoot()).matches(containsFiles("a", "b"));
  }

  @Test
  public void testSubDir() throws IOException {
    Files.touch( new File( tmp.newFolder( "dir" ), "a" ) );
    Assertions.assertThat(tmp.getRoot()).matches(containsFiles("dir/a"));
  }

  @Test
  public void testEmpty() throws IOException {
    Assertions.assertThat(tmp.getRoot()).matches(empty());

    org.junit.jupiter.api.Assertions.assertThrows(AssertionError.class, () -> {
      Files.touch(tmp.newFile("a"));
      Assertions.assertThat(tmp.getRoot()).matches(empty(), "to be empty");
    });
  }

  @Test
  public void testList() throws IOException {
    {
      File dir = tmp.newFolder( "dir" );
      Files.touch( new File( dir, "b" ) );
      Files.touch( new File( dir, "a" ) );
      Files.touch( new File( dir, "c" ) );
    }

    {
      File dir = tmp.newFolder( "dir2" );
      Files.touch( new File( dir, "a" ) );
      Files.touch( new File( dir, "c" ) );
      Files.touch( new File( dir, "b" ) );
    }


    String tree = ContainsFileMatcher.toTree(tmp.getRoot());

    assertTrue(tree.contains("dir" + File.separator + "a"));
    assertTrue(tree.contains("dir" + File.separator + "b"));
    assertTrue(tree.contains("dir" + File.separator + "c"));
    assertTrue(tree.contains("dir2" + File.separator + "a"));
    assertTrue(tree.contains("dir2" + File.separator + "b"));
    assertTrue(tree.contains("dir2" + File.separator + "c"));
  }

  @SuppressWarnings("TryFailThrowable")
  @Test
  public void testNontExistent() throws IOException {
    try {
      Assertions.assertThat(tmp.getRoot()).matches(containsFiles("a"));
      throw new RuntimeException("expected assertion");
    }
    catch (AssertionError ignore) {
    }
  }
}
