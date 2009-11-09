package com.cedarsoft.serialization.stax;

import com.cedarsoft.lookup.Lookup;
import com.cedarsoft.lookup.Lookups;
import com.cedarsoft.serialization.AbstractSerializer;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.staxmate.out.SMOutputDocument;
import org.codehaus.staxmate.out.SMOutputElement;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

  @Override
  public void serialize( @NotNull T object, @NotNull OutputStream out, @Nullable Lookup context ) throws IOException {
    try {
      SMOutputDocument doc = StaxSupport.getSmOutputFactory().createOutputDocument( out );
      //    doc.setIndentation( "\n  ", 1, 2 );
      SMOutputElement root = doc.addElement( getDefaultElementName() );
      serialize( root, object, context != null ? context : Lookups.emtyLookup() );
      doc.closeRoot();
    } catch ( XMLStreamException e ) {
      throw new IOException( e );
    }
  }

  @Override
  @NotNull
  public T deserialize( @NotNull InputStream in, @Nullable Lookup context ) throws IOException {
    try {
      XMLStreamReader2 reader = StaxSupport.getSmInputFactory().createStax2Reader( in );
      reader.nextTag();
      T deserialized = deserialize( reader, context != null ? context : Lookups.emtyLookup() );

      if ( !reader.isEndElement() ) {
        throw new IllegalStateException( "Not consumed everything in <" + getClass().getName() + ">" );
      }

      if ( reader.next() != XMLStreamReader2.END_DOCUMENT ) {
        throw new IllegalStateException( "Not consumed everything in <" + getClass().getName() + ">" );
      }

      return deserialized;
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

  /**
   * Returns the text and closes the tag
   *
   * @param reader the reader
   * @return the text
   *
   * @throws XMLStreamException
   */
  @NotNull
  protected String getText( @NotNull XMLStreamReader reader ) throws XMLStreamException {
    StringBuilder content = new StringBuilder();

    int result;
    while ( ( result = reader.next() ) != XMLStreamReader.END_ELEMENT ) {
      if ( result != XMLStreamReader.CHARACTERS ) {
        throw new IllegalStateException( "Invalid state: " + result );
      }
      content.append( reader.getText() );
    }

    return content.toString();
  }

  @NotNull
  protected String getChildText( @NotNull XMLStreamReader reader, @NotNull @NonNls String tagName ) throws XMLStreamException {
    reader.nextTag();
    ensureTag( reader, tagName );
    return getText( reader );
  }

  protected void closeTag( @NotNull XMLStreamReader reader ) throws XMLStreamException {
    int result = reader.nextTag();
    if ( result != XMLStreamReader.END_ELEMENT ) {
      throw new IllegalStateException( "Invalid result: " + result );
    }
  }

  protected void nextTag( @NotNull XMLStreamReader reader, @NotNull @NonNls String tagName ) throws XMLStreamException {
    int result = reader.nextTag();
    if ( result != XMLStreamReader.START_ELEMENT ) {
      throw new IllegalStateException( "Invalid result: " + result );
    }
    ensureTag( reader, tagName );
  }

  /**
   * Attention! The current element will be closed!
   *
   * @param streamReader the stream reader
   * @param callback     the callback
   * @throws XMLStreamException
   * @throws IOException
   */
  protected void visitChildren( @NotNull XMLStreamReader2 streamReader, @NotNull CB callback ) throws XMLStreamException, IOException {
    while ( streamReader.nextTag() != XMLStreamReader.END_ELEMENT ) {
      String tagName = streamReader.getName().getLocalPart();
      callback.tagEntered( streamReader, tagName );
    }
  }

  public interface CB {
    /**
     * Is called for each child.
     * ATTENTION: This method *must* close the tag
     *
     * @param deserializeFrom the reader
     * @param tagName         the tag name
     * @throws XMLStreamException
     * @throws IOException
     */
    void tagEntered( @NotNull XMLStreamReader2 deserializeFrom, @NotNull @NonNls String tagName ) throws XMLStreamException, IOException;
  }
}
