package com.cedarsoft.file;

import com.cedarsoft.Version;
import com.cedarsoft.file.Extension;
import com.cedarsoft.lookup.Lookup;
import com.cedarsoft.serialization.stax.AbstractStaxMateSerializer;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.staxmate.out.SMOutputElement;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

/**
 *
 */
public class ExtensionSerializer extends AbstractStaxMateSerializer<Extension> {
  @NotNull
  @NonNls
  private static final String ATTRIBUTE_DELIMITER = "delimiter";

  public ExtensionSerializer() {
    super( "extension", new Version( 1, 0, 0 ) );
  }

  @NotNull
  @Override
  public SMOutputElement serialize( @NotNull SMOutputElement serializeTo, @NotNull Extension object, @NotNull Lookup context ) throws IOException, XMLStreamException {
    serializeTo.addAttribute( ATTRIBUTE_DELIMITER, object.getDelimiter() );
    serializeTo.addCharacters( object.getExtension() );
    return serializeTo;
  }

  @NotNull
  @Override
  public Extension deserialize( @NotNull XMLStreamReader2 deserializeFrom, @NotNull Lookup context ) throws IOException, XMLStreamException {
    String delimiter = deserializeFrom.getAttributeValue( null, ATTRIBUTE_DELIMITER );

    deserializeFrom.next();
    String extension = deserializeFrom.getText();

    closeTag( deserializeFrom );

    return new Extension( delimiter, extension );
  }
}
