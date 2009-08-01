package com.cedarsoft.history;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 */
public class NonTransientHistoryListenerSupport<E> {
  @NotNull
  private List<HistoryListener<E>> listeners = new ArrayList<HistoryListener<E>>();

  @NotNull
  private final ReadWriteLock lock = new ReentrantReadWriteLock();

  public void addHistoryListener( @NotNull HistoryListener<E> historyListener ) {
    lock.writeLock().lock();
    try {
      listeners.add( historyListener );
    } finally {
      lock.writeLock().unlock();
    }
  }

  public void removeHistoryListener( @NotNull HistoryListener<E> historyListener ) {
    lock.writeLock().lock();
    try {
      listeners.remove( historyListener );
    } finally {
      lock.writeLock().unlock();
    }
  }

  public void notifyEntryChanged( @NotNull E entry ) {
    lock.readLock().lock();
    List<HistoryListener<E>> copy;
    try {
      copy = new ArrayList<HistoryListener<E>>( listeners );
    } finally {
      lock.readLock().unlock();
    }
    for ( HistoryListener<E> listener : copy ) {
      listener.entryChanged( entry );
    }
  }

  public void notifyEntryAdded( @NotNull E entry ) {
    lock.readLock().lock();
    List<HistoryListener<E>> copy;
    try {
      copy = new ArrayList<HistoryListener<E>>( listeners );
    } finally {
      lock.readLock().unlock();
    }
    for ( HistoryListener<E> listener : copy ) {
      listener.entryAdded( entry );
    }
  }

  public void notifyEntryRemoved( @NotNull E entry ) {
    lock.readLock().lock();
    List<HistoryListener<E>> copy;
    try {
      copy = new ArrayList<HistoryListener<E>>( listeners );
    } finally {
      lock.readLock().unlock();
    }
    for ( HistoryListener<E> listener : copy ) {
      listener.entryRemoved( entry );
    }
  }
}
