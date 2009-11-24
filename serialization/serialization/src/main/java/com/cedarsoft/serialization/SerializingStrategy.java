package com.cedarsoft.serialization;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * @param <T> the type for this strategy
 * @param <C> the type of the context
 * @param <S> the serializing object
 * @param <D> the deserializing object
 * @param <E> the exception that might be thrown
 */
public interface SerializingStrategy<T, C, S, D, E extends Throwable> extends PluggableSerializer<T, C, S, D, E> {

  /**
   * Returns the id
   *
   * @return the id
   */
  @NotNull
  @NonNls
  String getId();

  /**
   * Whether the given reference type is supported
   *
   * @param object the reference
   * @return true if this strategy supports the reference, false otherwise
   */
  boolean supports( @NotNull Object object );
}
