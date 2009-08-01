package com.cedarsoft;

import org.jetbrains.annotations.NotNull;

/**
 * Supports object addition
 */
public interface ObjectAdd<T> {
  /**
   * Adds an element
   *
   * @param element the element that is added
   */
  void add( @NotNull T element );
}