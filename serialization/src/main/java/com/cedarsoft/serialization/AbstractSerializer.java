package com.cedarsoft.serialization;

import com.cedarsoft.lookup.Lookup;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @param <T> the type
 * @param <S> the object to serialize to
 * @param <D> the object to deserialize from
 * @param <E> the exception that might be thrown
 */
public abstract class AbstractSerializer<T, S, D, E extends Throwable> implements ExtendedSerializer<T> {
  @NotNull
  @NonNls
  private final String defaultElementName;

  protected AbstractSerializer( @NotNull @NonNls String defaultElementName ) {
    this.defaultElementName = defaultElementName;
  }

  @NotNull
  @NonNls
  protected String getDefaultElementName() {
    return defaultElementName;
  }

  @NotNull
  public byte[] serialize( @NotNull T object ) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    serialize( object, out );
    return out.toByteArray();
  }

  public void serialize( @NotNull T object, @NotNull OutputStream out ) throws IOException {
    serialize( object, out, null );
  }

  @NotNull
  public T deserialize( @NotNull InputStream in ) throws IOException {
    return deserialize( in, null );
  }

  /**
   * Serializes the object to the given element
   *
   * @param serializeTo the serializeTo
   * @param object      the object
   * @param context     the context
   * @return the serializeTo (for fluent writing)
   */
  @NotNull
  public abstract S serialize( @NotNull S serializeTo, @NotNull T object, @NotNull Lookup context ) throws IOException, E;

  /**
   * Deserializes the object from the given document
   *
   * @param deserializeFrom the deserializeFrom
   * @param context         the context
   * @return the deserialized object
   */
  @NotNull
  public abstract T deserialize( @NotNull D deserializeFrom, @NotNull Lookup context ) throws IOException, E;
}
