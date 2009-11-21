package com.cedarsoft.serialization;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 * A pluggable serializer that can be used to stack several serializers together
 *
 * @param <T> the type
 * @param <C> the type of the context
 * @param <S> the object to serialize to
 * @param <D> the object to deserialize from
 * @param <E> the exception that might be thrown
 */
public interface PluggableSerializer<T, C, S, D, E extends Throwable> extends ExtendedSerializer<T, C> {
  /**
   * Serializes the object to the given element
   *
   * @param serializeTo the serializeTo
   * @param object      the object
   * @param context     the context
   * @return the serializeTo (for fluent writing)
   */
  @NotNull
  S serialize( @NotNull S serializeTo, @NotNull T object, @Nullable C context ) throws IOException, E;

  /**
   * Deserializes the object from the given document
   *
   * @param deserializeFrom the deserializeFrom
   * @param context         the context
   * @return the deserialized object
   */
  @NotNull
  T deserialize( @NotNull D deserializeFrom, @Nullable C context ) throws IOException, E;
}