package com.cedarsoft.concurrent;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.junit.*;

import com.google.common.util.concurrent.CycleDetectingLockFactory;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class SimpleCycleDetectingLockDemo {
  @Test
  @Ignore
  public void testName() throws Exception {
    CycleDetectingLockFactory lockFactory = CycleDetectingLockFactory.newInstance(new CycleDetectingLockFactory.Policy() {
      @Override
      public void handlePotentialDeadlock(CycleDetectingLockFactory.PotentialDeadlockException exception) {
        exception.printStackTrace();
      }
    });

    ReentrantReadWriteLock lock2 = lockFactory.newReentrantReadWriteLock("lock2");
    ReentrantReadWriteLock lock1 = lockFactory.newReentrantReadWriteLock("lock1");

    lock2.readLock().lock();
    lock1.readLock().lock();
    lock1.readLock().unlock();
    lock2.readLock().unlock();


    lock1.readLock().lock();
    lock2.readLock().lock();

    lock2.readLock().unlock();
    lock1.readLock().unlock();
  }
}
