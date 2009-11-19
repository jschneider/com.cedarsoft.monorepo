package com.cedarsoft.serialization;

import com.cedarsoft.Version;
import com.cedarsoft.lookup.Lookup;
import com.cedarsoft.serialization.stax.AbstractStaxMateSerializer;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.staxmate.out.SMOutputElement;
import org.jetbrains.annotations.NotNull;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.lang.Override;

/**
 *
 */
public class VersionSerializer extends AbstractStaxMateSerializer<Version> {
  public VersionSerializer() {
    super( "version", new Version( 1, 0, 0 ) );
  }

  @Override
  @NotNull
  public SMOutputElement serialize( @NotNull SMOutputElement serializeTo, @NotNull Version object, @NotNull Lookup context ) throws IOException, XMLStreamException {
    serializeTo.addCharacters( object.toString() );
    return serializeTo;
  }

  @Override
  @NotNull
  public Version deserialize( @NotNull XMLStreamReader2 deserializeFrom, @NotNull Lookup context ) throws IOException, XMLStreamException {
    String text = getText( deserializeFrom );
    return Version.parse( text );
  }
}
