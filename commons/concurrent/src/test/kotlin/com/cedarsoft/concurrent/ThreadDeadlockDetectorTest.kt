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

import org.assertj.core.api.Assertions
import org.awaitility.Awaitility
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.time.Duration
import java.util.concurrent.BrokenBarrierException
import java.util.concurrent.CyclicBarrier
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

/**
 *
 */
class ThreadDeadlockDetectorTest {
  /**
   * Create two threads that deadlock each other.
   */
  @Test
  fun testBasic() {
    org.junit.jupiter.api.Assertions.assertTimeout(Duration.ofSeconds(10)) {
      val lockA: Lock = ReentrantLock()
      val lockB: Lock = ReentrantLock()
      val barrier = CyclicBarrier(2)

      //Lock the locks in different order for both threads
      val thread1 = Thread(LockBothLocksRunnable(lockA, lockB, barrier), "dead-thread1")
      val thread2 = Thread(LockBothLocksRunnable(lockB, lockA, barrier), "dead-thread2")
      val deadlockDetector = ThreadDeadlockDetector(10)
      val out = ByteArrayOutputStream()
      deadlockDetector.addListener(DefaultDeadlockListener(PrintStream(out)))
      val deadLockDetected = AtomicBoolean(false)
      deadlockDetector.addListener { deadLockDetected.set(true) }
      deadlockDetector.start()
      thread1.start()
      thread2.start()

      //Wait until the deadlock has been detected
      Awaitility
        .waitAtMost(5, TimeUnit.SECONDS)
        .untilTrue(deadLockDetected)
      deadlockDetector.stop()

      //Check the output of the dumps
      Assertions.assertThat(out.toString()).contains("Deadlocked Threads:")
      Assertions.assertThat(out.toString()).contains("Thread[dead-thread1,5")
      Assertions.assertThat(out.toString()).contains("Thread[dead-thread2,5")
    }
  }

  /**
   * A runnable that locks both locks.
   *
   *
   *
   *  * Lock [.lock1]
   *  * Wait for the barrier [.barrier]
   *  * Lock [.lock2]
   *
   */
  class LockBothLocksRunnable(
    val lock1: Lock,
    val lock2: Lock,
    val barrier: CyclicBarrier,
  ) : Runnable {
    override fun run() {
      lock1.lock()
      try {
        barrier.await()
      } catch (e: InterruptedException) {
        throw RuntimeException(e)
      } catch (e: BrokenBarrierException) {
        throw RuntimeException(e)
      }
      lock2.lock()
    }
  }
}
