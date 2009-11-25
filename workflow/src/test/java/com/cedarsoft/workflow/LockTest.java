package com.cedarsoft.workflow;

import org.testng.annotations.*;

import java.util.concurrent.locks.ReentrantLock;

import static org.testng.Assert.*;

/**
 *
 */
public class LockTest {
  @Test
  public void testIt() {
    ReentrantLock lock = new ReentrantLock();

    try {
      lock.lock();
      assertEquals( 1, lock.getHoldCount() );

      lock.lock();
      assertEquals( 2, lock.getHoldCount() );
      lock.unlock();
      assertEquals( 1, lock.getHoldCount() );

    } finally {
      lock.unlock();
    }
  }

}
