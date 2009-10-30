package com.cedarsoft.serialization.jdom;

import com.cedarsoft.lookup.Lookup;
import com.cedarsoft.serialization.jdom.JDomSerializingStrategy;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @param <T> the type
 */
public abstract class AbstractJDomSerializingStrategy<T> extends AbstractJDomSerializer<T> implements JDomSerializingStrategy<T> {
  @NotNull
  @NonNls
  private final String id;

  @NotNull
  private final Class<? extends T> supportedType;

  protected AbstractJDomSerializingStrategy( @NotNull String id, @NotNull Class<? extends T> supportedType ) {
    super( id );
    this.id = id;
    this.supportedType = supportedType;
  }

  @NotNull
  public String getId() {
    return id;
  }

  public boolean supports( @NotNull Object object ) {
    return supportedType.isAssignableFrom( object.getClass() );
  }

  @NotNull
  @Override
  public Element serialize( @NotNull Element element, @NotNull T object, @NotNull Lookup context ) throws IOException {
    serialize( element, object );
    return element;
  }

  @NotNull
  @Override
  public T deserialize( @NotNull Element element, @NotNull Lookup context ) throws IOException {
    return deserialize( element );
  }
}
