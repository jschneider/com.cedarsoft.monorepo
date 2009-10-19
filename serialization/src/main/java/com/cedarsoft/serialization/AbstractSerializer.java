package com.cedarsoft.serialization;

import com.cedarsoft.lookup.Lookup;
import com.cedarsoft.lookup.Lookups;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @param <T> the type
 */
public abstract class AbstractSerializer<T> implements ExtendedSerializer<T> {
  @NotNull
  @NonNls
  protected static final String LINE_SEPARATOR = "\n";

  @NotNull
  @NonNls
  private final String defaultElementName;

  protected AbstractSerializer( @NotNull @NonNls String defaultElementName ) {
    this.defaultElementName = defaultElementName;
  }

  @NotNull
  public Element serializeToElement( @NotNull T object, @Nullable Lookup context ) throws IOException {
    Element element = new Element( getDefaultElementName() );
    serialize( element, object, context != null ? context : Lookups.emtyLookup() );
    return element;
  }

  public void serialize( @NotNull T object, @NotNull OutputStream out, @Nullable Lookup context ) throws IOException {
    Element root = new Element( getDefaultElementName() );
    serialize( root, object, context != null ? context : Lookups.emtyLookup() );
    new XMLOutputter( Format.getPrettyFormat().setLineSeparator( LINE_SEPARATOR ) ).output( new Document( root ), out );
  }

  @NotNull
  @NonNls
  protected String getDefaultElementName() {
    return defaultElementName;
  }

  @NotNull
  public T deserialize( @NotNull InputStream in, @Nullable Lookup context ) throws IOException {
    try {
      return deserialize( new SAXBuilder().build( in ).getRootElement(), context != null ? context : Lookups.emtyLookup() );
    } catch ( JDOMException e ) {
      throw new IOException( "Could not parse stream due to " + e.getMessage(), e );
    }
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
   * @param element the element
   * @param object  the object
   * @param context the context
   * @return the element (for fluent writing)
   */
  @NotNull
  public abstract Element serialize( @NotNull Element element, @NotNull T object, @NotNull Lookup context ) throws IOException;

  /**
   * Deserializes the object from the diven document
   *
   * @param element the element
   * @param context the context
   * @return the deserialized object
   */
  @NotNull
  public abstract T deserialize( @NotNull Element element, @NotNull Lookup context ) throws IOException;
}
