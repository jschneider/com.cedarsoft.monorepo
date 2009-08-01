package com.cedarsoft.history;

import com.cedarsoft.Lockable;
import com.cedarsoft.ObjectAccess;
import org.jetbrains.annotations.NotNull;

/**
 * An observable collection
 */
public interface ObservableCollection<E> extends ObjectAccess<E>, Lockable {
  /**
   * Adds an entry listener
   *
   * @param listener the listener
   */
  void addElementListener( @NotNull ElementsListener<? super E> listener );

  /**
   * Removes an entry listener
   *
   * @param listener the listener
   */
  void removeElementListener( @NotNull ElementsListener<? super E> listener );
}