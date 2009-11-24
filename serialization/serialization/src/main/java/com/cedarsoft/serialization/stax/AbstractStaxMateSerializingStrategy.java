package com.cedarsoft.serialization.stax;

import com.cedarsoft.VersionRange;
import org.codehaus.staxmate.out.SMOutputElement;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;

/**
 * Attention:
 * On deserialization every subclass has to *consume* everything including the end event for their tag.
 *
 * @param <T> the type
 * @param <C> the type of the context
 */
public abstract class AbstractStaxMateSerializingStrategy<T, C> extends AbstractStaxMateSerializer<T, C> implements StaxMateSerializingStrategy<T, C> {
  @NotNull
  @NonNls
  private final String id;

  @NotNull
  private final Class<? extends T> supportedType;

  protected AbstractStaxMateSerializingStrategy( @NotNull String id, @NotNull Class<? extends T> supportedType, @NotNull VersionRange formatVersionRange ) {
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
