package com.cedarsoft.file.io;

import com.cedarsoft.Version;
import com.cedarsoft.VersionRange;
import com.cedarsoft.file.BaseName;
import com.cedarsoft.serialization.stax.AbstractStaxMateSerializer;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.staxmate.out.SMOutputElement;
import org.jetbrains.annotations.NotNull;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

/**
 *
 */
public class BaseNameSerializer extends AbstractStaxMateSerializer<BaseName, Object> {
  public BaseNameSerializer() {
    super( "baseName", new VersionRange( new Version( 1, 0, 0 ), new Version( 1, 0, 0 ) ) );
  }

  @NotNull
  @Override
  public SMOutputElement serialize( @NotNull SMOutputElement serializeTo, @NotNull BaseName object, @NotNull Object context ) throws IOException, XMLStreamException {
    serializeTo.addCharacters( object.getName() );
    return serializeTo;
  }

  @NotNull
  @Override
  public BaseName deserialize( @NotNull XMLStreamReader2 deserializeFrom, @NotNull Object context ) throws IOException, XMLStreamException {
    return new BaseName( getText( deserializeFrom ) );
  }
}
