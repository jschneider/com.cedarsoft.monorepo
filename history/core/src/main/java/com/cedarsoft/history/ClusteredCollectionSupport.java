package com.cedarsoft.history;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 *
 */
public class ClusteredCollectionSupport<E> {
  @NotNull
  private final CollectionSupport<E> transientSupport;
  @NotNull
  private final NonTransientCollectionSupport<E> nonTransientSupport;

  public ClusteredCollectionSupport( @NotNull ObservableCollection<E> source ) {
    transientSupport = new CollectionSupport<E>( source );
    nonTransientSupport = new NonTransientCollectionSupport<E>( source );
  }

  public void addElementListener( @NotNull ElementsListener<? super E> listener, boolean isTransient ) {
    if ( isTransient ) {
      transientSupport.addElementListener( listener );
    } else {
      nonTransientSupport.addEntryListener( listener );
    }
  }

  public void removeElementListener( @NotNull ElementsListener<? super E> listener ) {
    transientSupport.removeElementListener( listener );
    nonTransientSupport.removeEntryListener( listener );
  }

  public void elementDeleted( @NotNull E entry, int index ) {
    transientSupport.elementDeleted( entry, index );
    nonTransientSupport.elementDeleted( entry, index );
  }

  public void elementChanged( @NotNull E entry, int index ) {
    transientSupport.elementChanged( entry, index );
    nonTransientSupport.elementChanged( entry, index );
  }

  public void elementAdded( @NotNull E entry, int index ) {
    transientSupport.elementAdded( entry, index );
    nonTransientSupport.elementAdded( entry, index );
  }

  public boolean hasListeners() {
    return !transientSupport.getListeners().isEmpty() || nonTransientSupport.hasListeners();
  }

  @NotNull
  public List<? extends ElementsListener<? super E>> getTransientElementListeners() {
    return transientSupport.getListeners();
  }
}
