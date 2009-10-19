package com.cedarsoft.serialization;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * @param <T> the type
 */
public abstract class AbstractSerializingStrategy<T> implements SerializingStrategy<T> {
  @NotNull
  @NonNls
  private final String id;

  @NotNull
  private final Class<? extends T> supportedType;

  protected AbstractSerializingStrategy( @NotNull String id, @NotNull Class<? extends T> supportedType ) {
    this.id = id;
    this.supportedType = supportedType;
  }

  @NotNull
  public String getId() {
    return id;
  }

  public boolean supports( @NotNull T object ) {
    return supportedType.isAssignableFrom( object.getClass() );
  }
}
