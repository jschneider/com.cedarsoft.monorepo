package com.cedarsoft.utils;

import org.jetbrains.annotations.NotNull;

/**
 * Is notified when anything has changed.
 */
public interface ChangeListener<T> {
  /**
   * Is called when an entry has been changed
   *
   * @param event the changed event
   */
  void entryChanged( @NotNull ChangedEvent<T> event );
}
