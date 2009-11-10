package com.cedarsoft.provider;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a provider.
 *
 * @param <T> the type that is provided
 * @param <C> the context
 * @param <E> the exception that is thrown
 */
public interface ContextualProvider<T, C, E extends Throwable> {
  /**
   * Provides the object
   *
   * @param context the context
   * @return the object that is provided
   */
  @NotNull
  T provide( @NotNull C context ) throws E;
}