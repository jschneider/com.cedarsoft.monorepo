package com.cedarsoft.app;

import com.cedarsoft.lookup.Lookup;
import com.cedarsoft.serialization.jdom.AbstractJDomSerializer;
import com.google.inject.Inject;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 *
 */
public class ApplicationSerializer extends AbstractJDomSerializer<Application> {
  @NotNull
  @NonNls
  private static final String ELEMENT_VERSION = "version";
  @NotNull
  private final VersionSerializer versionSerializer;
  private static final String ELEMENT_NAME = "name";

  @Inject
  public ApplicationSerializer( @NotNull VersionSerializer versionSerializer ) {
    super( "application" );
    this.versionSerializer = versionSerializer;
  }

  @NotNull
  public Element serialize( @NotNull Element serializeTo, @NotNull Application object, @NotNull Lookup context ) throws IOException {
    serializeTo.setContent( new Element( ELEMENT_NAME ).setText( object.getName() ) );
    serializeTo.addContent( versionSerializer.serialize( new Element( ELEMENT_VERSION ), object.getVersion(), context ) );
    return serializeTo;
  }

  @NotNull
  public Application deserialize( @NotNull Element deserializeFrom, @NotNull Lookup context ) throws IOException {
    return new Application( deserializeFrom.getChild( ELEMENT_NAME ).getTextNormalize(), versionSerializer.deserialize( deserializeFrom.getChild( ELEMENT_VERSION ), context ) );
  }
}
