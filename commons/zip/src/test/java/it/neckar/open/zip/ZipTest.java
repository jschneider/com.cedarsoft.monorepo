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

package it.neckar.open.zip;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nonnull;

import org.junit.jupiter.api.*;

import it.neckar.open.test.utils.TempFolder;
import it.neckar.open.test.utils.WithTempFiles;

/**
 */
@WithTempFiles
public class ZipTest {
  @Test
  public void testFile(@Nonnull @TempFolder File parent) throws IOException {
    String name = "asdf" + System.nanoTime();
    File file = new File(parent, name);
    assertFalse( file.isDirectory() );
    assertEquals(name, ZipCreator.getRelativePath(parent.getAbsolutePath(), file));
  }

  @Test
  public void testFileCanonical(@Nonnull @TempFolder File parent) throws IOException {
    String name = "asdf" + System.nanoTime();
    File file = new File(parent, name);
    assertFalse(file.isDirectory());
    assertEquals(name, ZipCreator.getCanonicalRelativePath(parent.getCanonicalPath(), file));
  }

  @Test
  public void testDirectory(@Nonnull @TempFolder File parent) throws IOException {
    String name = "asdf" + System.nanoTime();
    File file = new File(parent, name);
    file.mkdirs();
    assertTrue( file.isDirectory() );
    assertEquals(name + "/", ZipCreator.getRelativePath(parent.getAbsolutePath(), file));
  }

  @Test
  public void testDirectoryCanonical(@Nonnull @TempFolder File parent) throws IOException {
    String name = "asdf" + System.nanoTime();
    File file = new File(parent, name);
    file.mkdirs();
    assertTrue(file.isDirectory());
    assertEquals(name + "/", ZipCreator.getCanonicalRelativePath(parent.getCanonicalPath(), file));
  }
}
