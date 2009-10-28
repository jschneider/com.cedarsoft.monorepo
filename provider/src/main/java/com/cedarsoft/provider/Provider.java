package com.cedarsoft.provider;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a provider.
 *
 * @param <T> the type that is provided
 * @param <E> the exception that is thrown
 */
public interface Provider<T, E extends Throwable> {
  /**
   * Provides the object
   *
   * @return the object that is provided
   */
  @NotNull
  T provide() throws E;

  @NotNull
  @NonNls
  String getDescription();
}