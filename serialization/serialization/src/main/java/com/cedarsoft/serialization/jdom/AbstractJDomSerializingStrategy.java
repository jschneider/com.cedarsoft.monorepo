package com.cedarsoft.serialization.jdom;

import com.cedarsoft.VersionRange;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 * @param <T> the type
 * @param <C> the type of the context
 */
public abstract class AbstractJDomSerializingStrategy<T, C> extends AbstractJDomSerializer<T, C> implements JDomSerializingStrategy<T> {
  @NotNull
  @NonNls
  private final String id;

  @NotNull
  private final Class<? extends T> supportedType;


  protected AbstractJDomSerializingStrategy( @NotNull String id, @NotNull Class<? extends T> supportedType, @NotNull VersionRange formatVersionRange ) {
    super( id, formatVersionRange );
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
  public Element serialize( @NotNull Element serializeTo, @NotNull T object, @Nullable C context ) throws IOException {
    serialize( serializeTo, object );
    return serializeTo;
  }

  @Override
  @NotNull
  public T deserialize( @NotNull Element deserializeFrom, @Nullable Object context ) throws IOException {
    return deserialize( deserializeFrom );
  }
}
