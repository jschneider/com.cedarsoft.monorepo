package com.cedarsoft.utils;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Offers support for change listeners.
 */
public class ChangeListenerSupport<T> {
  @NotNull
  private final ReadWriteLock lock = new ReentrantReadWriteLock();

  @NotNull
  protected final T observedObject;

  public ChangeListenerSupport( @NotNull T observedObject ) {
    this.observedObject = observedObject;
  }

  @NotNull
  protected List<ChangeListener<T>> getTransientListeners() {
    lock.readLock().lock();
    try {
      if ( transientListeners != null ) {
        //noinspection ReturnOfCollectionOrArrayField
        return transientListeners;
      }
    } finally {
      lock.readLock().unlock();
    }

    lock.writeLock().lock();
    try {
      if ( transientListeners == null ) {
        transientListeners = new ArrayList<ChangeListener<T>>();
      }
      //noinspection ReturnOfCollectionOrArrayField
      return transientListeners;
    } finally {
      lock.writeLock().unlock();
    }
  }

  private transient List<ChangeListener<T>> transientListeners;

  public void addChangeListener( @NotNull ChangeListener<T> listener ) {
    lock.writeLock().lock();
    try {
      getTransientListeners().add( listener );
    } finally {
      lock.writeLock().unlock();
    }
  }

  public void removeChangeListener( @NotNull ChangeListener<T> listener ) {
    lock.writeLock().lock();
    try {
      getTransientListeners().remove( listener );
    } finally {
      lock.writeLock().unlock();
    }
  }

  public void changed( @Nullable Object context, @NotNull @NonNls String... propertiesPath ) {
    ChangedEvent<T> event = new ChangedEvent<T>( observedObject, context, propertiesPath );

    lock.writeLock().lock();
    try {
      List<ChangeListener<T>> listeners = getTransientListeners();
      for ( ChangeListener<T> listener : listeners ) {
        listener.entryChanged( event );
      }
    } finally {
      lock.writeLock().unlock();
    }
  }

  @NotNull
  public PropertyChangeListener createPropertyListenerDelegate( @NotNull @NonNls String... propertiesPath ) {
    final String[] actual = new String[propertiesPath.length + 1];
    System.arraycopy( propertiesPath, 0, actual, 0, propertiesPath.length );

    return new PropertyChangeListener() {
      public void propertyChange( PropertyChangeEvent evt ) {
        actual[actual.length - 1] = evt.getPropertyName();
        changed( actual );
      }
    };
  }
}
