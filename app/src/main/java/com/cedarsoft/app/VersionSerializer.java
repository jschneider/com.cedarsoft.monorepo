package com.cedarsoft.app;

import com.cedarsoft.lookup.Lookup;
import com.cedarsoft.serialization.jdom.AbstractJDomSerializer;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 *
 */
public class VersionSerializer extends AbstractJDomSerializer<Version> {
  public VersionSerializer() {
    super( "version" );
  }

  @NotNull
  @Override
  public Element serialize( @NotNull Element serializeTo, @NotNull Version object, @NotNull Lookup context ) throws IOException {
    serializeTo.setText( object.toString() );
    return serializeTo;
  }

  @NotNull
  @Override
  public Version deserialize( @NotNull Element deserializeFrom, @NotNull Lookup context ) throws IOException {
    return Version.parse( deserializeFrom.getTextNormalize() );
  }
}
