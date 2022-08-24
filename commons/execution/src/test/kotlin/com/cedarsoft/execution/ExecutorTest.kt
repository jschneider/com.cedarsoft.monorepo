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
package com.cedarsoft.execution

import com.google.common.io.ByteStreams
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verifyOrder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets

/**
 *
 */
class ExecutorTest {
  private lateinit var targetOut: ByteArrayOutputStream
  private lateinit var targetErr: ByteArrayOutputStream

  @BeforeEach
  fun setUp() {
    targetOut = ByteArrayOutputStream()
    targetErr = ByteArrayOutputStream()
  }

  @Test
  fun testIt() {
    val executor = Executor(ProcessBuilder("java", "-version"), targetOut, targetErr)

    val listener = mockk<ExecutionListener>(relaxed = true)
    executor.addExecutionListener(listener)
    executor.execute()

    verifyOrder {
      listener.executionStarted(any())
      listener.executionFinished(0)
    }

    confirmVerified(listener)
  }

  @Test
  fun testAsync() {
    val executor = Executor(ProcessBuilder("java", "-version"), targetOut, targetErr)
    executor.executeAsync().join()
  }

  @Test
  fun testOutputRedirector() {
    val errorOut = ByteArrayOutputStream()
    val out = ByteArrayOutputStream()
    val executor = Executor(ProcessBuilder("java", "-version"), out, errorOut)
    executor.execute()
    assertEquals("", out.toString())
    assertTrue(errorOut.toString().contains("version"), errorOut.toString())
  }

  @Test
  fun testOutput() {
    val process = ProcessBuilder("java", "-version").start()
    assertEquals("", String(ByteStreams.toByteArray(process.inputStream), StandardCharsets.UTF_8))
    assertThat(String(ByteStreams.toByteArray(process.errorStream), StandardCharsets.UTF_8)).contains("version")
  }

  @Test
  fun testStreams() {
    val process = ProcessBuilder("java", "-version").start()
    val inStream = process.errorStream
    assertEquals(0, inStream.available().toLong())
    assertEquals(0, process.waitFor().toLong())
    val available = inStream.available()
    assertTrue(available > 100)
  }
}
