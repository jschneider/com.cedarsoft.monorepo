package com.cedarsoft.serialization;

import com.cedarsoft.Version;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Serializes objects
 *
 * @param <T> the type of the objects
 * @param <C> the type of the context
 */
public interface Serializer<T, C> {
  @NotNull
  @NonNls
  String PI_TARGET_FORMAT = "format";

  /**
   * Serializes the object to the given output stream
   *
   * @param object  the object
   * @param out     the out stream
   * @param context the context (optional)
   */
  void serialize( @NotNull T object, @NotNull OutputStream out, @Nullable C context ) throws IOException;

  /**
   * Deserializes the object from the input stream
   *
   * @param in      the input stream
   * @param context the context (optional)
   * @return the deserialized object
   */
  @NotNull
  T deserialize( @NotNull InputStream in, @Nullable C context ) throws IOException;

  @NotNull
  Version getFormatVersion();
}