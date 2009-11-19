package com.cedarsoft.utils.crypt;

import com.cedarsoft.Version;
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
public class HashSerializer extends AbstractStaxMateSerializer<Hash> {
  @NotNull
  @NonNls
  private static final String ATTRIBUTE_ALGORITHM = "algorithm";

  public HashSerializer() {
    super( "hash", new Version( 1, 0, 0 ) );
  }

  @NotNull
  @Override
  public SMOutputElement serialize( @NotNull SMOutputElement serializeTo, @NotNull Hash object, @NotNull Lookup context ) throws IOException, XMLStreamException {
    serializeTo.addAttribute( ATTRIBUTE_ALGORITHM, object.getAlgorithm().name() );
    serializeTo.addCharacters( object.getValueAsHex() );
    return serializeTo;
  }

  @NotNull
  @Override
  public Hash deserialize( @NotNull XMLStreamReader2 deserializeFrom, @NotNull Lookup context ) throws IOException, XMLStreamException {
    String algorithm = deserializeFrom.getAttributeValue( null, ATTRIBUTE_ALGORITHM );
    String valueAsHex = getText( deserializeFrom );

    return Hash.fromHex( Algorithm.valueOf( algorithm ), valueAsHex );
  }
}
