package com.cedarsoft.serialization;

import com.cedarsoft.Version;
import com.cedarsoft.VersionRange;
import com.cedarsoft.serialization.stax.AbstractStaxMateSerializer;
import org.codehaus.staxmate.out.SMOutputElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;

/**
 *
 */
public class VersionSerializer extends AbstractStaxMateSerializer<Version, Object> {
  public VersionSerializer() {
    super( "version", new VersionRange( new Version( 1, 0, 0 ), new Version( 1, 0, 0 ) ) );
  }

  @Override
  @NotNull
  public SMOutputElement serialize( @NotNull SMOutputElement serializeTo, @NotNull Version object, @Nullable Object context ) throws IOException, XMLStreamException {
    serializeTo.addCharacters( object.toString() );
    return serializeTo;
  }

  @Override
  @NotNull
  public Version deserialize( @NotNull XMLStreamReader deserializeFrom, @Nullable Object context ) throws IOException, XMLStreamException {
    String text = getText( deserializeFrom );
    return Version.parse( text );
  }
}
