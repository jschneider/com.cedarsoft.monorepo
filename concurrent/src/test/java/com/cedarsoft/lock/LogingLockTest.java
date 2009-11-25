package com.cedarsoft.lock;

import org.testng.annotations.*;

import static org.testng.Assert.*;

/**
 *
 */
public class LogingLockTest {
  @Test
  public void testIt() {
    LogingReentrantLock lock = new LogingReentrantLock();
    lock.readLock().lock();
    try {
      lock.writeLock().lock();
      fail( "Where is the Exception" );
    } catch ( InvalidLockStateException ignore ) {
    }
  }
}
