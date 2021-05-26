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

import com.cedarsoft.annotations.NonUiThread
import com.cedarsoft.annotations.UiThread
import com.cedarsoft.common.kotlin.lang.fastFor
import java.util.concurrent.BlockingDeque
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingDeque
import java.util.logging.Level
import java.util.logging.Logger
import javax.annotation.PostConstruct
import javax.annotation.concurrent.ThreadSafe
import kotlin.math.max

/**
 * Default implementation for newest only jobs manager
 */
@ThreadSafe
class DefaultNewestOnlyJobManager @JvmOverloads constructor(
  private val executorService: ExecutorService,
  private val workerCount: Int,
  /**
   * The max amount of jobs that are held
   */
  private val maxJobsCount: Int = 50
) : NewestOnlyJobsManager {

  /**
   * Contains the tile identifiers that shall be calculated
   */
  private val jobs: BlockingDeque<NewestOnlyJobsManager.Job>

  /**
   * Returns the next job from the queue
   */
  //Clean up all other jobs with the given key to avoid duplicate actions
  private val nextJob: NewestOnlyJobsManager.Job
    @Throws(InterruptedException::class)
    get() {
      val job = jobs.takeFirst()
      clearJobs(job.key)
      return job
    }

  init {
    this.jobs = LinkedBlockingDeque(maxJobsCount)

    if (workerCount < 1) {
      throw IllegalArgumentException("Need at least one worker but was <$workerCount>")
    }
  }

  /**
   * Starts the workers
   */
  @PostConstruct
  fun startWorkers() {
    //Start the worker threads
    workerCount.fastFor {
      executorService.execute(Worker())
    }
  }

  /**
   * A new job is scheduled
   */
  @UiThread
  @NonUiThread
  override fun scheduleJob(job: NewestOnlyJobsManager.Job) {
    if (LOG.isLoggable(Level.FINE)) {
      LOG.fine("schedule job for key <" + job.key + ">. Old size <" + jobs.size + ">")
    }
    //Remove old jobs with the same key
    clearJobs(job.key)

    //Schedule for calculation
    while (!jobs.offerFirst(job)) {
      //Cleanup if necessary
      ensureMaxJobsSize()
    }
  }

  override fun clearJobs(key: Any) {
    jobs.removeIf { job -> job.key == key }
  }

  /**
   * Deletes old jobs to ensure the max job count is respected
   */
  private fun ensureMaxJobsSize() {
    if (LOG.isLoggable(Level.FINE)) {
      LOG.fine("Cleaning up old jobs <" + jobs.size + ">")
    }
    while (jobs.size >= maxJobsCount) {
      jobs.pollLast()
    }
  }

  /**
   * Worker that always gets the first job and solves it
   */
  private inner class Worker : Runnable {
    override fun run() {
      while (!Thread.currentThread().isInterrupted) {
        try {
          nextJob.execute()
        } catch (ignore: InterruptedException) {
          return
        } catch (e: Throwable) {
          Thread.currentThread().uncaughtExceptionHandler.uncaughtException(Thread.currentThread(), e)
        }
      }
    }
  }

  companion object {
    private val LOG = Logger.getLogger(DefaultNewestOnlyJobManager::class.java.name)

    /**
     * Creates a new instance with a worker count depending on the available processors count.
     *
     *
     * Creates a manager with (processors-count - 1) workers.
     */
    fun create(): DefaultNewestOnlyJobManager {
      //Calculate the amount of background painting threads
      val cores = Runtime.getRuntime().availableProcessors()
      val workerCount = max(1, cores - 1)

      return DefaultNewestOnlyJobManager(Executors.newFixedThreadPool(workerCount, NamedThreadFactory("GlobalOptionalJobManager")), cores)
    }
  }
}
