package com.cedarsoft.serialization.stax;

import com.cedarsoft.Version;
import com.cedarsoft.VersionRange;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @param <T> the type
 * @param <C> the type of the context
 */
public abstract class AbstractStaxSerializer<T, C> extends AbstractStaxBasedSerializer<T, C, XMLStreamWriter> {
  protected AbstractStaxSerializer( @NotNull @NonNls String defaultElementName, @NotNull VersionRange formatVersionRange ) {
    super( defaultElementName, formatVersionRange );
  }

  @Override
  public void serialize( @NotNull T object, @NotNull OutputStream out, @Nullable C context ) throws IOException {
    try {
      XMLStreamWriter writer = StaxSupport.getXmlOutputFactory().createXMLStreamWriter( out );
      serializeFormatVersion( writer, getFormatVersion() );

      writer.writeStartElement( getDefaultElementName() );
      serialize( writer, object, context );
      writer.writeEndElement();

      writer.close();
    } catch ( XMLStreamException e ) {
      throw new IOException( e );
    }
  }

  /**
   * Serializes the format version to the given element
   *
   * @param writer        the writer
   * @param formatVersion the format version
   * @throws XMLStreamException
   */
  protected void serializeFormatVersion( @NotNull XMLStreamWriter writer, @NotNull Version formatVersion ) throws XMLStreamException {
    writer.writeProcessingInstruction( PI_TARGET_FORMAT, formatVersion.toString() );
  }
}
