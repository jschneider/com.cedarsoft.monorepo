package com.cedarsoft.serialization;

import com.cedarsoft.Version;
import com.cedarsoft.VersionRange;
import com.cedarsoft.serialization.stax.AbstractStaxMateSerializer;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.staxmate.out.SMOutputElement;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.stream.XMLStreamException;
import java.awt.Dimension;
import java.io.IOException;

/**
 *
 */
public class DimensionSerializer extends AbstractStaxMateSerializer<Dimension, Object> {
  @NotNull
  @NonNls
  public static final String SEPARATOR = "x";

  public DimensionSerializer() {
    super( "dimension", new VersionRange( new Version( 1, 0, 0 ), new Version( 1, 0, 0 ) ) );
  }

  @Override
  @NotNull
  public SMOutputElement serialize( @NotNull SMOutputElement serializeTo, @NotNull Dimension object, @Nullable Object context ) throws IOException, XMLStreamException {
    serializeTo.addCharacters( object.width + SEPARATOR + object.height );
    return serializeTo;
  }

  @Override
  @NotNull
  public Dimension deserialize( @NotNull XMLStreamReader2 deserializeFrom, @Nullable Object context ) throws IOException, XMLStreamException {
    String[] parts = getText( deserializeFrom ).split( SEPARATOR );

    return new Dimension( Integer.parseInt( parts[0] ), Integer.parseInt( parts[1] ) );
  }
}
