package com.cedarsoft.utils;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * A mapped cache.
 * If there is no entry for a given key, the entry is automatically created using a factory.
 *
 * @param <K> the key
 * @param <T> the type that is stored within the cache
 */
public interface Cache<K, T> extends Map<K, T> {

  /**
   * A factory that is used to fill the cache
   */
  interface Factory<K, T> {
    /**
     * Create the object for the given key
     *
     * @param key the key
     * @return the object
     */
    @NotNull
    T create( @NotNull K key );
  }
}
