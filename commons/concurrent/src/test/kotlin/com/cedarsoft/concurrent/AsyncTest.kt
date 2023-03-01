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

import it.neckar.open.test.utils.ThreadExtension
import org.assertj.core.api.Assertions
import org.awaitility.Awaitility
import org.hamcrest.core.IsNot
import org.hamcrest.core.IsNull
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

/**
 */
@ExtendWith(ThreadExtension::class)
class AsyncTest {
  private var executor: ExecutorService? = null

  @BeforeEach
  fun setUp() {
    executor = Executors.newSingleThreadExecutor()
  }

  @AfterEach
  fun tearDown() {
    executor!!.shutdownNow()
    executor!!.awaitTermination(100, TimeUnit.MILLISECONDS)
  }

  @Disabled
  @Test
  fun testException() {
    val defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
    try {
      val caught = AtomicReference<Throwable>()
      Thread.setDefaultUncaughtExceptionHandler { t: Thread?, e: Throwable -> caught.set(e) }
      val async = Async(executor!!)
      async.last { throw IllegalArgumentException("Uups") }
      Awaitility.await().timeout(30, TimeUnit.SECONDS).untilAtomic(caught, IsNot(IsNull()))
    } finally {
      Thread.setDefaultUncaughtExceptionHandler(defaultUncaughtExceptionHandler)
    }
  }

  @Test
  fun basic() {
    val async = Async(executor!!)
    val otherJobsAdded = AtomicBoolean()
    val firstJobStarted = AtomicBoolean()
    async.last("asdf") {
      firstJobStarted.set(true)
      Awaitility.await().atMost(30, TimeUnit.SECONDS)
        .pollDelay(10, TimeUnit.MILLISECONDS)
        .until { otherJobsAdded.get() }
    }

    //Ensure the first stop has been started
    Awaitility.await().atMost(30, TimeUnit.SECONDS)
      .pollDelay(10, TimeUnit.MILLISECONDS)
      .until { firstJobStarted.get() }


    //Schedule other jobs that should be skipped, since there will be another job added last
    for (i in 0..9) {
      async.last("asdf") { Assertions.fail<Any>("Must not be called") }
    }


    //Start the final job that is executed
    val executed = AtomicBoolean()
    async.last("asdf") { executed.set(true) }

    //finish first job
    otherJobsAdded.set(true)
    Awaitility.await().atMost(30, TimeUnit.SECONDS).pollDelay(10, TimeUnit.MILLISECONDS).until { executed.get() }
  }
}
