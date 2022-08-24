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
package com.cedarsoft.file

import com.cedarsoft.file.FileCopyManager.copy
import com.cedarsoft.test.utils.TemporaryFolder
import com.cedarsoft.test.utils.WithTempFiles
import org.junit.After
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.io.FileOutputStream
import javax.annotation.Nonnull

/**
 *
 *
 * Date: 25.08.2006<br></br>
 * Time: 17:07:01<br></br>
 *
 * GlobalTilesCache *         [Xore Systems](http://www.xore.de)
 */
@WithTempFiles
class FileCopyTest {
  private lateinit var temporaryFolder: TemporaryFolder
  private lateinit var tmp: File
  private lateinit var myFile: File

  @BeforeEach
  fun setUp(@Nonnull temporaryFolder: TemporaryFolder) {
    tmp = temporaryFolder.newFolder()
    this.temporaryFolder = temporaryFolder
    tmp.mkdir()
    myFile = File(tmp, "test.txt")
    val out = FileOutputStream(myFile)
    out.write(60)
    out.close()
  }

  @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
  @Test
  fun testCopySimple() {
    val dst = temporaryFolder.newFolder()
    copy(tmp, dst)
    assertTrue(dst.exists())
    assertEquals(1, dst.list().size.toLong())
    assertEquals("test.txt", dst.list()[0])
  }

  @After
  fun tearDown() {
    myFile.delete()
    tmp.delete()
  }
}
