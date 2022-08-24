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

import com.cedarsoft.test.utils.ThreadExtension
import org.assertj.core.api.Assertions
import org.awaitility.Awaitility
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTimeoutPreemptively
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.Duration
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

/**
 */
@ExtendWith(ThreadExtension::class)
class DefaultNewestOnlyJobManagerTest {
  private lateinit var executorService: ExecutorService

  @BeforeEach
  fun setUp() {
    executorService = Executors.newCachedThreadPool()
  }

  @AfterEach
  fun tearDown() {
    executorService.shutdownNow()
    Assertions.assertThat(executorService.awaitTermination(1, TimeUnit.SECONDS)).isTrue
  }

  @Test
  fun smoke() {
    val jobManager = DefaultNewestOnlyJobManager(executorService, 1)
    jobManager.startWorkers()
  }

  @Test
  fun onlyYoungestJobIsExecuted() {
    assertTimeoutPreemptively(Duration.ofSeconds(5)) {
      val jobManager = DefaultNewestOnlyJobManager(executorService, 1)
      for (i in 0..9) {
        jobManager.scheduleJob(object : NewestOnlyJobsManager.Job {
          override val key: Any
            get() {
              return "asdf"
            }

          override fun execute() {
            Assertions.fail<Any>("Must not be executed")
          }
        })
      }
      val executed = AtomicBoolean()
      jobManager.scheduleJob(object : NewestOnlyJobsManager.Job {
        override val key: Any
          get() {
            return "asdf"
          }

        override fun execute() {
          executed.set(true)
        }
      })
      Assertions.assertThat(executed.get()).isFalse
      jobManager.startWorkers()
      Awaitility.await().atMost(30, TimeUnit.SECONDS).pollDelay(10, TimeUnit.MILLISECONDS).until { executed.get() }
    }
  }

  @Test
  fun reproduceDeadLock() {
    val executed = AtomicBoolean()
    val jobManager = DefaultNewestOnlyJobManager(executorService, 1)
    jobManager.startWorkers()
    jobManager.scheduleJob(object : NewestOnlyJobsManager.Job {
      override val key: Any
        get() = "asf"

      override fun execute() {
        executed.set(true)
      }
    })
    Awaitility.await().atMost(30, TimeUnit.SECONDS).pollDelay(10, TimeUnit.MILLISECONDS).until { executed.get() }
  }
}
