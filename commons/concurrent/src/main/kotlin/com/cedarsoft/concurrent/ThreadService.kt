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
package it.neckar.open.concurrent

import it.neckar.open.unit.si.ms
import java.util.concurrent.ExecutorService
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ThreadFactory
import javax.swing.Timer

/**
 * Creates threads/timers/executors and stuff
 *
 */
interface ThreadService : ThreadFactory {
  /**
   * Creates a new thread
   */
  override fun newThread(r: Runnable): Thread

  /**
   * Creates a new fixed thread pool
   */
  fun newFixedThreadPool(nThreads: Int, name: String): ExecutorService

  /**
   * Creates a new cached thread pool
   */
  fun newCachedThreadPool(name: String): ExecutorService

  /**
   * Creates a new scheduled executor service
   */
  fun newScheduledThreadPool(nThreads: Int, name: String): ScheduledExecutorService

  /**
   * Creates a new timer
   */
  fun newTimer(name: String): java.util.Timer

  /**
   * Creates a new swing timer
   */
  fun newSwingTimer(@ms delay: Int): Timer
}
