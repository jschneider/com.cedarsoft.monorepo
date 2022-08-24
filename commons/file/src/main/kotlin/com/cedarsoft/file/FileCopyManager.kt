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

import com.cedarsoft.common.collections.fastForEach
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.channels.FileChannel
import javax.annotation.Nonnull

/**
 * Offers utility methods for file copying
 *
 */
object FileCopyManager {
  /**
   *
   * deleteForced
   *
   * @param toDelete a File object.
   */
  @JvmStatic
  fun deleteForced(@Nonnull toDelete: File) {
    require(toDelete.exists()) { "File must exist: " + toDelete.absolutePath }
    if (toDelete.isDirectory) {

      toDelete.list()?.fastForEach { entry ->
        val child = File(toDelete, entry)
        deleteForced(child)
      }
    }
    toDelete.delete()
  }

  /**
   *
   * copy
   *
   * @param src  a File object.
   * @param dest a File object.
   * @throws IOException if any.
   */
  @JvmStatic
  fun copy(@Nonnull src: File, @Nonnull dest: File) {
    if (src.isDirectory) {
      copyDirectory(src, dest)
    } else {
      copyFile(src, dest)
    }
  }

  /**
   *
   * copyFile
   *
   * @param source a File object.
   * @param target a File object.
   * @throws IOException if any.
   */
  @JvmStatic
  fun copyFile(@Nonnull source: File, @Nonnull target: File) {
    var sourceChannel: FileChannel? = null
    var targetChannel: FileChannel? = null
    var inputStream: FileInputStream? = null
    var out: FileOutputStream? = null

    try {
      inputStream = FileInputStream(source)
      sourceChannel = inputStream.channel
      out = FileOutputStream(target)
      targetChannel = out.channel
      sourceChannel.transferTo(0, sourceChannel.size(), targetChannel)
    } finally {
      sourceChannel?.close()
      targetChannel?.close()
      inputStream?.close()
      out?.close()
    }
  }

  /**
   *
   * copyDirectory
   *
   * @param srcDir  a File object.
   * @param destDir a File object.
   * @throws IOException if any.
   */
  @JvmStatic
  fun copyDirectory(@Nonnull srcDir: File, @Nonnull destDir: File) {
    if (!destDir.exists()) {
      destDir.mkdirs()
    }

    srcDir.list()?.fastForEach { entry ->
      val src = File(srcDir, entry)
      val dest = File(destDir, entry)
      copy(src, dest)
    }
  }
}
