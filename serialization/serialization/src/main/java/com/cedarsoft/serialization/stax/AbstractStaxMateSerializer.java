package com.cedarsoft.serialization.stax;

import com.cedarsoft.Version;
import com.cedarsoft.VersionRange;
import org.codehaus.staxmate.out.SMOutputContainer;
import org.codehaus.staxmate.out.SMOutputDocument;
import org.codehaus.staxmate.out.SMOutputElement;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @param <T> the type
 * @param <C> the type of the context
 */
public abstract class AbstractStaxMateSerializer<T, C> extends AbstractStaxBasedSerializer<T, C, SMOutputElement> {
  protected AbstractStaxMateSerializer( @NotNull @NonNls String defaultElementName, @NotNull VersionRange formatVersionRange ) {
    super( defaultElementName, formatVersionRange );
  }

  @Override
  public void serialize( @NotNull T object, @NotNull OutputStream out, @Nullable C context ) throws IOException {
    try {
      SMOutputDocument doc = StaxSupport.getSmOutputFactory().createOutputDocument( out );
      serializeFormatVersion( doc, getFormatVersion() );

      SMOutputElement root = doc.addElement( getDefaultElementName() );
      serialize( root, object, context );
      doc.closeRoot();
    } catch ( XMLStreamException e ) {
      throw new IOException( e );
    }
  }

  /**
   * Serializes the format version to the given element
   *
   * @param element       the element
   * @param formatVersion the format version
   * @throws XMLStreamException
   */
  protected void serializeFormatVersion( @NotNull SMOutputContainer element, @NotNull Version formatVersion ) throws XMLStreamException {
    element.addProcessingInstruction( PI_TARGET_FORMAT, formatVersion.toString() );
  }
}
