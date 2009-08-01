package com.cedarsoft;

import org.jetbrains.annotations.NotNull;

/**
 * Supports object removals
 */
public interface ObjectRemove<T> {
  /**
   * Removes an element
   *
   * @param element the element that is removed
   * @return whether the element has been removed
   */
  void remove( @NotNull T element );
}