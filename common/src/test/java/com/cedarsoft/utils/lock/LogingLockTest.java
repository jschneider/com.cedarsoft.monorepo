package com.cedarsoft.utils.lock;

import static org.testng.Assert.*;
import org.testng.annotations.*;

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
