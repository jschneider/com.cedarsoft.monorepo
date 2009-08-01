package com.cedarsoft.history;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;

/**
 *
 */
public abstract class DelegatingClusteredObservableObjectAccess<T> implements ClusteredObservableObjectAccess<T> {
  @NotNull
  public ReadWriteLock getLock() {
    return getDelegate().getLock();
  }

  public void commit( @NotNull T element ) {
    getDelegate().commit( element );
  }

  public void remove( @NotNull T element ) {
    getDelegate().remove( element );
  }

  public void add( @NotNull T element ) {
    getDelegate().add( element );
  }

  public void setElements( @NotNull List<? extends T> elements ) {
    getDelegate().setElements( elements );
  }

  @NotNull
  public List<? extends T> getElements() {
    return getDelegate().getElements();
  }

  public void addElementListener( @NotNull ElementsListener<? super T> listener ) {
    getDelegate().addElementListener( listener );
  }

  public void addElementListener( @NotNull ElementsListener<? super T> listener, boolean isTransient ) {
    getDelegate().addElementListener( listener, isTransient );
  }

  public void removeElementListener( @NotNull ElementsListener<? super T> listener ) {
    getDelegate().removeElementListener( listener );
  }

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
