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

package com.cedarsoft.concurrent

import com.cedarsoft.unit.si.ms
import java.lang.management.ManagementFactory
import java.lang.management.ThreadInfo
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.CopyOnWriteArraySet

/**
 *
 * ThreadDeadlockDetector class.
 *
 */
class ThreadDeadlockDetector
/**
 *
 * Constructor for ThreadDeadlockDetector.
 *
 * @param deadlockCheckPeriod a int.
 */
@JvmOverloads constructor(private val deadlockCheckPeriod: Long = DEFAULT_DEADLOCK_CHECK_PERIOD) {
  private val threadCheck = Timer("ThreadDeadlockDetector", true)
  private val mXBean = ManagementFactory.getThreadMXBean()
  private val listeners = CopyOnWriteArraySet<Listener>()

  fun start() {
    threadCheck.scheduleAtFixedRate(object : TimerTask() {
      override fun run() {
        checkForDeadlocks()
      }
    }, this.deadlockCheckPeriod, this.deadlockCheckPeriod)
  }

  fun stop() {
    threadCheck.cancel()
  }

  private fun checkForDeadlocks() {
    val ids = findDeadlockedThreads()
    if (ids != null && ids.isNotEmpty()) {
      val threads = HashSet<Thread>()

      for (id in ids) {
        threads.add(findMatchingThread(mXBean.getThreadInfo(id)))
      }
      fireDeadlockDetected(threads)
    }
  }

  private fun findDeadlockedThreads(): LongArray? {
    // JDK 1.5 only supports the findMonitorDeadlockedThreads()
    // method, so you need to comment out the following three lines
    return if (mXBean.isSynchronizerUsageSupported) {
      mXBean.findDeadlockedThreads()
    } else {
      mXBean.findMonitorDeadlockedThreads()
    }
  }

  private fun fireDeadlockDetected(threads: Set<Thread>) {
    for (listener in listeners) {
      listener.deadlockDetected(threads)
    }
  }

  /**
   *
   * addListener
   *
   * @param listener a ThreadDeadlockDetector.Listener object.
   * @return a boolean.
   */
  fun addListener(listener: Listener): Boolean {
    return listeners.add(listener)
  }

  /**
   *
   * removeListener
   *
   * @param listener a ThreadDeadlockDetector.Listener object.
   * @return a boolean.
   */
  fun removeListener(listener: Listener): Boolean {
    return listeners.remove(listener)
  }

  /**
   * This is called whenever a problem with threads is detected.
   */
  fun interface Listener {
    fun deadlockDetected(deadlockedThreads: Set<Thread>)
  }

  companion object {
    /**
     * The number of milliseconds between checking for deadlocks.
     * It may be expensive to check for deadlocks, and it is not
     * critical to know so quickly.
     */
    @ms
    private val DEFAULT_DEADLOCK_CHECK_PERIOD: Long = 10000

    private fun findMatchingThread(threadInfo: ThreadInfo): Thread {
      for (thread in Thread.getAllStackTraces().keys) {
        if (thread.id == threadInfo.threadId) {
          return thread
        }
      }
      throw IllegalStateException("Deadlocked Thread not found")
    }
  }
}
/**
 *
 * Constructor for ThreadDeadlockDetector.
 */
