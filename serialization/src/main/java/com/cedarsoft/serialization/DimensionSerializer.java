package com.cedarsoft.serialization;

import com.cedarsoft.lookup.Lookup;
import com.cedarsoft.serialization.jdom.AbstractJDomSerializer;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.awt.Dimension;
import java.io.IOException;

/**
 *
 */
public class DimensionSerializer extends AbstractJDomSerializer<Dimension> {
  @NotNull
  @NonNls
  public static final String SEPARATOR = "x";

  public DimensionSerializer() {
    super( "dimension" );
  }

  @NotNull
  public Element serialize( @NonNls @NotNull Element serializeTo, @NotNull Dimension object, @NotNull Lookup context ) throws IOException {
    return serializeTo.setText( object.width + SEPARATOR + object.height );
  }

  @NotNull
  public Dimension deserialize( @NotNull Element deserializeFrom, @NotNull Lookup context ) throws IOException {
    String[] parts = deserializeFrom.getText().split( SEPARATOR );
    return new Dimension( Integer.parseInt( parts[0] ), Integer.parseInt( parts[1] ) );
  }
}
