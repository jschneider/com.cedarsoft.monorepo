package com.cedarsoft.utils.serialization;

import com.cedarsoft.lookup.Lookup;
import org.apache.commons.lang.StringUtils;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.awt.Dimension;
import java.io.IOException;

/**
 *
 */
public class DimensionSerializer extends AbstractSerializer<Dimension> {
  public static final String SEPARATOR = "x";

  public DimensionSerializer() {
    super( "dimension" );
  }

  @Override
  public void serialize( @NonNls @NotNull Element element, @NotNull Dimension object, @NotNull Lookup context ) throws IOException {
    element.setText( object.width + SEPARATOR + object.height );
  }

  @NotNull
  @Override
  public Dimension deserialize( @NotNull Element element, @NotNull Lookup context ) throws IOException {
    String[] parts = StringUtils.split( element.getText(), SEPARATOR );
    return new Dimension( Integer.parseInt( parts[0] ), Integer.parseInt( parts[1] ) );
  }
}
