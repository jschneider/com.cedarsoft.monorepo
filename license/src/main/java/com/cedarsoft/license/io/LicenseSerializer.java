package com.cedarsoft.license.io;

import com.cedarsoft.Version;
import com.cedarsoft.VersionRange;
import com.cedarsoft.license.License;
import com.cedarsoft.serialization.stax.AbstractStaxMateSerializingStrategy;
import org.codehaus.staxmate.out.SMOutputElement;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;

/**
 *
 */
public class LicenseSerializer extends AbstractStaxMateSerializingStrategy<License, Object> {
  @NotNull
  @NonNls
  private static final String ATTRIBUTE_ID = "id";
  @NotNull
  @NonNls
  private static final String ELEMENT_NAME = "name";

  public LicenseSerializer() {
    super( "license", License.class, new VersionRange( new Version( 1, 0, 0 ), new Version( 1, 0, 0 ) ) );
  }

  @NotNull
  @Override
  public SMOutputElement serialize( @NotNull SMOutputElement serializeTo, @NotNull License object, @Nullable Object context ) throws IOException, XMLStreamException {
    serializeTo.addAttribute( ATTRIBUTE_ID, object.getId() );
    serializeTo.addElement( ELEMENT_NAME ).addCharacters( object.getName() );
    return serializeTo;
  }

  @NotNull
  @Override
  public License deserialize( @NotNull XMLStreamReader deserializeFrom, @Nullable Object context ) throws IOException, XMLStreamException {
    String id = deserializeFrom.getAttributeValue( null, ATTRIBUTE_ID );
    String name = getChildText( deserializeFrom, ELEMENT_NAME );

    closeTag( deserializeFrom );

    return new License( id, name );
  }
}
