package com.cedarsoft.serialization.stax;

import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.staxmate.out.SMOutputElement;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

/**
 * @param <T> the type this strategy serializes
 */
public interface StaxMateSerializingStrategy<T> {
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
   * @return the output element (for fluent usage)
   *
   * @throws IOException
   */
  @NotNull
  SMOutputElement serialize( @NotNull SMOutputElement element, @NotNull T object ) throws IOException, XMLStreamException;

  /**
   * Deserializes the file reference
   *
   * @param reader the reader
   * @return the file reference
   *
   * @throws IOException
   */
  @NotNull
  T deserialize( @NotNull @NonNls XMLStreamReader2 reader ) throws IOException, XMLStreamException;
}