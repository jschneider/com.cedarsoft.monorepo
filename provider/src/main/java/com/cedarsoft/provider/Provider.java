package com.cedarsoft.provider;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a provider
 */
public interface Provider<T> {
  /**
   * Provides the object
   *
   * @return the object that is provided
   */
  @NotNull
  T provide();
}