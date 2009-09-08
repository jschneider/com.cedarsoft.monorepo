package com.cedarsoft.utils.serialization;

import com.cedarsoft.lookup.Lookup;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.awt.Dimension;
import java.io.IOException;

/**
 *
 */
public class DimensionSerializer extends AbstractSerializer<Dimension> {
  @NotNull
  @NonNls
  public static final String SEPARATOR = "x";

  public DimensionSerializer() {
    super( "dimension" );
  }

  @NotNull
  @Override
  public Element serialize( @NonNls @NotNull Element element, @NotNull Dimension object, @NotNull Lookup context ) throws IOException {
    return element.setText( object.width + SEPARATOR + object.height );
  }

  @NotNull
  @Override
  public Dimension deserialize( @NotNull Element element, @NotNull Lookup context ) throws IOException {
    String[] parts = element.getText().split( SEPARATOR );
    return new Dimension( Integer.parseInt( parts[0] ), Integer.parseInt( parts[1] ) );
  }
}
