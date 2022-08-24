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

import java.io.IOException
import java.io.InputStream
import java.io.Reader

/**
 * @noinspection RefusedBequest,SynchronizedMethod
 */
class ReaderInputStream @JvmOverloads
constructor(
  private val `in`: Reader,
  val encoding: String = System.getProperty("file.encoding"),

  ) : InputStream() {

  private var slack: ByteArray? = null

  private var begin = 0

  /**
   * Reads from the <CODE>Reader</CODE>, returning the same value.
   *
   * @return the value of the next character in the <CODE>Reader</CODE>.
   *
   * @throws IOException if the original `Reader` fails to be read
   */
  @Throws(IOException::class)
  override fun read(): Int {
    var result: Byte
    if (slack != null && begin < slack!!.size) {
      result = slack!![begin]
      if (++begin == slack!!.size) {
        slack = null
      }
    } else {
      val buf = ByteArray(1)
      if (read(buf, 0, 1) <= 0) {
        result = -1
      }
      result = buf[0]
    }
    if (result < -1) {
      result = (result + 256).toByte()
    }
    return result.toInt()
  }

  /**
   * Reads from the `Reader` into a byte array
   *
   * @param b   the byte array to read into
   * @param off the offset in the byte array
   * @param len the length in the byte array to fill
   * @return the actual number read into the byte array, -1 at
   * the end of the stream
   *
   * @throws IOException if an error occurs
   */
  @Throws(IOException::class)
  override fun read(b: ByteArray, off: Int, len: Int): Int {
    var len = len
    while (slack == null) {
      val buf = CharArray(len) // might read too much
      val n = `in`.read(buf)
      if (n == -1) {
        return -1
      }
      if (n > 0) {
        slack = String(buf, 0, n).toByteArray(charset(encoding!!))
        begin = 0
      }
    }
    if (len > slack!!.size - begin) {
      len = slack!!.size - begin
    }
    System.arraycopy(slack, begin, b, off, len)
    if (len.let { begin += it; begin } >= slack!!.size) {
      slack = null
    }
    return len
  }

  @Synchronized
  override fun mark(readlimit: Int) {
    try {
      `in`.mark(readlimit)
    } catch (ioe: IOException) {
      throw RuntimeException(ioe.message)
    }
  }

  @Synchronized
  @Throws(IOException::class)
  override fun available(): Int {
    if (slack != null) {
      return slack!!.size - begin
    }
    return if (`in`.ready()) {
      1
    } else {
      0
    }
  }

  override fun markSupported(): Boolean {
    return false // would be imprecise
  }

  @Synchronized
  @Throws(IOException::class)
  override fun reset() {
    slack = null
    `in`.reset()
  }

  @Throws(IOException::class)
  override fun close() {
    `in`.close()
    slack = null
  }
}
