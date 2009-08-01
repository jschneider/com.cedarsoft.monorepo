package com.cedarsoft.utils.workflow;

import static org.testng.Assert.*;
import org.testng.annotations.*;

import java.util.concurrent.locks.ReentrantLock;

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
