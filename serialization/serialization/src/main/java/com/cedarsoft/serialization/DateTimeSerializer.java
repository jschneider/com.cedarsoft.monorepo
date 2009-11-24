package com.cedarsoft.serialization;

import com.cedarsoft.Version;
import com.cedarsoft.VersionRange;
import com.cedarsoft.serialization.stax.AbstractStaxMateSerializer;
import org.codehaus.staxmate.out.SMOutputElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;

/**
 *
 */
public class DateTimeSerializer extends AbstractStaxMateSerializer<DateTime, Object> {
  public DateTimeSerializer() {
    super( "dateTime", new VersionRange( new Version( 1, 0, 0 ), new Version( 1, 0, 0 ) ) );
  }

  @NotNull
  @Override
  public SMOutputElement serialize( @NotNull SMOutputElement serializeTo, @NotNull DateTime object, @Nullable Object context ) throws IOException, XMLStreamException {
    serializeTo.addCharacters( ISODateTimeFormat.basicDateTime().print( object ) );
    return serializeTo;
  }

  @NotNull
  @Override
  public DateTime deserialize( @NotNull XMLStreamReader deserializeFrom, @Nullable Object context ) throws IOException, XMLStreamException {
    String text = getText( deserializeFrom );

    try {
      return ISODateTimeFormat.basicDateTime().parseDateTime( text );
    } catch ( IllegalArgumentException ignore ) {
      //Maybe it is a long
      return new DateTime( Long.parseLong( text ) );
    }
  }
}
