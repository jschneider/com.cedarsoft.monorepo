package com.cedarsoft.concurrent;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.*;

import com.google.common.base.Joiner;
import com.google.common.util.concurrent.CycleDetectingLockFactory;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@Ignore
public class CycleDetectingLockFactoryDemo {
  private ReentrantLock lockA;
  private ReentrantLock lockB;
  private ReentrantLock lockC;
  private ReentrantReadWriteLock.ReadLock readLockA;
  private ReentrantReadWriteLock.ReadLock readLockB;
  private ReentrantReadWriteLock.ReadLock readLockC;
  private ReentrantReadWriteLock.WriteLock writeLockA;
  private ReentrantReadWriteLock.WriteLock writeLockB;
  private ReentrantReadWriteLock.WriteLock writeLockC;
  private ReentrantLock lock1;
  private ReentrantLock lock2;
  private ReentrantLock lock3;
  private ReentrantLock lock01;
  private ReentrantLock lock02;
  private ReentrantLock lock03;

  @Before
  public void setUp() throws Exception {
    CycleDetectingLockFactory factory = CycleDetectingLockFactory.newInstance(CycleDetectingLockFactory.Policies.THROW);
    lockA = factory.newReentrantLock("LockA");
    lockB = factory.newReentrantLock("LockB");
    lockC = factory.newReentrantLock("LockC");

    ReentrantReadWriteLock readWriteLockA = factory.newReentrantReadWriteLock("ReadWriteA");
    ReentrantReadWriteLock readWriteLockB = factory.newReentrantReadWriteLock("ReadWriteB");
    ReentrantReadWriteLock readWriteLockC = factory.newReentrantReadWriteLock("ReadWriteC");

    readLockA = readWriteLockA.readLock();
    readLockB = readWriteLockB.readLock();
    readLockC = readWriteLockC.readLock();

    writeLockA = readWriteLockA.writeLock();
    writeLockB = readWriteLockB.writeLock();
    writeLockC = readWriteLockC.writeLock();
  }

  @Test
  public void testDeadlock_twoLocks() {
    // Establish an acquisition order of lockA -> lockB.
    lockA.lock();
    lockB.lock();
    lockA.unlock();
    lockB.unlock();

    // The opposite order should fail (Policies.THROW).
    CycleDetectingLockFactory.PotentialDeadlockException firstException = null;
    lockB.lock();
    try {
      lockA.lock();
      fail("Expected PotentialDeadlockException");
    } catch (CycleDetectingLockFactory.PotentialDeadlockException expected) {
      checkMessage(expected, "LockB -> LockA", "LockA -> LockB");
      firstException = expected;
    }

    // Second time should also fail, with a cached causal chain.
    try {
      lockA.lock();
      fail("Expected PotentialDeadlockException");
    } catch (CycleDetectingLockFactory.PotentialDeadlockException expected) {
      checkMessage(expected, "LockB -> LockA", "LockA -> LockB");
      // The causal chain should be cached.
      assertSame(firstException.getCause(), expected.getCause());
    }

    // lockA should work after lockB is released.
    lockB.unlock();
    lockA.lock();
  }

  @Test
  // Tests transitive deadlock detection.
  public void testDeadlock_threeLocks() {
    // Establish an ordering from lockA -> lockB.
    lockA.lock();
    lockB.lock();
    lockB.unlock();
    lockA.unlock();

    // Establish an ordering from lockB -> lockC.
    lockB.lock();
    lockC.lock();
    lockB.unlock();

    // lockC -> lockA should fail.
    try {
      lockA.lock();
      fail("Expected PotentialDeadlockException");
    } catch (CycleDetectingLockFactory.PotentialDeadlockException expected) {
      checkMessage(
        expected, "LockC -> LockA", "LockB -> LockC", "LockA -> LockB");
    }
  }

  @Test
  public void testReentrancy_noDeadlock() {
    lockA.lock();
    lockB.lock();
    lockA.lock();  // Should not assert on lockB -> reentrant(lockA)
  }

  @Test
  public void testExplicitOrdering_noViolations() {
    lock1.lock();
    lock3.lock();
    lock3.unlock();
    lock2.lock();
    lock3.lock();
  }

  @Test
  public void testExplicitOrdering_violations() {
    lock3.lock();
    try {
      lock2.lock();
      fail("Expected PotentialDeadlockException");
    } catch (CycleDetectingLockFactory.PotentialDeadlockException expected) {
      checkMessage(expected, "MyOrder.THIRD -> MyOrder.SECOND");
    }

    try {
      lock1.lock();
      fail("Expected PotentialDeadlockException");
    } catch (CycleDetectingLockFactory.PotentialDeadlockException expected) {
      checkMessage(expected, "MyOrder.THIRD -> MyOrder.FIRST");
    }

    lock3.unlock();
    lock2.lock();

    try {
      lock1.lock();
      fail("Expected PotentialDeadlockException");
    } catch (CycleDetectingLockFactory.PotentialDeadlockException expected) {
      checkMessage(expected, "MyOrder.SECOND -> MyOrder.FIRST");
    }
  }

  @Test
  public void testDifferentOrderings_noViolations() {
    lock3.lock();   // MyOrder, ordinal() == 3
    lock01.lock();  // OtherOrder, ordinal() == 1
  }

  @Test
  public void testExplicitOrderings_generalCycleDetection() {
    lock3.lock();   // MyOrder, ordinal() == 3
    lock01.lock();  // OtherOrder, ordinal() == 1

    lock3.unlock();
    try {
      lock3.lock();
      fail("Expected PotentialDeadlockException");
    } catch (CycleDetectingLockFactory.PotentialDeadlockException expected) {
      checkMessage(
        expected,
        "OtherOrder.FIRST -> MyOrder.THIRD",
        "MyOrder.THIRD -> OtherOrder.FIRST");
    }

    lockA.lock();
    lock01.unlock();
    lockB.lock();
    lockA.unlock();

    try {
      lock01.lock();
      fail("Expected PotentialDeadlockException");
    } catch (CycleDetectingLockFactory.PotentialDeadlockException expected) {
      checkMessage(
        expected,
        "LockB -> OtherOrder.FIRST",
        "LockA -> LockB",
        "OtherOrder.FIRST -> LockA");
    }
  }

  @Test
  public void testExplicitOrdering_cycleWithUnorderedLock() {
    Lock myLock = CycleDetectingLockFactory.newInstance(CycleDetectingLockFactory.Policies.THROW)
      .newReentrantLock("MyLock");
    lock03.lock();
    myLock.lock();
    lock03.unlock();

    try {
      lock01.lock();
      fail("Expected PotentialDeadlockException");
    } catch (CycleDetectingLockFactory.PotentialDeadlockException expected) {
      checkMessage(
        expected,
        "MyLock -> OtherOrder.FIRST",
        "OtherOrder.THIRD -> MyLock",
        "OtherOrder.FIRST -> OtherOrder.THIRD");
    }
  }

  @Test
  public void testReadLock_deadlock() {
    readLockA.lock();  // Establish an ordering from readLockA -> lockB.
    lockB.lock();
    lockB.unlock();
    readLockA.unlock();

    lockB.lock();
    try {
      readLockA.lock();
      fail("Expected PotentialDeadlockException");
    } catch (CycleDetectingLockFactory.PotentialDeadlockException expected) {
      checkMessage(expected, "LockB -> ReadWriteA", "ReadWriteA -> LockB");
    }
  }

  @Test
  public void testReadLock_transitive() {
    readLockA.lock();  // Establish an ordering from readLockA -> lockB.
    lockB.lock();
    lockB.unlock();
    readLockA.unlock();

    // Establish an ordering from lockB -> readLockC.
    lockB.lock();
    readLockC.lock();
    lockB.unlock();
    readLockC.unlock();

    // readLockC -> readLockA
    readLockC.lock();
    try {
      readLockA.lock();
      fail("Expected PotentialDeadlockException");
    } catch (CycleDetectingLockFactory.PotentialDeadlockException expected) {
      checkMessage(
        expected,
        "ReadWriteC -> ReadWriteA",
        "LockB -> ReadWriteC",
        "ReadWriteA -> LockB");
    }
  }

  @Test
  public void testWriteLock_threeLockDeadLock() {
    // Establish an ordering from writeLockA -> writeLockB.
    writeLockA.lock();
    writeLockB.lock();
    writeLockB.unlock();
    writeLockA.unlock();

    // Establish an ordering from writeLockB -> writeLockC.
    writeLockB.lock();
    writeLockC.lock();
    writeLockB.unlock();

    // writeLockC -> writeLockA should fail.
    try {
      writeLockA.lock();
      fail("Expected PotentialDeadlockException");
    } catch (CycleDetectingLockFactory.PotentialDeadlockException expected) {
      checkMessage(
        expected,
        "ReadWriteC -> ReadWriteA",
        "ReadWriteB -> ReadWriteC",
        "ReadWriteA -> ReadWriteB");
    }
  }

  @Test
  public void testWriteToReadLockDowngrading() {
    writeLockA.lock();  // writeLockA downgrades to readLockA
    readLockA.lock();
    writeLockA.unlock();

    lockB.lock();  // readLockA -> lockB
    readLockA.unlock();

    // lockB -> writeLockA should fail
    try {
      writeLockA.lock();
      fail("Expected PotentialDeadlockException");
    } catch (CycleDetectingLockFactory.PotentialDeadlockException expected) {
      checkMessage(
        expected, "LockB -> ReadWriteA", "ReadWriteA -> LockB");
    }
  }

  @Test
  public void testReadWriteLockDeadlock() {
    writeLockA.lock();  // Establish an ordering from writeLockA -> lockB
    lockB.lock();
    writeLockA.unlock();
    lockB.unlock();

    // lockB -> readLockA should fail.
    lockB.lock();
    try {
      readLockA.lock();
      fail("Expected PotentialDeadlockException");
    } catch (CycleDetectingLockFactory.PotentialDeadlockException expected) {
      checkMessage(
        expected, "LockB -> ReadWriteA", "ReadWriteA -> LockB");
    }
  }

  @Test
  public void testReadWriteLockDeadlock_transitive() {
    readLockA.lock();  // Establish an ordering from readLockA -> lockB
    lockB.lock();
    readLockA.unlock();
    lockB.unlock();

    // Establish an ordering from lockB -> lockC
    lockB.lock();
    lockC.lock();
    lockB.unlock();
    lockC.unlock();

    // lockC -> writeLockA should fail.
    lockC.lock();
    try {
      writeLockA.lock();
      fail("Expected PotentialDeadlockException");
    } catch (CycleDetectingLockFactory.PotentialDeadlockException expected) {
      checkMessage(
        expected,
        "LockC -> ReadWriteA",
        "LockB -> LockC",
        "ReadWriteA -> LockB");
    }
  }

  @Test
  public void testReadWriteLockDeadlock_treatedEquivalently() {
    readLockA.lock();  // readLockA -> writeLockB
    writeLockB.lock();
    readLockA.unlock();
    writeLockB.unlock();

    // readLockB -> writeLockA should fail.
    readLockB.lock();
    try {
      writeLockA.lock();
      fail("Expected PotentialDeadlockException");
    } catch (CycleDetectingLockFactory.PotentialDeadlockException expected) {
      checkMessage(
        expected, "ReadWriteB -> ReadWriteA", "ReadWriteA -> ReadWriteB");
    }
  }

  @Test
  public void testDifferentLockFactories() {
    CycleDetectingLockFactory otherFactory = CycleDetectingLockFactory.newInstance(CycleDetectingLockFactory.Policies.WARN);
    ReentrantLock lockD = otherFactory.newReentrantLock("LockD");

    // lockA -> lockD
    lockA.lock();
    lockD.lock();
    lockA.unlock();
    lockD.unlock();

    // lockD -> lockA should fail even though lockD is from a different factory.
    lockD.lock();
    try {
      lockA.lock();
      fail("Expected PotentialDeadlockException");
    } catch (CycleDetectingLockFactory.PotentialDeadlockException expected) {
      checkMessage(expected, "LockD -> LockA", "LockA -> LockD");
    }
  }

  @Test
  public void testDifferentLockFactories_policyExecution() {
    CycleDetectingLockFactory otherFactory =
      CycleDetectingLockFactory.newInstance(CycleDetectingLockFactory.Policies.WARN);
    ReentrantLock lockD = otherFactory.newReentrantLock("LockD");

    // lockD -> lockA
    lockD.lock();
    lockA.lock();
    lockA.unlock();
    lockD.unlock();

    // lockA -> lockD should warn but otherwise succeed because lockD was
    // created by a factory with the WARN policy.
    lockA.lock();
    lockD.lock();
  }

  @Test
  public void testReentrantLock_tryLock() throws Exception {
    LockingThread thread = new LockingThread(lockA);
    thread.start();

    thread.waitUntilHoldingLock();
    assertFalse(lockA.tryLock());

    thread.releaseLockAndFinish();
    assertTrue(lockA.tryLock());
  }

  @Test
  public void testReentrantWriteLock_tryLock() throws Exception {
    LockingThread thread = new LockingThread(writeLockA);
    thread.start();

    thread.waitUntilHoldingLock();
    assertFalse(writeLockA.tryLock());
    assertFalse(readLockA.tryLock());

    thread.releaseLockAndFinish();
    assertTrue(writeLockA.tryLock());
    assertTrue(readLockA.tryLock());
  }

  @Test
  public void testReentrantReadLock_tryLock() throws Exception {
    LockingThread thread = new LockingThread(readLockA);
    thread.start();

    thread.waitUntilHoldingLock();
    assertFalse(writeLockA.tryLock());
    assertTrue(readLockA.tryLock());
    readLockA.unlock();

    thread.releaseLockAndFinish();
    assertTrue(writeLockA.tryLock());
    assertTrue(readLockA.tryLock());
  }

  private static class LockingThread extends Thread {
    final CountDownLatch locked = new CountDownLatch(1);
    final CountDownLatch finishLatch = new CountDownLatch(1);
    final Lock lock;

    LockingThread(Lock lock) {
      this.lock = lock;
    }

    @Override
    public void run() {
      lock.lock();
      try {
        locked.countDown();
        finishLatch.await(1, TimeUnit.MINUTES);
      } catch (InterruptedException e) {
        fail(e.toString());
      } finally {
        lock.unlock();
      }
    }

    void waitUntilHoldingLock() throws InterruptedException {
      locked.await(1, TimeUnit.MINUTES);
    }

    void releaseLockAndFinish() throws InterruptedException {
      finishLatch.countDown();
      this.join(10000);
      assertFalse(this.isAlive());
    }
  }

  public void testReentrantReadWriteLock_implDoesNotExposeShadowedLocks() {
    assertEquals(
      "Unexpected number of public methods in ReentrantReadWriteLock. " +
        "The correctness of CycleDetectingReentrantReadWriteLock depends on " +
        "the fact that the shadowed ReadLock and WriteLock are never used or " +
        "exposed by the superclass implementation. If the implementation has " +
        "changed, the code must be re-inspected to ensure that the " +
        "assumption is still valid.",
      24, ReentrantReadWriteLock.class.getMethods().length);
  }

  private enum MyOrder {
    FIRST, SECOND, THIRD;
  }

  private enum OtherOrder {
    FIRST, SECOND, THIRD;
  }

  // Given a sequence of lock acquisition descriptions
  // (e.g. "LockA -> LockB", "LockB -> LockC", ...)
  // Checks that the exception.getMessage() matches a regex of the form:
  // "LockA -> LockB \b.*\b LockB -> LockC \b.*\b LockC -> LockA"
  private void checkMessage(
    IllegalStateException exception, String... expectedLockCycle) {
    String regex = Joiner.on("\\b.*\\b").join(expectedLockCycle);
    assertContainsRegex(regex, exception.getMessage());
  }

  // TODO(cpovirk): consider adding support for regex to Truth
  private static void assertContainsRegex(String expectedRegex, String actual) {
    Pattern pattern = Pattern.compile(expectedRegex);
    Matcher matcher = pattern.matcher(actual);
    if (!matcher.find()) {
      String actualDesc = (actual == null) ? "null" : ('<' + actual + '>');
      fail("expected to contain regex:<" + expectedRegex + "> but was:"
             + actualDesc);
    }
  }
}