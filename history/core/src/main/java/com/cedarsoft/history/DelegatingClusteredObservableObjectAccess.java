package com.cedarsoft.history;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;

/**
 *
 */
public abstract class DelegatingClusteredObservableObjectAccess<T> implements ClusteredObservableObjectAccess<T> {
  @Override
  @NotNull
  public ReadWriteLock getLock() {
    return getDelegate().getLock();
  }

  @Override
  public void commit( @NotNull T element ) {
    getDelegate().commit( element );
  }

  @Override
  public void remove( @NotNull T element ) {
    getDelegate().remove( element );
  }

  @Override
  public void add( @NotNull T element ) {
    getDelegate().add( element );
  }

  @Override
  public void setElements( @NotNull List<? extends T> elements ) {
    getDelegate().setElements( elements );
  }

  @Override
  @NotNull
  public List<? extends T> getElements() {
    return getDelegate().getElements();
  }

  @Override
  public void addElementListener( @NotNull ElementsListener<? super T> listener ) {
    getDelegate().addElementListener( listener );
  }

  @Override
  public void addElementListener( @NotNull ElementsListener<? super T> listener, boolean isTransient ) {
    getDelegate().addElementListener( listener, isTransient );
  }

  @Override
  public void removeElementListener( @NotNull ElementsListener<? super T> listener ) {
    getDelegate().removeElementListener( listener );
  }

  @Override
  @NotNull
  public List<? extends ElementsListener<? super T>> getTransientElementListeners() {
    return getDelegate().getTransientElementListeners();
  }

  /**
   * Returns the delegate
   *
   * @return the delegate
   */
  @NotNull
  public abstract ClusteredObservableObjectAccess<T> getDelegate();
}
