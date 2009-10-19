package com.cedarsoft.app;

import com.cedarsoft.collustra.processing.Application;
import com.cedarsoft.lookup.Lookup;
import com.cedarsoft.serialization.AbstractSerializer;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 *
 */
public class ApplicationSerializer extends AbstractSerializer<Application> {
  @NotNull
  @NonNls
  private static final String ELEMENT_VERSION = "version";
  @NotNull
  private final VersionSerializer versionSerializer;
  private static final String ELEMENT_NAME = "name";

  public ApplicationSerializer( @NotNull VersionSerializer versionSerializer ) {
    super( "application" );
    this.versionSerializer = versionSerializer;
  }

  @NotNull
  @Override
  public Element serialize( @NotNull Element element, @NotNull Application object, @NotNull Lookup context ) throws IOException {
    element.setContent( new Element( ELEMENT_NAME ).setText( object.getName() ) );
    element.addContent( versionSerializer.serialize( new Element( ELEMENT_VERSION ), object.getVersion(), context ) );
    return element;
  }

  @NotNull
  @Override
  public Application deserialize( @NotNull Element element, @NotNull Lookup context ) throws IOException {
    return new Application( element.getChild( ELEMENT_NAME ).getTextNormalize(), versionSerializer.deserialize( element.getChild( ELEMENT_VERSION ), context ) );
  }
}
