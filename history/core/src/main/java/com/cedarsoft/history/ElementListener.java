package com.cedarsoft.history;

import org.jetbrains.annotations.NotNull;

/**
 *
 */
public interface ElementListener<E> {
  /**
   * Is called when an entry has been deleted
   *
   * @param element the entry that has been deleted
   */
  void elementDeleted( @NotNull E element );

  /**
   * Is called when an entry has been added
   *
   * @param element the entry that has been added
   */
  void elementAdded( @NotNull E element );

  /**
   * Is called when an enty has been changed
   *
   * @param element the entry that has been changed
   */
  void elementChanged( @NotNull E element );
}