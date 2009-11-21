package com.cedarsoft.serialization;

import com.cedarsoft.lookup.Lookup;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Serializer with several convenience methods.
 *
 * @param <T> the type
 */
public interface ExtendedSerializer<T> extends Serializer<T, Lookup> {
  /**
   * Serializes the given object
   *
   * @param object the object
   * @return the serialized object
   *
   * @throws IOException
   */
  @NotNull
  byte[] serialize( @NotNull T object ) throws IOException;

  /**
   * Serializes the object to the output stream
   *
   * @param object the object
   * @param out    the out stream
   * @throws IOException
   */
  void serialize( @NotNull T object, @NotNull OutputStream out ) throws IOException;

  /**
   * Deserializes the object
   *
   * @param in the input stream
   * @return the deserialized object
   *
   * @throws IOException
   */
  @NotNull
  T deserialize( @NotNull InputStream in ) throws IOException;
}