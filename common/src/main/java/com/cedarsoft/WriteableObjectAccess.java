package com.cedarsoft;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Offers simple methods for accessing a list of methods.
 * Implementations may use a database or some sort of memory
 *
 * @param <T> the type
 */
public interface WriteableObjectAccess<T> extends ObjectAccess<T>, ObjectAddRemove<T> {
  /**
   * Sets the elements
   * This method removes all old values if not contained in the given elements and adds all new elements.
   * After this method has called {@link #getElements()} will return the given elements.
   *
   * @param elements the elements
   * @throws UnsupportedOperationException if the method is not supported
   */
  void setElements( @NotNull List<? extends T> elements ) throws UnsupportedOperationException;
}