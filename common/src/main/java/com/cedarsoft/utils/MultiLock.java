package com.cedarsoft.utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * USE WITH CARE!!!
 * May easily result in dead locks
 */
public class MultiLock implements Lock {
  @NotNull
  private final List<Lock> locks = new ArrayList<Lock>();

  public MultiLock( @NotNull Lock... locks ) {
    this( Arrays.asList( locks ) );
  }

  public MultiLock( @NotNull Collection<? extends Lock> locks ) {
    this.locks.addAll( locks );
  }

  public void lock() {
    for ( Lock lock : locks ) {
      //noinspection LockAcquiredButNotSafelyReleased
      lock.lock();
    }
  }

  public void lockInterruptibly() throws InterruptedException {
    throw new UnsupportedOperationException( "Not supported for a multi lock" );
  }

  public boolean tryLock() {
    throw new UnsupportedOperationException( "Not supported for a multi lock" );
  }

  public boolean tryLock( long time, TimeUnit unit ) throws InterruptedException {
    throw new UnsupportedOperationException( "Not supported for a multi lock" );
  }

  public void unlock() {
    for ( Lock lock : locks ) {
      lock.unlock();
    }
  }

  public Condition newCondition() {
    throw new UnsupportedOperationException( "Cannot create a condition for a multi lock" );
  }
}
