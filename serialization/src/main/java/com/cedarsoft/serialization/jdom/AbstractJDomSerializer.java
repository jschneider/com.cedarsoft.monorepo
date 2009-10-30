package com.cedarsoft.serialization.jdom;

import com.cedarsoft.lookup.Lookup;
import com.cedarsoft.lookup.Lookups;
import com.cedarsoft.serialization.AbstractSerializer;
import com.cedarsoft.serialization.ExtendedSerializer;
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
 * Abstract serializer based on JDom
 *
 * @param <T> the type
 */
public abstract class AbstractJDomSerializer<T> extends AbstractSerializer<T, Element, Element, IOException> {
  @NotNull
  @NonNls
  protected static final String LINE_SEPARATOR = "\n";

  protected AbstractJDomSerializer( @NotNull @NonNls String defaultElementName ) {
    super( defaultElementName );
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
  public T deserialize( @NotNull InputStream in, @Nullable Lookup context ) throws IOException {
    try {
      return deserialize( new SAXBuilder().build( in ).getRootElement(), context != null ? context : Lookups.emtyLookup() );
    } catch ( JDOMException e ) {
      throw new IOException( "Could not parse stream due to " + e.getMessage(), e );
    }
  }
}
