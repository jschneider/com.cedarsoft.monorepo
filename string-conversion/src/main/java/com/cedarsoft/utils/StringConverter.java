package com.cedarsoft.utils;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * A string converter is able to convert objects to string representations and vice versa.
 */
public interface StringConverter<T> {
  /**
   * Create a string representation for an object
   *
   * @param object the object that is serialized
   * @return the string representation
   */
  @NotNull
  @NonNls
  String createRepresentation( @NotNull T object );

  /**
   * Deserializes an string
   *
   * @param representation the string representation
   * @return the deserialized object
   */
  @NotNull
  T createObject( @NonNls @NotNull String representation );
}
