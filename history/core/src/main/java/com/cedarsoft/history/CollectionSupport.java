package com.cedarsoft.history;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 */
public class CollectionSupport<E> {
  @NotNull
  private final ObservableCollection<E> source;

  @NotNull
  private final ReentrantReadWriteLock.WriteLock writeLock = new ReentrantReadWriteLock().writeLock();

  public CollectionSupport( @NotNull ObservableCollection<E> source ) {
    this.source = source;
  }

  @NotNull
  protected List<ElementsListener<? super E>> getListeners() {
    writeLock.lock();
    try {
      if ( listeners == null ) {
        listeners = new ArrayList<ElementsListener<? super E>>();
      }
      //noinspection ReturnOfCollectionOrArrayField
      return listeners;
    } finally {
      writeLock.unlock();
    }
  }

  private transient List<ElementsListener<? super E>> listeners;

  public void addElementListener( @NotNull ElementsListener<? super E> listener ) {
    writeLock.lock();
    try {
      getListeners().add( listener );
    } finally {
      writeLock.unlock();
    }
  }

  public void removeElementListener( @NotNull ElementsListener<? super E> listener ) {
    writeLock.lock();
    try {
      getListeners().remove( listener );
    } finally {
      writeLock.unlock();
    }
  }

  public void elementDeleted( @NotNull E element, int index ) {
    //noinspection TooBroadScope
    List<ElementsListener<? super E>> elementsListeners;
    writeLock.lock();
    try {
      elementsListeners = new ArrayList<ElementsListener<? super E>>( getListeners() );
    } finally {
      writeLock.unlock();
    }

    ElementsChangedEvent<E> event = new ElementsChangedEvent<E>( source, Collections.singletonList( element ), Collections.singletonList( index ) );

    for ( ElementsListener<? super E> listener : elementsListeners ) {
      listener.elementsDeleted( event );
    }
  }

  public void elementChanged( @NotNull E element, int index ) {
    //noinspection TooBroadScope
    List<ElementsListener<? super E>> elementsListeners;
    writeLock.lock();
    try {
      elementsListeners = new ArrayList<ElementsListener<? super E>>( getListeners() );
    } finally {
      writeLock.unlock();
    }

    ElementsChangedEvent<E> event = new ElementsChangedEvent<E>( source, Collections.singletonList( element ), Collections.singletonList( index ) );
    for ( ElementsListener<? super E> listener : elementsListeners ) {
      listener.elementsChanged( event );
    }
  }

  public void elementAdded( @NotNull E element, int index ) {
    //noinspection TooBroadScope
    List<ElementsListener<? super E>> elementsListeners;
    writeLock.lock();
    try {
      elementsListeners = new ArrayList<ElementsListener<? super E>>( getListeners() );
    } finally {
      writeLock.unlock();
    }

    ElementsChangedEvent<E> event = new ElementsChangedEvent<E>( source, Collections.singletonList( element ), Collections.singletonList( index ) );
    for ( ElementsListener<? super E> listener : elementsListeners ) {
      listener.elementsAdded( event );
    }
  }

  public boolean hasListeners() {
    writeLock.lock();
    try {
      return listeners != null && !listeners.isEmpty();
    } finally {
      writeLock.unlock();
    }
  }
}
