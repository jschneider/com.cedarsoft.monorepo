package com.cedarsoft.app;

import com.cedarsoft.lookup.Lookup;
import com.cedarsoft.serialization.AbstractSerializer;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 *
 */
public class VersionSerializer extends AbstractSerializer<Version> {
  public VersionSerializer() {
    super( "version" );
  }

  @NotNull
  @Override
  public Element serialize( @NotNull Element element, @NotNull Version object, @NotNull Lookup context ) throws IOException {
    element.setText( object.toString() );
    return element;
  }

  @NotNull
  @Override
  public Version deserialize( @NotNull Element element, @NotNull Lookup context ) throws IOException {
    return Version.parse( element.getTextNormalize() );
  }
}
