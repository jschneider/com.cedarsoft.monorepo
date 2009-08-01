package com.cedarsoft.utils.serialization;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.cedarsoft.utils.StillContainedException;

/**
 *
 */
public class InMemorySerializedObjectsAccess implements RegistrySerializer.SerializedObjectsAccess {
  @NotNull
  @NonNls
  private final Map<String, byte[]> serialized = new HashMap<String, byte[]>();

  @NotNull
  public InputStream getInputStream( @NotNull @NonNls String id ) {
    byte[] found = serialized.get( id );
    if ( found == null ) {
      throw new IllegalArgumentException( "No stored data found for <" + id + ">" );
    }
    return new ByteArrayInputStream( found );
  }

  @NotNull
  public Set<? extends String> getStoredIds() {
    return serialized.keySet();
  }

  @NotNull
  public OutputStream openOut( @NotNull @NonNls final String id ) {
    byte[] stored = serialized.get( id );
    if ( stored != null ) {
      throw new StillContainedException( id );
    }

    return new ByteArrayOutputStream() {
      @Override
      public void close() throws IOException {
        super.close();
        serialized.put( id, toByteArray() );
      }
    };
  }
}
