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
package com.cedarsoft.io

import com.cedarsoft.test.utils.TemporaryFolder
import com.cedarsoft.test.utils.WithTempFiles
import com.google.common.io.Files
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.charset.StandardCharsets
import javax.annotation.Nonnull

/**
 */
@WithTempFiles
class FileOutputStreamWithMoveTest {
  @Test
  fun basics(@Nonnull tmp: TemporaryFolder) {
    val targetOut = File(tmp.newFolder(), "asdf")
    assertThat(targetOut).doesNotExist()

    val out = FileOutputStreamWithMove(targetOut)
    assertThat(targetOut).doesNotExist()

    out.write("asdf".toByteArray(StandardCharsets.UTF_8))
    assertThat(targetOut).doesNotExist()

    out.close()
    assertThat(targetOut).exists()

    out.close()
    out.close()
    out.close()
    out.close()
  }

  @Test
  fun exception(@Nonnull tmp: TemporaryFolder) {
    val targetOut = File(tmp.newFolder(), "asdf")
    Files.write("daContent".toByteArray(StandardCharsets.UTF_8), targetOut)
    assertThat(targetOut).exists()

    val out = FileOutputStreamWithMove(targetOut)
    out.write("asdf".toByteArray(StandardCharsets.UTF_8))

    assertThat(targetOut).exists()
    assertThat(out.tmpFile).exists()

    //old content
    assertThat(Files.asCharSource(targetOut, StandardCharsets.UTF_8).read()).isEqualTo("daContent")
    out.close()

    assertThat(targetOut).exists()
    assertThat(Files.asCharSource(targetOut, StandardCharsets.UTF_8).read()).isEqualTo("asdf")
  }
}
