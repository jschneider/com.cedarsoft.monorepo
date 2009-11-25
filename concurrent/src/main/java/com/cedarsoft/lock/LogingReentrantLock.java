package com.cedarsoft.lock;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 */
public class LogingReentrantLock implements ReadWriteLock {
  @NotNull
  private final ReadWriteLock delegate = new ReentrantReadWriteLock();

  @NotNull
  private final List<Thread> readLockingThreads = new ArrayList<Thread>();
  private final List<Thread> writeLockingThreads = new ArrayList<Thread>();

  @NotNull
  private final LogingLock readLock = new LogingLock( delegate.readLock(), readLockingThreads );
  @NotNull
  private final LogingLock writeLock = new LogingLock( delegate.writeLock(), writeLockingThreads ) {
    @Override
    public void lock() {
      if ( !readLockingThreads.isEmpty() ) {
        throw new InvalidLockStateException( readLockingThreads );
      }
      super.lock();
    }
  };


  @Override
  public Lock readLock() {
    return readLock;
  }

  @Override
  public Lock writeLock() {
    return writeLock;
  }

  private static class DelegatingLock implements Lock {
    private final Lock delegate;

    protected DelegatingLock( @NotNull Lock delegate ) {
      this.delegate = delegate;
    }

    @Override
    public void lock() {
      delegate.lock();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
      delegate.lockInterruptibly();
    }

    @Override
    public boolean tryLock() {
      return delegate.tryLock();
    }

    @Override
    public boolean tryLock( long time, TimeUnit unit ) throws InterruptedException {
      return delegate.tryLock( time, unit );
    }

    @Override
    public void unlock() {
      delegate.unlock();
    }

    @Override
    public Condition newCondition() {
      return delegate.newCondition();
    }
  }

  private static class LogingLock extends DelegatingLock {
    @NotNull
    private final List<Thread> lockingThreads;

    private LogingLock( @NotNull Lock delegate, @NotNull List<Thread> lockingThreads ) {
      super( delegate );
      this.lockingThreads = lockingThreads;
    }

    @Override
    public void lock() {
      lockingThreads.add( 0, Thread.currentThread() );
      super.lock();
    }

    @Override
    public void unlock() {
      lockingThreads.remove( Thread.currentThread() );
      super.unlock();
    }

    @NotNull
    public List<? extends Thread> getLockingThreads() {
      return Collections.unmodifiableList( lockingThreads );
    }
  }
}
