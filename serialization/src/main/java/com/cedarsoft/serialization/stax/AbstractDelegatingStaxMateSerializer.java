package com.cedarsoft.serialization.stax;

import com.cedarsoft.NotFoundException;
import com.cedarsoft.lookup.Lookup;
import com.cedarsoft.serialization.SerializingStrategySupport;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.staxmate.out.SMOutputElement;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.lang.Override;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @param <T> the type
 */
public class AbstractDelegatingStaxMateSerializer<T> extends AbstractStaxMateSerializer<T> {
  @NotNull
  @NonNls
  private static final String ATTRIBUTE_TYPE = "type";

  private final SerializingStrategySupport<T, StaxMateSerializingStrategy<T>> serializingStrategySupport;

  public AbstractDelegatingStaxMateSerializer( @NotNull String defaultElementName, @NotNull StaxMateSerializingStrategy<? extends T>... strategies ) {
    this( defaultElementName, Arrays.asList( strategies ) );
  }

  public AbstractDelegatingStaxMateSerializer( @NotNull String defaultElementName, @NotNull Collection<? extends StaxMateSerializingStrategy<? extends T>> strategies ) {
    super( defaultElementName );
    serializingStrategySupport = new SerializingStrategySupport<T, StaxMateSerializingStrategy<T>>( strategies );
  }

  @Override
  @NotNull
  public SMOutputElement serialize( @NotNull SMOutputElement serializeTo, @NotNull T object, @NotNull Lookup context ) throws IOException {
    try {
      StaxMateSerializingStrategy<T> strategy = serializingStrategySupport.findStrategy( object );
      serializeTo.addAttribute( ATTRIBUTE_TYPE, strategy.getId() );
      strategy.serialize( serializeTo, object );

      return serializeTo;
    } catch ( XMLStreamException e ) {
      throw new IOException( e );
    }
  }

  @Override
  @NotNull
  public T deserialize( @NotNull XMLStreamReader2 deserializeFrom, @NotNull Lookup context ) throws IOException, XMLStreamException {
    String type = deserializeFrom.getAttributeValue( null, ATTRIBUTE_TYPE );

    StaxMateSerializingStrategy<? extends T> strategy = serializingStrategySupport.findStrategy( type );
    return strategy.deserialize( deserializeFrom );
  }

  @NotNull
  public Collection<? extends StaxMateSerializingStrategy<T>> getStrategies() {
    return serializingStrategySupport.getStrategies();
  }
}
