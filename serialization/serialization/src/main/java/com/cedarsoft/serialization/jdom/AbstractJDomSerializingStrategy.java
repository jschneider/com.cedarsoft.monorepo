package com.cedarsoft.serialization.jdom;

import com.cedarsoft.VersionRange;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * @param <T> the type
 * @param <C> the type of the context
 */
public abstract class AbstractJDomSerializingStrategy<T, C> extends AbstractJDomSerializer<T, C> implements JDomSerializingStrategy<T, C> {
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
}
