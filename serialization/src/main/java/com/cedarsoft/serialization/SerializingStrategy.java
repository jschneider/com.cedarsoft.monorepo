package com.cedarsoft.serialization;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @param <T> the type for this strategy
 * @param <S> the serializing object
 * @param <D> the deserializing object
 */
public interface SerializingStrategy<T, S, D> {
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

  /**
   * Serializes the reference
   *
   * @param serializeTo the serializeTo
   * @param object      the object
   * @return the output serializeTo (for fluent usage)
   *
   * @throws IOException
   */
  @NotNull
  S serialize( @NotNull S serializeTo, @NotNull T object ) throws IOException;

  /**
   * Deserializes the file reference
   *
   * @param deserializeFrom the deserializeFrom
   * @return the file reference
   *
   * @throws IOException
   */
  @NotNull
  T deserialize( @NotNull @NonNls D deserializeFrom ) throws IOException;
}
