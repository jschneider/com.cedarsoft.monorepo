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
package it.neckar.open.execution

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.BufferedInputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.nio.charset.StandardCharsets

/**
 */
class OutputRedirectorTest {
  @Test
  fun testDefault() {
    val out = ByteArrayOutputStream()
    val outputRedirector = OutputRedirector(ByteArrayInputStream("asdf".toByteArray(StandardCharsets.UTF_8)), out)
    outputRedirector.run()
    assertEquals("asdf", out.toString())
  }

  @Test
  fun testThreaded() {
    val out = ByteArrayOutputStream()
    val outputRedirector = OutputRedirector(ByteArrayInputStream("asdf".toByteArray(StandardCharsets.UTF_8)), out)
    val thread = Thread(outputRedirector)
    thread.start()
    thread.join()
    assertEquals("asdf", out.toString())
  }

  @Test
  fun testBlocking() {
    val out = ByteArrayOutputStream()
    val outputRedirector = OutputRedirector(BlockingInputStream(), out)
    val thread = Thread(outputRedirector)
    thread.start()
    thread.join(100)
    Assertions.assertTrue(thread.isAlive)
    assertEquals("*", out.toString())
    thread.join(100)
    Assertions.assertTrue(thread.isAlive)
    thread.interrupt()
    thread.join(100)
    Assertions.assertFalse(thread.isAlive)
  }

  @Test
  fun testStreams() {
    val stream = ByteArrayInputStream("asdf".toByteArray(StandardCharsets.UTF_8))
    assertEquals(4, stream.available().toLong())
    stream.read()
    assertEquals(3, stream.available().toLong())
    stream.read()
    assertEquals(2, stream.available().toLong())
    stream.read()
    assertEquals(1, stream.available().toLong())
    stream.read()
    assertEquals(0, stream.available().toLong())
    assertEquals(0, stream.available().toLong())
    assertEquals(-1, stream.read().toLong())
  }

  @Test
  fun testStreamsBuffered() {
    val stream: InputStream = BufferedInputStream(ByteArrayInputStream("asdf".toByteArray(StandardCharsets.UTF_8)))
    assertEquals(4, stream.available().toLong())
    stream.read()
    assertEquals(3, stream.available().toLong())
    stream.read()
    assertEquals(2, stream.available().toLong())
    stream.read()
    assertEquals(1, stream.available().toLong())
    stream.read()
    assertEquals(0, stream.available().toLong())
    assertEquals(0, stream.available().toLong())
    assertEquals(-1, stream.read().toLong())
  }

  private class BlockingInputStream : InputStream() {
    @Volatile
    private var blocking = false

    override fun available(): Int = 1

    override fun read(b: ByteArray, off: Int, len: Int): Int {
      if (!blocking) {
        blocking = true
        b[0] = 42
        return 1
      }
      try {
        Thread.sleep(10000000)
      } catch (ignore: InterruptedException) {
      }
      return -1
    }

    override fun read(): Int {
      throw UnsupportedOperationException()
    }
  }
}
