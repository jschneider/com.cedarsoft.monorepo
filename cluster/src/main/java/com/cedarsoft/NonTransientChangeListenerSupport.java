package com.cedarsoft;

import com.cedarsoft.utils.ChangeListener;
import com.cedarsoft.utils.ChangedEvent;
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
 *
 */
public class NonTransientChangeListenerSupport<T> {
  @NotNull
  private final ReadWriteLock lock = new ReentrantReadWriteLock();

  @NotNull
  protected final T observedObject;

  @NotNull
  private final List<ChangeListener<T>> transientListeners = new ArrayList<ChangeListener<T>>();

  public NonTransientChangeListenerSupport( @NotNull T observedObject ) {
    this.observedObject = observedObject;
  }

  public void addChangeListener( @NotNull ChangeListener<T> listener ) {
    lock.writeLock().lock();
    try {
      transientListeners.add( listener );
    } finally {
      lock.writeLock().unlock();
    }
  }

  public void removeChangeListener( @NotNull ChangeListener<T> listener ) {
    lock.writeLock().lock();
    try {
      transientListeners.remove( listener );
    } finally {
      lock.writeLock().unlock();
    }
  }

  public void changed( @Nullable Object context, @NotNull String... propertiesPath ) {
    ChangedEvent<T> event = new ChangedEvent<T>( observedObject, context, propertiesPath );

    lock.readLock().lock();
    try {
      for ( ChangeListener<T> listener : transientListeners ) {
        listener.entryChanged( event );
      }
    } finally {
      lock.readLock().unlock();
    }
  }

  @NotNull
  public PropertyChangeListener createPropertyListenerDelegate( @NotNull @NonNls String... propertiesPath ) {
    final String[] actual = new String[propertiesPath.length + 1];
    System.arraycopy( propertiesPath, 0, actual, 0, propertiesPath.length );

    return new PropertyChangeListener() {
      @Override
      public void propertyChange( PropertyChangeEvent evt ) {
        actual[actual.length - 1] = evt.getPropertyName();
        changed( actual );
      }
    };
  }
}
