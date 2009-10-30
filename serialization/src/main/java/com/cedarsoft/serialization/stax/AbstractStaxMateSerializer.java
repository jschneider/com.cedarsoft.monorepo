package com.cedarsoft.serialization.stax;

import com.cedarsoft.lookup.Lookup;
import com.cedarsoft.lookup.Lookups;
import com.cedarsoft.serialization.AbstractSerializer;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.staxmate.SMInputFactory;
import org.codehaus.staxmate.SMOutputFactory;
import org.codehaus.staxmate.out.SMOutputDocument;
import org.codehaus.staxmate.out.SMOutputElement;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @param <T> the type
 */
public abstract class AbstractStaxMateSerializer<T> extends AbstractSerializer<T, SMOutputElement, XMLStreamReader2, XMLStreamException> {
  protected AbstractStaxMateSerializer( @NotNull @NonNls String defaultElementName ) {
    super( defaultElementName );
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
  public T deserialize( @NotNull InputStream in, @Nullable Lookup context ) throws IOException {
    try {
      XMLStreamReader2 reader = new SMInputFactory( XMLInputFactory.newInstance() ).createStax2Reader( in );
      reader.nextTag();
      return deserialize( reader, context != null ? context : Lookups.emtyLookup() );
    } catch ( XMLStreamException e ) {
      throw new IOException( "Could not parse stream due to " + e.getMessage(), e );
    }
  }

  protected void ensureTag( @NotNull XMLStreamReader deserializeFrom, @NotNull @NonNls String tagName ) {
    String current = deserializeFrom.getName().getLocalPart();
    if ( !current.equals( tagName ) ) {
      throw new IllegalStateException( "Invalid tag. Was <" + current + "> but expected <" + tagName + ">" );
    }
  }

  @NotNull
  protected String getText( @NotNull XMLStreamReader deserializeFrom ) throws XMLStreamException {
    int result = deserializeFrom.next();
    if ( result != XMLStreamReader2.CHARACTERS ) {
      throw new IllegalStateException( "Invalid state: " + result );
    }

    return deserializeFrom.getText();
  }

  @NotNull
  protected String getChildText( @NotNull XMLStreamReader deserializeFrom, @NotNull @NonNls String tagName ) throws XMLStreamException {
    deserializeFrom.nextTag();
    ensureTag( deserializeFrom, tagName );
    String name = getText( deserializeFrom );
    closeTag( deserializeFrom );
    return name;
  }

  protected void closeTag( @NotNull XMLStreamReader deserializeFrom ) throws XMLStreamException {
    int result = deserializeFrom.nextTag();
    if ( result != XMLStreamReader.END_ELEMENT ) {
      throw new IllegalStateException( "Invalid result: " + result );
    }
  }

  protected void nextTag( @NotNull XMLStreamReader deserializeFrom, @NotNull @NonNls String tagName ) throws XMLStreamException {
    int result = deserializeFrom.nextTag();
    if ( result != XMLStreamReader.START_ELEMENT ) {
      throw new IllegalStateException( "Invalid result: " + result );
    }
    ensureTag( deserializeFrom, tagName );
  }
}
