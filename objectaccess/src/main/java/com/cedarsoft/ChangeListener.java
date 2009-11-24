package com.cedarsoft;

import org.jetbrains.annotations.NotNull;

/**
 * Is notified when anything has changed.
 * @param <T> the type
 */
public interface ChangeListener<T> {
  /**
   * Is called when an entry has been changed
   *
   * @param event the changed event
   */
  void entryChanged( @NotNull ChangedEvent<T> event );
}
