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

import com.cedarsoft.exceptions.CanceledException
import com.cedarsoft.unit.si.ms
import java.lang.ref.WeakReference
import java.util.ArrayList
import java.util.Collections
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.ThreadFactory
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import javax.swing.Timer

/**
 * Keeps references to the threads that have been created
 *
 */
class AccountingThreadService(
  /**
   * The thread factory that is used to create the threads
   */
  private val threadFactory: ThreadFactory
) : ThreadService {
  private val threads = Collections.synchronizedList(ArrayList<WeakReference<Thread>>())
  private val executorServices = Collections.synchronizedList(ArrayList<WeakReference<ExecutorService>>())
  private val swingTimers = Collections.synchronizedList(ArrayList<WeakReference<Timer>>())
  private val timers = Collections.synchronizedList(ArrayList<WeakReference<java.util.Timer>>())

  private val isShutdown = AtomicBoolean()

  override fun newThread(r: Runnable): Thread {
    ensureNotShutdown()

    val thread = threadFactory.newThread(r)
    threads.add(WeakReference(thread))
    return thread
  }

  override fun newFixedThreadPool(nThreads: Int, name: String): ExecutorService {
    ensureNotShutdown()

    val executorService = Executors.newFixedThreadPool(nThreads, NamedThreadFactory(this, name))
    executorServices.add(WeakReference(executorService))
    return executorService
  }

  override fun newCachedThreadPool(name: String): ExecutorService {
    ensureNotShutdown()

    val executorService = Executors.newCachedThreadPool(NamedThreadFactory(this, name))
    executorServices.add(WeakReference(executorService))
    return executorService
  }

  override fun newScheduledThreadPool(nThreads: Int, name: String): ScheduledExecutorService {
    ensureNotShutdown()

    val executor = ScheduledThreadPoolExecutor(nThreads, NamedThreadFactory(this, name))
    executorServices.add(WeakReference(executor))
    return executor
  }

  override fun newTimer(name: String): java.util.Timer {
    val timer = java.util.Timer()
    timers.add(WeakReference(timer))
    return timer
  }

  override fun newSwingTimer(@ms delay: Int): Timer {
    val swingTimer = Timer(delay, null)
    swingTimers.add(WeakReference(swingTimer))
    return swingTimer
  }

  fun getThreads(): List<WeakReference<Thread>> {
    return Collections.unmodifiableList(threads)
  }

  fun getExecutorServices(): List<WeakReference<ExecutorService>> {
    return Collections.unmodifiableList(executorServices)
  }

  private fun ensureNotShutdown() {
    if (isShutdown.get()) {
      throw CanceledException()
    }
  }

  fun shutdown() {
    isShutdown.set(true)

    //interrupt all threads
    for (threadRef in threads) {
      val thread = threadRef.get()
      thread?.interrupt()
    }

    for (executorService in executorServices) {
      val service = executorService.get()
      service?.shutdownNow()
    }

    //close all timers
    for (timerReference in swingTimers) {
      val timer = timerReference.get()
      timer?.stop()
    }

    for (timerReference in timers) {
      val timer = timerReference.get()
      timer?.cancel()
    }
  }

  @Throws(InterruptedException::class)
  fun awaitTermination(timeout: Int, timeUnit: TimeUnit): Boolean {
    var success = true
    for (executorService in executorServices) {
      val service = executorService.get()
      if (service != null) {
        if (!service.awaitTermination(timeout.toLong(), timeUnit)) {
          success = false
        }
      }
    }
    return success
  }
}
