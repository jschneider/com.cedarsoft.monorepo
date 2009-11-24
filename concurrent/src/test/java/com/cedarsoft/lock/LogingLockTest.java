package com.cedarsoft.lock;

import static org.testng.Assert.*;

import com.cedarsoft.lock.InvalidLockStateException;
import com.cedarsoft.lock.LogingReentrantLock;
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
