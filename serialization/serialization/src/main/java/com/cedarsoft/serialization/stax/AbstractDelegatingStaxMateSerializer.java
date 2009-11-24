package com.cedarsoft.serialization.stax;

import com.cedarsoft.VersionRange;
import com.cedarsoft.serialization.SerializingStrategySupport;
import org.codehaus.staxmate.out.SMOutputElement;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

/**
 * @param <T> the type
 * @param <C> the type of the context
 */
public class AbstractDelegatingStaxMateSerializer<T, C> extends AbstractStaxMateSerializer<T, C> {
  @NotNull
  @NonNls
  private static final String ATTRIBUTE_TYPE = "type";

  private final SerializingStrategySupport<T, C, StaxMateSerializingStrategy<T, C>> serializingStrategySupport;

  public AbstractDelegatingStaxMateSerializer( @NotNull String defaultElementName, @NotNull VersionRange formatVersionRange, @NotNull StaxMateSerializingStrategy<? extends T, C>... strategies ) {
    this( defaultElementName, formatVersionRange, Arrays.asList( strategies ) );
  }

  public AbstractDelegatingStaxMateSerializer( @NotNull String defaultElementName, @NotNull VersionRange formatVersionRange, @NotNull Collection<? extends StaxMateSerializingStrategy<? extends T, C>> strategies ) {
    super( defaultElementName, formatVersionRange );
    serializingStrategySupport = new SerializingStrategySupport<T, C, StaxMateSerializingStrategy<T, C>>( strategies );
  }

  @Override
  @NotNull
  public SMOutputElement serialize( @NotNull SMOutputElement serializeTo, @NotNull T object, @Nullable C context ) throws IOException {
    try {
      StaxMateSerializingStrategy<T, C> strategy = serializingStrategySupport.findStrategy( object );
      serializeTo.addAttribute( ATTRIBUTE_TYPE, strategy.getId() );
      strategy.serialize( serializeTo, object, null );

      return serializeTo;
    } catch ( XMLStreamException e ) {
      throw new IOException( e );
    }
  }

  @Override
  @NotNull
  public T deserialize( @NotNull XMLStreamReader deserializeFrom, @Nullable C context ) throws IOException, XMLStreamException {
    String type = deserializeFrom.getAttributeValue( null, ATTRIBUTE_TYPE );

    StaxMateSerializingStrategy<? extends T, C> strategy = serializingStrategySupport.findStrategy( type );
    return strategy.deserialize( deserializeFrom, null );
  }

  @NotNull
  public Collection<? extends StaxMateSerializingStrategy<T, C>> getStrategies() {
    return serializingStrategySupport.getStrategies();
  }
}
