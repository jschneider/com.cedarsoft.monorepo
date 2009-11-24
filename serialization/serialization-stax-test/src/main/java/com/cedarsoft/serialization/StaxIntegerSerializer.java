package com.cedarsoft.serialization;

import com.cedarsoft.Version;
import com.cedarsoft.VersionRange;
import com.cedarsoft.serialization.stax.AbstractStaxSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;

/**
 *
 */
public class StaxIntegerSerializer extends AbstractStaxSerializer<Integer, Object> {
  public StaxIntegerSerializer() {
    super( "int", new VersionRange( new Version( 1, 0, 0 ), new Version( 1, 0, 0 ) ) );
  }

  @NotNull
  @Override
  public XMLStreamWriter serialize( @NotNull XMLStreamWriter serializeTo, @NotNull Integer object, @Nullable Object context ) throws IOException, XMLStreamException {
    serializeTo.writeCharacters( object.toString() );
    return serializeTo;
  }

  @NotNull
  @Override
  public Integer deserialize( @NotNull XMLStreamReader deserializeFrom, @Nullable Object context ) throws IOException, XMLStreamException {
    return Integer.parseInt( getText( deserializeFrom ) );
  }
}
