package com.cedarsoft.serialization;

import com.cedarsoft.Version;
import com.cedarsoft.lookup.Lookup;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joda.time.DateTimeZone;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 */
public class ZoneInfoSerializer implements Serializer<DateTimeZone, Lookup> {
  @Override
  public void serialize( @NotNull DateTimeZone object, @NotNull OutputStream out, @Nullable Lookup context ) throws IOException {
    out.write( object.getID().getBytes() );
  }

  @NotNull
  @Override
  public DateTimeZone deserialize( @NotNull InputStream in, @Nullable Lookup context ) throws IOException {
    return DateTimeZone.forID( IOUtils.toString( in ) );
  }

  @NotNull
  @Override
  public Version getFormatVersion() {
    return new Version( 1, 0, 0 );
  }
}
