package com.cedarsoft.serialization.stax;

import com.cedarsoft.Version;
import com.cedarsoft.lookup.Lookups;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.staxmate.out.SMOutputElement;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.lang.Override;

/**
 * Attention:
 * On deserialization every subclass has to *consume* everything including the end event for their tag.
 *
 * @param <T> the type
 */
public abstract class AbstractStaxMateSerializingStrategy<T> extends AbstractStaxMateSerializer<T> implements StaxMateSerializingStrategy<T> {
  @NotNull
  @NonNls
  private final String id;

  @NotNull
  private final Class<? extends T> supportedType;

  protected AbstractStaxMateSerializingStrategy( @NotNull String id, @NotNull Class<? extends T> supportedType, @NotNull Version formatVersion ) {
    super( id, formatVersion );
    this.id = id;
    this.supportedType = supportedType;
  }

  @Override
  @NotNull
  public String getId() {
    return id;
  }

  @Override
  public boolean supports( @NotNull Object object ) {
    return supportedType.isAssignableFrom( object.getClass() );
  }

  @Override
  @NotNull
  public SMOutputElement serialize( @NotNull SMOutputElement serializeTo, @NotNull T object ) throws IOException {
    try {
      return serialize( serializeTo, object, Lookups.emtyLookup() );
    } catch ( XMLStreamException e ) {
      throw new IOException( e );
    }
  }

  @Override
  @NotNull
  public T deserialize( @NotNull @NonNls XMLStreamReader2 deserializeFrom ) throws IOException {
    try {
      return deserialize( deserializeFrom, Lookups.emtyLookup() );
    } catch ( XMLStreamException e ) {
      throw new IOException( e );
    }
  }
}
