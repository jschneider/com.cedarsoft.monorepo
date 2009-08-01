package com.cedarsoft.history;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 *
 */
public interface ClusteredObservableCollection<E> extends ObservableCollection<E> {
  /**
   * Adds an element listener
   *
   * @param listener    the listener
   * @param isTransient whether this listener is added transient or not
   */
  void addElementListener( @NotNull ElementsListener<? super E> listener, boolean isTransient );

  /**
   * Returns the transient element listeners
   *
   * @return the transient element listeners
   */
  @NotNull
  List<? extends ElementsListener<? super E>> getTransientElementListeners();
}