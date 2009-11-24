package com.cedarsoft.serialization.jdom;

import com.cedarsoft.VersionRange;
import com.cedarsoft.serialization.SerializingStrategySupport;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

/**
 * @param <T> the type
 */
public class AbstractDelegatingJDomSerializer<T> extends AbstractJDomSerializer<T, Object> {
  @NotNull
  @NonNls
  private static final String ATTRIBUTE_TYPE = "type";
  @NotNull
  private final SerializingStrategySupport<T, Object, JDomSerializingStrategy<T, Object>> serializingStrategySupport;

  public AbstractDelegatingJDomSerializer( @NotNull String defaultElementName, @NotNull VersionRange formatVersionRange, @NotNull JDomSerializingStrategy<? extends T, Object>... strategies ) {
    this( defaultElementName, formatVersionRange, Arrays.asList( strategies ) );
  }

  public AbstractDelegatingJDomSerializer( @NotNull String defaultElementName, @NotNull VersionRange formatVersionRange, @NotNull Collection<? extends JDomSerializingStrategy<? extends T, Object>> strategies ) {
    super( defaultElementName, formatVersionRange );
    this.serializingStrategySupport = new SerializingStrategySupport<T, Object, JDomSerializingStrategy<T, Object>>( strategies );
  }

  @Override
  @NotNull
  public Element serialize( @NotNull Element serializeTo, @NotNull T object, @Nullable  Object context ) throws IOException {
    JDomSerializingStrategy<T, Object> strategy = serializingStrategySupport.findStrategy( object );
    serializeTo.setAttribute( ATTRIBUTE_TYPE, strategy.getId() );
    strategy.serialize( serializeTo, object, null );

    return serializeTo;
  }

  @Override
  @NotNull
  public T deserialize( @NotNull Element deserializeFrom, @Nullable Object context ) throws IOException {
    String type = deserializeFrom.getAttributeValue( ATTRIBUTE_TYPE );

    JDomSerializingStrategy<T, Object> strategy = serializingStrategySupport.findStrategy( type );
    return strategy.deserialize( deserializeFrom, null );
  }

  @NotNull
  public Collection<? extends JDomSerializingStrategy<T, Object>> getStrategies() {
    return serializingStrategySupport.getStrategies();
  }
}
