package com.cedarsoft.serialization;

import com.cedarsoft.lookup.Lookup;
import com.cedarsoft.serialization.jdom.AbstractJDomSerializer;
import com.cedarsoft.serialization.stax.AbstractStaxMateSerializer;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.staxmate.out.SMOutputElement;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.xml.stream.XMLStreamException;
import java.awt.Dimension;
import java.io.IOException;

/**
 *
 */
public class DimensionSerializer extends AbstractStaxMateSerializer<Dimension> {
  @NotNull
  @NonNls
  public static final String SEPARATOR = "x";

  public DimensionSerializer() {
    super( "dimension" );
  }

  @NotNull
  public SMOutputElement serialize( @NotNull SMOutputElement serializeTo, @NotNull Dimension object, @NotNull Lookup context ) throws IOException, XMLStreamException {
    serializeTo.addCharacters( object.width + SEPARATOR + object.height );
    return serializeTo;
  }

  @NotNull
  public Dimension deserialize( @NotNull XMLStreamReader2 deserializeFrom, @NotNull Lookup context ) throws IOException, XMLStreamException {
    deserializeFrom.next();
    String[] parts = deserializeFrom.getText().split( SEPARATOR );
    return new Dimension( Integer.parseInt( parts[0] ), Integer.parseInt( parts[1] ) );
  }
}
