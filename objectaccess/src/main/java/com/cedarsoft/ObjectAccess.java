package com.cedarsoft;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Implementing classes offer access to a list of elements
 *
 * @param <T> the type
 */
public interface ObjectAccess<T> {
  /**
   * Returns all elements
   *
   * @return all elements
   */
  @NotNull
  List<? extends T> getElements();
}