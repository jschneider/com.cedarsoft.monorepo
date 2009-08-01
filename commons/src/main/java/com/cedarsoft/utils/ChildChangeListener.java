package com.cedarsoft.utils;

import org.jetbrains.annotations.NotNull;

/**
 * Listener that may be registered at a parent
 */
public interface ChildChangeListener<P> {
  /**
   * Is called when the parent has changed
   *
   * @param parent the parent
   */
  void notifyChildrenChangedFor( @NotNull P parent );
}
