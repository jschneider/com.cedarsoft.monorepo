package com.cedarsoft.history;

import org.jetbrains.annotations.NotNull;

/**
 * Can be registered to be notified about updates.
 * There exist two types of "added", "changed" and "deleted" methods.
 *
 * @param <E> the type of the elements
 */
public interface ElementsListener<E> {
  /**
   * Is notified when the elements have been deleted
   *
   * @param event
   */
  void elementsDeleted( @NotNull ElementsChangedEvent<? extends E> event );

  /**
   * The elements are added
   *
   * @param event
   */
  void elementsAdded( @NotNull ElementsChangedEvent<? extends E> event );

  /**
   * Elements have been changed
   *
   * @param event
   */
  void elementsChanged( @NotNull ElementsChangedEvent<? extends E> event );
}