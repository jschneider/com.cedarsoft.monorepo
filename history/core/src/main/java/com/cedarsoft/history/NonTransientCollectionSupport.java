package com.cedarsoft.history;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 */
public class NonTransientCollectionSupport<E> {
  @NotNull
  private final ObservableCollection<E> source;

  @NotNull
  private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

  private final List<ElementsListener<? super E>> listeners = new ArrayList<ElementsListener<? super E>>();

  public NonTransientCollectionSupport( @NotNull ObservableCollection<E> source ) {
    this.source = source;
  }

  public void addEntryListener( @NotNull ElementsListener<? super E> listener ) {
    lock.writeLock().lock();
    try {
      listeners.add( listener );
    } finally {
      lock.writeLock().unlock();
    }
  }

  public void removeEntryListener( @NotNull ElementsListener<? super E> listener ) {
    lock.writeLock().lock();
    try {
      listeners.remove( listener );
    } finally {
      lock.writeLock().unlock();
    }
  }

  public void elementDeleted( @NotNull E element, int index ) {
    lock.readLock().lock();
    try {
      ElementsChangedEvent<E> event = new ElementsChangedEvent<E>( source, Collections.singletonList( element ), Collections.singletonList( index ) );
      for ( ElementsListener<? super E> listener : listeners ) {
        listener.elementsDeleted( event );
      }
    } finally {
      lock.readLock().unlock();
    }
  }

  public void elementChanged( @NotNull E element, int index ) {
    lock.readLock().lock();
    try {
      ElementsChangedEvent<E> event = new ElementsChangedEvent<E>( source, Collections.singletonList( element ), Collections.singletonList( index ) );
      for ( ElementsListener<? super E> listener : listeners ) {
        listener.elementsChanged( event );
      }
    } finally {
      lock.readLock().unlock();
    }
  }

  public void elementAdded( @NotNull E element, int index ) {
    lock.readLock().lock();
    try {
      ElementsChangedEvent<E> event = new ElementsChangedEvent<E>( source, Collections.singletonList( element ), Collections.singletonList( index ) );
      for ( ElementsListener<? super E> listener : listeners ) {
        listener.elementsAdded( event );
      }
    } finally {
      lock.readLock().unlock();
    }
  }

  public boolean hasListeners() {
    lock.readLock().lock();
    try {
      return !listeners.isEmpty();
    } finally {
      lock.readLock().unlock();
    }
  }
}
