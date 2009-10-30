package com.cedarsoft.serialization.jdom;

import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @param <T> the type
 */
public interface JDomSerializingStrategy<T> {
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
   * @param element the element
   * @param object  the object
   * @throws IOException
   */
  void serialize( @NotNull Element element, @NotNull T object ) throws IOException;

  /**
   * Deserializes the file reference
   *
   * @param element the element
   * @return the file reference
   *
   * @throws IOException
   */
  @NotNull
  T deserialize( @NotNull @NonNls Element element ) throws IOException;
}