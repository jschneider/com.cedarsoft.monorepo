/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce
 *         (GPL 3 with Classpath Exception)
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

package com.cedarsoft.concurrent;

import static org.assertj.core.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.Duration;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.*;

/**
 *
 */
public class ThreadDeadlockDetectorTest {
  /**
   * Create two threads that deadlock each other.
   */
  @Test
  public void testBasic() throws Exception {
    Assertions.assertTimeout(Duration.ofSeconds(10), () -> {
      final Lock lockA = new ReentrantLock();
      final Lock lockB = new ReentrantLock();

      final CyclicBarrier barrier = new CyclicBarrier(2);

      //Lock the locks in different order for both threads
      final Thread thread1 = new Thread(new LockBothLocksRunnable(lockA, lockB, barrier), "dead-thread1");
      final Thread thread2 = new Thread(new LockBothLocksRunnable(lockB, lockA, barrier), "dead-thread2");

      ThreadDeadlockDetector deadlockDetector = new ThreadDeadlockDetector(10);

      final ByteArrayOutputStream out = new ByteArrayOutputStream();
      deadlockDetector.addListener(new DefaultDeadlockListener(new PrintStream(out)));

      AtomicBoolean deadLockDetected = new AtomicBoolean(false);
      deadlockDetector.addListener(deadlockedThreads -> deadLockDetected.set(true));
      deadlockDetector.start();

      thread1.start();
      thread2.start();

      //Wait until the dead lock has been detected
      Awaitility
        .waitAtMost(5, TimeUnit.SECONDS)
        .untilTrue(deadLockDetected);

      deadlockDetector.stop();

      //Check the output of the dumps
      assertThat(out.toString()).contains("Deadlocked Threads:");
      assertThat(out.toString()).contains("Thread[dead-thread1,5");
      assertThat(out.toString()).contains("Thread[dead-thread2,5");
    });
  }

  /**
   * A runnable that locks both locks.
   *
   *
   * <ul>
   * <li>Lock {@link #lock1}</li>
   * <li>Wait for the barrier {@link #barrier}</li>
   * <li>Lock {@link #lock2}</li>
   * </ul>
   */
  private static class LockBothLocksRunnable implements Runnable {
    private final Lock lock1;
    private final CyclicBarrier barrier;
    private final Lock lock2;

    private LockBothLocksRunnable(Lock lock1, Lock lock2, CyclicBarrier barrier) {
      this.lock1 = lock1;
      this.barrier = barrier;
      this.lock2 = lock2;
    }

    @Override
    public void run() {
      //noinspection LockAcquiredButNotSafelyReleased
      lock1.lock();
      try {
        barrier.await();
      }
      catch (InterruptedException | BrokenBarrierException e) {
        throw new RuntimeException( e );
      }

      //noinspection LockAcquiredButNotSafelyReleased
      lock2.lock();
    }
  }
}
