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

import it.neckar.open.annotations.NonBlocking
import it.neckar.open.annotations.NonUiThread
import it.neckar.open.annotations.UiThread
import java.util.HashMap
import java.util.concurrent.locks.ReentrantReadWriteLock
import javax.annotation.concurrent.GuardedBy
import kotlin.concurrent.write

/**
 * Abstract base class for async execution of scheduled runnables.
 * Only the latest runnable is executed
 *
 */
abstract class AbstractAsync {
  private val lock = ReentrantReadWriteLock()

  @GuardedBy("lock")
  private val scheduledRunnables = HashMap<Any, Runnable>()

  /**
   * Asynchronously calls only the last added runnable.
   */
  @NonUiThread
  @UiThread
  fun last(runnable: Runnable) {
    last(runnable.javaClass, runnable)
  }

  /**
   * Asynchronously calls only the last runnable.
   */
  @NonUiThread
  @UiThread
  fun last(key: Any, runnable: Runnable) {
    lock.write {
      if (scheduledRunnables.put(key, runnable) != null) {
        //There is another job scheduled, so we do not have to reschedule it
        return
      }
    }

    //There has no other event been scheduled
    runInTargetThread(Runnable { getAndRemove(key).run() })
  }

  /**
   * Returns the runnable for the given key
   */
  @UiThread
  private fun getAndRemove(key: Any): Runnable {
    lock.write {
      return scheduledRunnables.remove(key) ?: throw IllegalStateException("No job found for <$key>")
    }
  }

  /**
   * Run in target thread
   */
  @NonBlocking
  protected abstract fun runInTargetThread(runnable: Runnable)
}
