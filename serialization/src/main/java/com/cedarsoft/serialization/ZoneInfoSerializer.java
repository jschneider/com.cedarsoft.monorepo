package com.cedarsoft.serialization;

import com.cedarsoft.lookup.Lookup;
import com.cedarsoft.lookup.Lookups;
import com.cedarsoft.serialization.Serializer;
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
}
