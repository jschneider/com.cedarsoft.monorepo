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

import com.google.common.base.Joiner
import com.google.common.util.concurrent.CycleDetectingLockFactory
import org.assertj.core.api.Assertions
import org.junit.Assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import java.util.concurrent.locks.ReentrantReadWriteLock
import java.util.regex.Pattern

/**
 */
@Disabled
class CycleDetectingLockFactoryDemo {
  private var lockA: ReentrantLock? = null
  private var lockB: ReentrantLock? = null
  private var lockC: ReentrantLock? = null
  private var readLockA: ReentrantReadWriteLock.ReadLock? = null
  private var readLockB: ReentrantReadWriteLock.ReadLock? = null
  private var readLockC: ReentrantReadWriteLock.ReadLock? = null
  private var writeLockA: ReentrantReadWriteLock.WriteLock? = null
  private var writeLockB: ReentrantReadWriteLock.WriteLock? = null
  private var writeLockC: ReentrantReadWriteLock.WriteLock? = null
  private val lock1: ReentrantLock? = null
  private val lock2: ReentrantLock? = null
  private val lock3: ReentrantLock? = null
  private val lock01: ReentrantLock? = null
  private val lock02: ReentrantLock? = null
  private val lock03: ReentrantLock? = null

  @BeforeEach
  @Throws(Exception::class)
  fun setUp() {
    val factory = CycleDetectingLockFactory.newInstance(CycleDetectingLockFactory.Policies.THROW)
    lockA = factory.newReentrantLock("LockA")
    lockB = factory.newReentrantLock("LockB")
    lockC = factory.newReentrantLock("LockC")
    val readWriteLockA = factory.newReentrantReadWriteLock("ReadWriteA")
    val readWriteLockB = factory.newReentrantReadWriteLock("ReadWriteB")
    val readWriteLockC = factory.newReentrantReadWriteLock("ReadWriteC")
    readLockA = readWriteLockA.readLock()
    readLockB = readWriteLockB.readLock()
    readLockC = readWriteLockC.readLock()
    writeLockA = readWriteLockA.writeLock()
    writeLockB = readWriteLockB.writeLock()
    writeLockC = readWriteLockC.writeLock()
  }

  @Test
  fun testDeadlock_twoLocks() {
    // Establish an acquisition order of lockA -> lockB.
    lockA!!.lock()
    lockB!!.lock()
    lockA!!.unlock()
    lockB!!.unlock()

    // The opposite order should fail (Policies.THROW).
    var firstException: CycleDetectingLockFactory.PotentialDeadlockException? = null
    lockB!!.lock()
    try {
      lockA!!.lock()
      Assertions.fail<Any>("Expected PotentialDeadlockException")
    } catch (expected: CycleDetectingLockFactory.PotentialDeadlockException) {
      checkMessage(expected, "LockB -> LockA", "LockA -> LockB")
      firstException = expected
    }

    // Second time should also fail, with a cached causal chain.
    try {
      lockA!!.lock()
      Assertions.fail<Any>("Expected PotentialDeadlockException")
    } catch (expected: CycleDetectingLockFactory.PotentialDeadlockException) {
      checkMessage(expected, "LockB -> LockA", "LockA -> LockB")
      // The causal chain should be cached.
      Assert.assertSame(firstException!!.cause, expected.cause)
    }

    // lockA should work after lockB is released.
    lockB!!.unlock()
    lockA!!.lock()
  }

  @Test // Tests transitive deadlock detection.
  fun testDeadlock_threeLocks() {
    // Establish an ordering from lockA -> lockB.
    lockA!!.lock()
    lockB!!.lock()
    lockB!!.unlock()
    lockA!!.unlock()

    // Establish an ordering from lockB -> lockC.
    lockB!!.lock()
    lockC!!.lock()
    lockB!!.unlock()

    // lockC -> lockA should fail.
    try {
      lockA!!.lock()
      Assertions.fail<Any>("Expected PotentialDeadlockException")
    } catch (expected: CycleDetectingLockFactory.PotentialDeadlockException) {
      checkMessage(
        expected, "LockC -> LockA", "LockB -> LockC", "LockA -> LockB"
      )
    }
  }

  @Test
  fun testReentrancy_noDeadlock() {
    lockA!!.lock()
    lockB!!.lock()
    lockA!!.lock() // Should not assert on lockB -> reentrant(lockA)
  }

  @Test
  fun testExplicitOrdering_noViolations() {
    lock1!!.lock()
    lock3!!.lock()
    lock3.unlock()
    lock2!!.lock()
    lock3.lock()
  }

  @Test
  fun testExplicitOrdering_violations() {
    lock3!!.lock()
    try {
      lock2!!.lock()
      Assertions.fail<Any>("Expected PotentialDeadlockException")
    } catch (expected: CycleDetectingLockFactory.PotentialDeadlockException) {
      checkMessage(expected, "MyOrder.THIRD -> MyOrder.SECOND")
    }
    try {
      lock1!!.lock()
      Assertions.fail<Any>("Expected PotentialDeadlockException")
    } catch (expected: CycleDetectingLockFactory.PotentialDeadlockException) {
      checkMessage(expected, "MyOrder.THIRD -> MyOrder.FIRST")
    }
    lock3.unlock()
    lock2!!.lock()
    try {
      lock1!!.lock()
      Assertions.fail<Any>("Expected PotentialDeadlockException")
    } catch (expected: CycleDetectingLockFactory.PotentialDeadlockException) {
      checkMessage(expected, "MyOrder.SECOND -> MyOrder.FIRST")
    }
  }

  @Test
  fun testDifferentOrderings_noViolations() {
    lock3!!.lock() // MyOrder, ordinal() == 3
    lock01!!.lock() // OtherOrder, ordinal() == 1
  }

  @Test
  fun testExplicitOrderings_generalCycleDetection() {
    lock3!!.lock() // MyOrder, ordinal() == 3
    lock01!!.lock() // OtherOrder, ordinal() == 1
    lock3.unlock()
    try {
      lock3.lock()
      Assertions.fail<Any>("Expected PotentialDeadlockException")
    } catch (expected: CycleDetectingLockFactory.PotentialDeadlockException) {
      checkMessage(
        expected,
        "OtherOrder.FIRST -> MyOrder.THIRD",
        "MyOrder.THIRD -> OtherOrder.FIRST"
      )
    }
    lockA!!.lock()
    lock01.unlock()
    lockB!!.lock()
    lockA!!.unlock()
    try {
      lock01.lock()
      Assertions.fail<Any>("Expected PotentialDeadlockException")
    } catch (expected: CycleDetectingLockFactory.PotentialDeadlockException) {
      checkMessage(
        expected,
        "LockB -> OtherOrder.FIRST",
        "LockA -> LockB",
        "OtherOrder.FIRST -> LockA"
      )
    }
  }

  @Test
  fun testExplicitOrdering_cycleWithUnorderedLock() {
    val myLock: Lock = CycleDetectingLockFactory.newInstance(CycleDetectingLockFactory.Policies.THROW)
      .newReentrantLock("MyLock")
    lock03!!.lock()
    myLock.lock()
    lock03.unlock()
    try {
      lock01!!.lock()
      Assertions.fail<Any>("Expected PotentialDeadlockException")
    } catch (expected: CycleDetectingLockFactory.PotentialDeadlockException) {
      checkMessage(
        expected,
        "MyLock -> OtherOrder.FIRST",
        "OtherOrder.THIRD -> MyLock",
        "OtherOrder.FIRST -> OtherOrder.THIRD"
      )
    }
  }

  @Test
  fun testReadLock_deadlock() {
    readLockA!!.lock() // Establish an ordering from readLockA -> lockB.
    lockB!!.lock()
    lockB!!.unlock()
    readLockA!!.unlock()
    lockB!!.lock()
    try {
      readLockA!!.lock()
      Assertions.fail<Any>("Expected PotentialDeadlockException")
    } catch (expected: CycleDetectingLockFactory.PotentialDeadlockException) {
      checkMessage(expected, "LockB -> ReadWriteA", "ReadWriteA -> LockB")
    }
  }

  @Test
  fun testReadLock_transitive() {
    readLockA!!.lock() // Establish an ordering from readLockA -> lockB.
    lockB!!.lock()
    lockB!!.unlock()
    readLockA!!.unlock()

    // Establish an ordering from lockB -> readLockC.
    lockB!!.lock()
    readLockC!!.lock()
    lockB!!.unlock()
    readLockC!!.unlock()

    // readLockC -> readLockA
    readLockC!!.lock()
    try {
      readLockA!!.lock()
      Assertions.fail<Any>("Expected PotentialDeadlockException")
    } catch (expected: CycleDetectingLockFactory.PotentialDeadlockException) {
      checkMessage(
        expected,
        "ReadWriteC -> ReadWriteA",
        "LockB -> ReadWriteC",
        "ReadWriteA -> LockB"
      )
    }
  }

  @Test
  fun testWriteLock_threeLockDeadLock() {
    // Establish an ordering from writeLockA -> writeLockB.
    writeLockA!!.lock()
    writeLockB!!.lock()
    writeLockB!!.unlock()
    writeLockA!!.unlock()

    // Establish an ordering from writeLockB -> writeLockC.
    writeLockB!!.lock()
    writeLockC!!.lock()
    writeLockB!!.unlock()

    // writeLockC -> writeLockA should fail.
    try {
      writeLockA!!.lock()
      Assertions.fail<Any>("Expected PotentialDeadlockException")
    } catch (expected: CycleDetectingLockFactory.PotentialDeadlockException) {
      checkMessage(
        expected,
        "ReadWriteC -> ReadWriteA",
        "ReadWriteB -> ReadWriteC",
        "ReadWriteA -> ReadWriteB"
      )
    }
  }

  @Test
  fun testWriteToReadLockDowngrading() {
    writeLockA!!.lock() // writeLockA downgrades to readLockA
    readLockA!!.lock()
    writeLockA!!.unlock()
    lockB!!.lock() // readLockA -> lockB
    readLockA!!.unlock()

    // lockB -> writeLockA should fail
    try {
      writeLockA!!.lock()
      Assertions.fail<Any>("Expected PotentialDeadlockException")
    } catch (expected: CycleDetectingLockFactory.PotentialDeadlockException) {
      checkMessage(
        expected, "LockB -> ReadWriteA", "ReadWriteA -> LockB"
      )
    }
  }

  @Test
  fun testReadWriteLockDeadlock() {
    writeLockA!!.lock() // Establish an ordering from writeLockA -> lockB
    lockB!!.lock()
    writeLockA!!.unlock()
    lockB!!.unlock()

    // lockB -> readLockA should fail.
    lockB!!.lock()
    try {
      readLockA!!.lock()
      Assertions.fail<Any>("Expected PotentialDeadlockException")
    } catch (expected: CycleDetectingLockFactory.PotentialDeadlockException) {
      checkMessage(
        expected, "LockB -> ReadWriteA", "ReadWriteA -> LockB"
      )
    }
  }

  @Test
  fun testReadWriteLockDeadlock_transitive() {
    readLockA!!.lock() // Establish an ordering from readLockA -> lockB
    lockB!!.lock()
    readLockA!!.unlock()
    lockB!!.unlock()

    // Establish an ordering from lockB -> lockC
    lockB!!.lock()
    lockC!!.lock()
    lockB!!.unlock()
    lockC!!.unlock()

    // lockC -> writeLockA should fail.
    lockC!!.lock()
    try {
      writeLockA!!.lock()
      Assertions.fail<Any>("Expected PotentialDeadlockException")
    } catch (expected: CycleDetectingLockFactory.PotentialDeadlockException) {
      checkMessage(
        expected,
        "LockC -> ReadWriteA",
        "LockB -> LockC",
        "ReadWriteA -> LockB"
      )
    }
  }

  @Test
  fun testReadWriteLockDeadlock_treatedEquivalently() {
    readLockA!!.lock() // readLockA -> writeLockB
    writeLockB!!.lock()
    readLockA!!.unlock()
    writeLockB!!.unlock()

    // readLockB -> writeLockA should fail.
    readLockB!!.lock()
    try {
      writeLockA!!.lock()
      Assertions.fail<Any>("Expected PotentialDeadlockException")
    } catch (expected: CycleDetectingLockFactory.PotentialDeadlockException) {
      checkMessage(
        expected, "ReadWriteB -> ReadWriteA", "ReadWriteA -> ReadWriteB"
      )
    }
  }

  @Test
  fun testDifferentLockFactories() {
    val otherFactory = CycleDetectingLockFactory.newInstance(CycleDetectingLockFactory.Policies.WARN)
    val lockD = otherFactory.newReentrantLock("LockD")

    // lockA -> lockD
    lockA!!.lock()
    lockD.lock()
    lockA!!.unlock()
    lockD.unlock()

    // lockD -> lockA should fail even though lockD is from a different factory.
    lockD.lock()
    try {
      lockA!!.lock()
      Assertions.fail<Any>("Expected PotentialDeadlockException")
    } catch (expected: CycleDetectingLockFactory.PotentialDeadlockException) {
      checkMessage(expected, "LockD -> LockA", "LockA -> LockD")
    }
  }

  @Test
  fun testDifferentLockFactories_policyExecution() {
    val otherFactory = CycleDetectingLockFactory.newInstance(CycleDetectingLockFactory.Policies.WARN)
    val lockD = otherFactory.newReentrantLock("LockD")

    // lockD -> lockA
    lockD.lock()
    lockA!!.lock()
    lockA!!.unlock()
    lockD.unlock()

    // lockA -> lockD should warn but otherwise succeed because lockD was
    // created by a factory with the WARN policy.
    lockA!!.lock()
    lockD.lock()
  }

  @Test
  @Throws(Exception::class)
  fun testReentrantLock_tryLock() {
    val thread = LockingThread(lockA)
    thread.start()
    thread.waitUntilHoldingLock()
    Assert.assertFalse(lockA!!.tryLock())
    thread.releaseLockAndFinish()
    Assert.assertTrue(lockA!!.tryLock())
  }

  @Test
  @Throws(Exception::class)
  fun testReentrantWriteLock_tryLock() {
    val thread = LockingThread(writeLockA)
    thread.start()
    thread.waitUntilHoldingLock()
    Assert.assertFalse(writeLockA!!.tryLock())
    Assert.assertFalse(readLockA!!.tryLock())
    thread.releaseLockAndFinish()
    Assert.assertTrue(writeLockA!!.tryLock())
    Assert.assertTrue(readLockA!!.tryLock())
  }

  @Test
  @Throws(Exception::class)
  fun testReentrantReadLock_tryLock() {
    val thread = LockingThread(readLockA)
    thread.start()
    thread.waitUntilHoldingLock()
    Assert.assertFalse(writeLockA!!.tryLock())
    Assert.assertTrue(readLockA!!.tryLock())
    readLockA!!.unlock()
    thread.releaseLockAndFinish()
    Assert.assertTrue(writeLockA!!.tryLock())
    Assert.assertTrue(readLockA!!.tryLock())
  }

  private class LockingThread internal constructor(val lock: Lock?) : Thread() {
    val locked = CountDownLatch(1)
    val finishLatch = CountDownLatch(1)
    override fun run() {
      lock!!.lock()
      try {
        locked.countDown()
        finishLatch.await(1, TimeUnit.MINUTES)
      } catch (e: InterruptedException) {
        Assertions.fail<Any>(e.toString())
      } finally {
        lock.unlock()
      }
    }

    @Throws(InterruptedException::class)
    fun waitUntilHoldingLock() {
      locked.await(1, TimeUnit.MINUTES)
    }

    @Throws(InterruptedException::class)
    fun releaseLockAndFinish() {
      finishLatch.countDown()
      this.join(10000)
      Assert.assertFalse(this.isAlive)
    }
  }

  @Test
  fun testReentrantReadWriteLock_implDoesNotExposeShadowedLocks() {
    Assert.assertEquals(
      "Unexpected number of public methods in ReentrantReadWriteLock. " +
        "The correctness of CycleDetectingReentrantReadWriteLock depends on " +
        "the fact that the shadowed ReadLock and WriteLock are never used or " +
        "exposed by the superclass implementation. If the implementation has " +
        "changed, the code must be re-inspected to ensure that the " +
        "assumption is still valid.",
      24, ReentrantReadWriteLock::class.java.methods.size.toLong()
    )
  }

  private enum class MyOrder {
    FIRST, SECOND, THIRD
  }

  private enum class OtherOrder {
    FIRST, SECOND, THIRD
  }

  // Given a sequence of lock acquisition descriptions
  // (e.g. "LockA -> LockB", "LockB -> LockC", ...)
  // Checks that the exception.getMessage() matches a regex of the form:
  // "LockA -> LockB \b.*\b LockB -> LockC \b.*\b LockC -> LockA"
  private fun checkMessage(
    exception: IllegalStateException, vararg expectedLockCycle: String?,
  ) {
    val regex = Joiner.on("\\b.*\\b").join(expectedLockCycle)
    assertContainsRegex(regex, exception.message)
  }

  companion object {
    // TODO(cpovirk): consider adding support for regex to Truth
    private fun assertContainsRegex(expectedRegex: String, actual: String?) {
      val pattern = Pattern.compile(expectedRegex)
      val matcher = pattern.matcher(actual)
      if (!matcher.find()) {
        val actualDesc = if (actual == null) "null" else "<$actual>"
        Assertions.fail<Any>(
          "expected to contain regex:<" + expectedRegex + "> but was:"
            + actualDesc
        )
      }
    }
  }
}
