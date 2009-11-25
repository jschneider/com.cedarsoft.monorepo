package com.cedarsoft;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * An Null-Lock that does nothing
 *
 * @noinspection Singleton
 */
@SuppressWarnings( {"LockAcquiredButNotSafelyReleased"} )
public class NullLock implements Lock, ReadWriteLock {
  @NotNull
  public static final NullLock LOCK = new NullLock();

  private NullLock() {
  }

  @Override
  @NotNull
  public Lock readLock() {
    return this;
  }

  @Override
  @NotNull
  public Lock writeLock() {
    return this;
  }

  @Override
  public void lock() {
  }

  @Override
  public void lockInterruptibly() throws InterruptedException {
  }

  @Override
  public boolean tryLock() {
    return true;
  }

  @Override
  public boolean tryLock( long time, TimeUnit unit ) throws InterruptedException {
    return true;
  }

  @Override
  public void unlock() {
  }

  @Override
  public Condition newCondition() {
    throw new NullPointerException( "Cannot create a condition for a null lock" );
  }
}
