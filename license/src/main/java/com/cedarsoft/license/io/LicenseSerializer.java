package com.cedarsoft.license.io;

import com.cedarsoft.license.License;
import com.cedarsoft.lookup.Lookup;
import com.cedarsoft.serialization.stax.AbstractStaxMateSerializingStrategy;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.staxmate.out.SMOutputElement;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

/**
 *
 */
public class LicenseSerializer extends AbstractStaxMateSerializingStrategy<License> {
  @NotNull
  @NonNls
  private static final String ATTRIBUTE_ID = "id";
  @NotNull
  @NonNls
  private static final String ELEMENT_NAME = "name";

  public LicenseSerializer() {
    super( "license", License.class );
  }

  @NotNull
  @Override
  public SMOutputElement serialize( @NotNull SMOutputElement serializeTo, @NotNull License object, @NotNull Lookup context ) throws IOException, XMLStreamException {
    serializeTo.addAttribute( ATTRIBUTE_ID, object.getId() );
    serializeTo.addElement( ELEMENT_NAME ).addCharacters( object.getName() );
    return serializeTo;
  }

  @NotNull
  @Override
  public License deserialize( @NotNull XMLStreamReader2 deserializeFrom, @NotNull Lookup context ) throws IOException, XMLStreamException {
    String id = deserializeFrom.getAttributeValue( null, ATTRIBUTE_ID );
    String name = getChildText( deserializeFrom, ELEMENT_NAME );

    closeTag( deserializeFrom );

    return new License( id, name );
  }
}
