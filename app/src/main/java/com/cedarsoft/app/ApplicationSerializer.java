package com.cedarsoft.app;

import com.cedarsoft.Version;
import com.cedarsoft.lookup.Lookup;
import com.cedarsoft.serialization.stax.AbstractStaxMateSerializer;
import com.google.inject.Inject;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.staxmate.out.SMOutputElement;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.lang.Override;

/**
 *
 */
public class ApplicationSerializer extends AbstractStaxMateSerializer<Application> {
  @NotNull
  @NonNls
  private static final String ELEMENT_VERSION = "version";
  @NotNull
  @NonNls
  private static final String ELEMENT_NAME = "name";

  @NotNull
  private final VersionSerializer versionSerializer;

  @Inject
  public ApplicationSerializer( @NotNull VersionSerializer versionSerializer ) {
    super( "application" );
    this.versionSerializer = versionSerializer;
  }

  @Override
  @NotNull
  public SMOutputElement serialize( @NotNull SMOutputElement serializeTo, @NotNull Application object, @NotNull Lookup context ) throws IOException, XMLStreamException {
    serializeTo.addElement( ELEMENT_NAME ).addCharacters( object.getName() );

    SMOutputElement versionElement = serializeTo.addElement( ELEMENT_VERSION );
    versionSerializer.serialize( versionElement, object.getVersion(), context );

    return serializeTo;
  }

  @Override
  @NotNull
  public Application deserialize( @NotNull XMLStreamReader2 deserializeFrom, @NotNull Lookup context ) throws IOException, XMLStreamException {
    String name = getChildText( deserializeFrom, ELEMENT_NAME );

    nextTag( deserializeFrom, ELEMENT_VERSION );
    Version version = versionSerializer.deserialize( deserializeFrom, context );
    closeTag( deserializeFrom );

    return new Application( name, version );
  }
}
