package com.cedarsoft.registry;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

/**
 * A factory that creates a registry
 *
 * @param <T> the type contained within a registry
 * @param <R> the type for the registry
 */
public interface RegistryFactory<T, R extends Registry<T>> {
  /**
   * Creates a registry with the given initial objects
   *
   * @param objects    the initial objects
   * @param comparator the comparator that must be used for the created registry
   * @return the newly created registry
   */
  @NotNull
  R createRegistry( @NotNull List<? extends T> objects, @NotNull Comparator<T> comparator );
}
