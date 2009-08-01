package com.cedarsoft.history;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 */
public class HistoryListenerSupport<E> {
  @NotNull
  private final ReadWriteLock lock = new ReentrantReadWriteLock();

  public void addHistoryListener( @NotNull HistoryListener<E> historyListener ) {
    lock.writeLock().lock();
    try {
      getListeners().add( historyListener );
    } finally {
      lock.writeLock().unlock();
    }
  }

  public void removeHistoryListener( @NotNull HistoryListener<E> historyListener ) {
    lock.writeLock().lock();
    try {
      getListeners().remove( historyListener );
    } finally {
      lock.writeLock().unlock();
    }
  }

  public void notifyEntryChanged( @NotNull E entry ) {
    lock.writeLock().lock();
    List<HistoryListener<E>> copy;
    try {
      copy = new ArrayList<HistoryListener<E>>( getListeners() );
    } finally {
      lock.writeLock().unlock();
    }
    for ( HistoryListener<E> listener : copy ) {
      listener.entryChanged( entry );
    }
  }

  public void notifyEntryAdded( @NotNull E entry ) {
    lock.writeLock().lock();
    List<HistoryListener<E>> copy;
    try {
      copy = new ArrayList<HistoryListener<E>>( getListeners() );
    } finally {
      lock.writeLock().unlock();
    }
    for ( HistoryListener<E> listener : copy ) {
      listener.entryAdded( entry );
    }
  }

  public void notifyEntryRemoved( @NotNull E entry ) {
    lock.writeLock().lock();
    List<HistoryListener<E>> copy;
    try {
      copy = new ArrayList<HistoryListener<E>>( getListeners() );
    } finally {
      lock.writeLock().unlock();
    }
    for ( HistoryListener<E> listener : copy ) {
      listener.entryRemoved( entry );
    }
  }

  private transient List<HistoryListener<E>> listeners;

  @NotNull
  private List<HistoryListener<E>> getListeners() {
    lock.writeLock().lock();
    try {
      if ( listeners == null ) {
        listeners = new ArrayList<HistoryListener<E>>();
      }
      return listeners;
    } finally {
      lock.writeLock().unlock();
    }
  }

  @NotNull
  public List<? extends HistoryListener<E>> getHistoryListeners() {
    return Collections.unmodifiableList( getListeners() );
  }
}
