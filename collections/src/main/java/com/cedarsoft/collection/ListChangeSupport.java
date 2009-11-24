package com.cedarsoft.collection;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Supplies support for list change support.
 */
public class ListChangeSupport<T> {
  private List<ListChangeListener<T>> listeners = new ArrayList<ListChangeListener<T>>();

  public boolean add( @NotNull List<T> backend, @NotNull T element ) {
    int index = backend.size();
    boolean returnValue = backend.add( element );
    fireAddEvent( index, element );
    return returnValue;
  }

  public void fireAddEvent( int index, @NotNull T element ) {
    if ( listeners.isEmpty() ) return;
    for ( ListChangeListener<T> listener : new ArrayList<ListChangeListener<T>>( listeners ) ) {
      listener.elementAdded( index, element );
    }
  }

  public void fireRemoveEvent( int index, @NotNull T element ) {
    if ( listeners.isEmpty() ) return;
    for ( ListChangeListener<T> listener : new ArrayList<ListChangeListener<T>>( listeners ) ) {
      listener.elementRemoved( index, element );
    }
  }

  public boolean remove( @NotNull List<T> backend, @NotNull T element ) {
    int index = backend.indexOf( element );
    boolean returnValue = backend.remove( element );
    fireRemoveEvent( index, element );
    return returnValue;
  }

  public void addListener( @NotNull ListChangeListener<T> listener ) {
    listeners.add( listener );
  }

  public void removeListener( @NotNull ListChangeListener<T> listener ) {
    listeners.remove( listener );
  }

  @NotNull
  public List<ListChangeListener<T>> getListeners() {
    return Collections.unmodifiableList( listeners );
  }
}
