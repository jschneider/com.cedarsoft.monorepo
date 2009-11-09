package com.cedarsoft.utils.lock;

import org.jetbrains.annotations.NotNull;

import java.lang.Override;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

/**
 *
 */
public class MultiReadWriteLock implements ReadWriteLock {
  @NotNull
  private final MultiLock multiReadLock;
  @NotNull
  private final MultiLock multiWriteLock;

  public MultiReadWriteLock( @NotNull ReadWriteLock... locks ) {
    this( Arrays.asList( locks ) );
  }

  public MultiReadWriteLock( @NotNull List<? extends ReadWriteLock> locks ) {
    List<Lock> readLocks = new ArrayList<Lock>();
    List<Lock> writeLocks = new ArrayList<Lock>();

    for ( ReadWriteLock lock : locks ) {
      readLocks.add( lock.readLock() );
      writeLocks.add( lock.writeLock() );
    }

    multiReadLock = new MultiLock( readLocks );
    multiWriteLock = new MultiLock( writeLocks );
  }


  @Override
  @NotNull
  public Lock readLock() {
    return multiReadLock;
  }

  @Override
  @NotNull
  public Lock writeLock() {
    return multiWriteLock;
  }
}
