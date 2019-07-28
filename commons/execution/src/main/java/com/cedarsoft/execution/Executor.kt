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

import java.io.IOException
import java.io.OutputStream
import java.util.ArrayList

/**
 * Executes a process and notifies execution listeners whenever the process has finished
 *
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
class Executor(
  private val processBuilder: ProcessBuilder,
  private val targetOut: OutputRedirector.ByteSink,
  private val targetErr: OutputRedirector.ByteSink
) {
  private val executionListeners = ArrayList<ExecutionListener>()

  /**
   *
   * isRedirectStreams
   *
   * @return a boolean.
   */
  val isRedirectStreams: Boolean
    @Deprecated("")
    get() = true

  /**
   *
   * Constructor for Executor.
   *
   * @param processBuilder a ProcessBuilder object.
   */
  @Deprecated("")
  constructor(processBuilder: ProcessBuilder) : this(processBuilder, OutputStreamByteSink(System.out), OutputStreamByteSink(System.err)) {
  }

  constructor(processBuilder: ProcessBuilder, targetOut: OutputStream, targetErr: OutputStream) : this(processBuilder, OutputStreamByteSink(targetOut), OutputStreamByteSink(targetErr)) {}

  /**
   *
   * execute
   *
   * @return a int.
   *
   * @throws IOException          if any.
   * @throws InterruptedException if any.
   */
  @Throws(IOException::class, InterruptedException::class)
  fun execute(): Int {
    val process = processBuilder.start()
    val threads = redirectStreams(process)
    notifyExecutionStarted(process)

    val answer = process.waitFor()

    //Wait for the redirecting threads
    for (thread in threads) {
      thread.join()
    }

    notifyExecutionFinished(answer)
    return answer
  }

  /**
   *
   * redirectStreams
   *
   * @param process a Process object.
   * @return the redirecting threads (or an empty array)
   */
  protected fun redirectStreams(process: Process): Array<Thread> {
    return OutputRedirector.redirect(process, targetOut, targetErr)
  }

  /**
   *
   * executeAsync
   *
   * @return the thread
   */
  fun executeAsync(): Thread {
    val thread = Thread {
      try {
        execute()
      } catch (e: Exception) {
        throw RuntimeException(e)
      }
    }
    thread.start()
    return thread
  }

  private fun notifyExecutionFinished(answer: Int) {
    for (executionListener in executionListeners) {
      executionListener.executionFinished(answer)
    }
  }

  private fun notifyExecutionStarted(process: Process) {
    for (executionListener in executionListeners) {
      executionListener.executionStarted(process)
    }
  }

  /**
   *
   * addExecutionListener
   *
   * @param executionListener a ExecutionListener object.
   */
  fun addExecutionListener(executionListener: ExecutionListener) {
    this.executionListeners.add(executionListener)
  }

  /**
   *
   * removeExecutionListener
   *
   * @param executionListener a ExecutionListener object.
   */
  fun removeExecutionListener(executionListener: ExecutionListener) {
    this.executionListeners.remove(executionListener)
  }
}
