package com.cedarsoft.history;

import com.cedarsoft.utils.event.ClusteredPropertyChangeSupport;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.joda.time.LocalDate;

import java.beans.PropertyChangeListener;
import java.lang.Override;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * An entry within a history.
 * Every entry spans an interval.
 */
public class DefaultContinuousEntry implements ContinuousEntry {
  @NotNull
  protected ReadWriteLock lock = new ReentrantReadWriteLock();

  @NotNull
  protected final ClusteredPropertyChangeSupport pcs = new ClusteredPropertyChangeSupport( this );

  private Long id;

  @NotNull
  private LocalDate begin;

  /**
   * Hibernate
   */
  @Deprecated
  public DefaultContinuousEntry() {
  }

  public DefaultContinuousEntry( @NotNull LocalDate begin ) {
    this.begin = begin;
  }

  @Override
  @NotNull
  public LocalDate getBegin() {
    lock.readLock().lock();
    try {
      return begin;
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public void setBegin( @NotNull LocalDate begin ) {
    lock.writeLock().lock();
    try {
      pcs.firePropertyChange( PROPERTY_BEGIN, this.begin, this.begin = begin );
    } finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public int compareTo( ContinuousEntry o ) {
    return getBegin().compareTo( o.getBegin() );
  }

  public void removePropertyChangeListener( @NotNull PropertyChangeListener listener ) {
    lock.writeLock().lock();
    try {
      pcs.removePropertyChangeListener( listener );
    } finally {
      lock.writeLock().unlock();
    }
  }

  public void removePropertyChangeListener( @NotNull @NonNls String propertyName, @NotNull PropertyChangeListener listener ) {
    lock.writeLock().lock();
    try {
      pcs.removePropertyChangeListener( propertyName, listener );
    } finally {
      lock.writeLock().unlock();
    }
  }

  public void addPropertyChangeListener( @NotNull PropertyChangeListener listener ) {
    lock.writeLock().lock();
    try {
      pcs.addPropertyChangeListener( listener );
    } finally {
      lock.writeLock().unlock();
    }
  }

  public void addPropertyChangeListener( @NotNull PropertyChangeListener listener, boolean isTransient ) {
    lock.writeLock().lock();
    try {
      pcs.addPropertyChangeListener( listener, isTransient );
    } finally {
      lock.writeLock().unlock();
    }
  }

  public void addPropertyChangeListener( @NotNull @NonNls String propertyName, @NotNull PropertyChangeListener listener ) {
    lock.writeLock().lock();
    try {
      pcs.addPropertyChangeListener( propertyName, listener );
    } finally {
      lock.writeLock().unlock();
    }
  }

  public void addPropertyChangeListener( @NotNull @NonNls String propertyName, @NotNull PropertyChangeListener listener, boolean isTransient ) {
    lock.writeLock().lock();
    try {
      pcs.addPropertyChangeListener( propertyName, listener, isTransient );
    } finally {
      lock.writeLock().unlock();
    }
  }
}
