package com.cedarsoft.serialization;

import com.cedarsoft.Version;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Default interface for all types of serializers.
 * Each serializer is able to serialize an object.
 * <p/>
 * A format version is supported for each serializer.
 *
 * @param <T> the type of the objects
 */
public interface Serializer<T> {
  /**
   * Serializes the object to the given output stream
   *
   * @param object the object
   * @param out    the out stream
   */
  void serialize( @NotNull T object, @NotNull OutputStream out ) throws IOException;

  /**
   * Deserializes the object from the input stream
   *
   * @param in the input stream
   * @return the deserialized object
   */
  @NotNull
  T deserialize( @NotNull InputStream in ) throws IOException;

  /**
   * Returns the format version that is written
   *
   * @return the format version that is written
   */
  @NotNull
  Version getFormatVersion();
}