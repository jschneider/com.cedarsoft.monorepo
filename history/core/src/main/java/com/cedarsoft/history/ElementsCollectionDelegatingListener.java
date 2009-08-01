package com.cedarsoft.history;

import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class ElementsCollectionDelegatingListener<T> extends SingleElementsListener<T> {
  @NotNull
  private final ObservableObjectAccess<T> delegate;

  public ElementsCollectionDelegatingListener( @NotNull ObservableObjectAccess<T> delegate ) {
    this.delegate = delegate;
  }

  @Override
  public void elementDeleted( @NotNull ObservableCollection<? extends T> source, @NotNull T element, int index ) {
    delegate.remove( element );
  }

  @Override
  public void elementAdded( @NotNull ObservableCollection<? extends T> source, @NotNull T element, int index ) {
    delegate.add( element );
  }

  @Override
  public void elementChanged( @NotNull ObservableCollection<? extends T> source, @NotNull T element, int index ) {
    delegate.commit( element );
  }
}
