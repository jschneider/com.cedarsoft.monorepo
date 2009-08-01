package com.cedarsoft.history;

import org.jetbrains.annotations.NotNull;

import java.util.EventListener;

/**
 *
 */
public interface HistoryListener<E> extends EventListener {

  /**
   * Is called when an entry has been added
   *
   * @param entry the entry that has been added
   */
  void entryAdded( @NotNull E entry );

  /**
   * Is called when an entry has been removed
   *
   * @param entry the entry that has been removed
   */
  void entryRemoved( @NotNull E entry );

  /**
   * Is called when the given entry is changed
   *
   * @param entry the entry that is changed
   */
  void entryChanged( @NotNull E entry );
}