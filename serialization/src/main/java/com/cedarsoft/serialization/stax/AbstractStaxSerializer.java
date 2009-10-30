package com.cedarsoft.serialization.stax;

import com.cedarsoft.lookup.Lookup;
import com.cedarsoft.lookup.Lookups;
import com.cedarsoft.serialization.ExtendedSerializer;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.staxmate.SMInputFactory;
import org.codehaus.staxmate.SMOutputFactory;
import org.codehaus.staxmate.out.SMOutputDocument;
import org.codehaus.staxmate.out.SMOutputElement;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;

/**
 * @param <T> the type
 */
public abstract class AbstractStaxSerializer<T> implements ExtendedSerializer<T> {
  @NotNull
  @NonNls
  private final String defaultElementName;

  protected AbstractStaxSerializer( @NotNull @NonNls String defaultElementName ) {
    this.defaultElementName = defaultElementName;
  }

  public void serialize( @NotNull T object, @NotNull OutputStream out, @Nullable Lookup context ) throws IOException {
    try {
      SMOutputDocument doc = new SMOutputFactory( XMLOutputFactory.newInstance() ).createOutputDocument( out );
      //    doc.setIndentation( "\n  ", 1, 2 );
      SMOutputElement root = doc.addElement( getDefaultElementName() );
      serialize( root, object, context != null ? context : Lookups.emtyLookup() );
      doc.closeRoot();
    } catch ( XMLStreamException e ) {
      throw new IOException( e );
    }
  }

  @NotNull
  @NonNls
  protected String getDefaultElementName() {
    return defaultElementName;
  }

  @NotNull
  public T deserialize( @NotNull InputStream in, @Nullable Lookup context ) throws IOException {
    try {
      XMLStreamReader2 reader = new SMInputFactory( XMLInputFactory.newInstance() ).createStax2Reader( in );
      return deserialize( reader, context != null ? context : Lookups.emtyLookup() );
    } catch ( XMLStreamException e ) {
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
   * @return the given element (for fluent writing)
   */
  @NotNull
  public abstract SMOutputElement serialize( @NotNull SMOutputElement element, @NotNull T object, @NotNull Lookup context ) throws IOException, XMLStreamException;

  /**
   * Deserializes the object from the given document
   *
   * @param reader  the reader for the document
   * @param context the context
   * @return the deserialized object
   */
  @NotNull
  public abstract T deserialize( @NotNull XMLStreamReader2 reader, @NotNull Lookup context ) throws IOException, XMLStreamException;
}
