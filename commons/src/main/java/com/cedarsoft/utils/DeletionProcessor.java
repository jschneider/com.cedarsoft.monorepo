package com.cedarsoft.utils;

import org.jetbrains.annotations.NotNull;

/**
 * Will be notified whenever an object will be deleted.
 */
public interface DeletionProcessor<T> {
  /**
   * Is called whenever the object will be deleted
   *
   * @param object the object that will be deleted
   */
  void willBeDeleted( @NotNull T object );
}