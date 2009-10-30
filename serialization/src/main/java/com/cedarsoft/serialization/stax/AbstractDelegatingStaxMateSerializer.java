package com.cedarsoft.serialization.stax;

import com.cedarsoft.NotFoundException;
import com.cedarsoft.lookup.Lookup;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.staxmate.out.SMOutputElement;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
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

  @NotNull
  private final List<StaxMateSerializingStrategy<? extends T>> strategies = new ArrayList<StaxMateSerializingStrategy<? extends T>>();

  public AbstractDelegatingStaxMateSerializer( @NotNull String defaultElementName, @NotNull StaxMateSerializingStrategy<? extends T>... strategies ) {
    this( defaultElementName, Arrays.asList( strategies ) );
  }

  public AbstractDelegatingStaxMateSerializer( @NotNull String defaultElementName, @NotNull Collection<? extends StaxMateSerializingStrategy<? extends T>> strategies ) {
    super( defaultElementName );
    this.strategies.addAll( strategies );
  }

  @NotNull
  @Override
  public SMOutputElement serialize( @NotNull SMOutputElement serializeTo, @NotNull T object, @NotNull Lookup context ) throws IOException {
    try {
      StaxMateSerializingStrategy<T> strategy = findStrategy( object );
      serializeTo.addAttribute( ATTRIBUTE_TYPE, strategy.getId() );
      strategy.serialize( serializeTo, object );

      return serializeTo;
    } catch ( XMLStreamException e ) {
      throw new IOException( e );
    }
  }

  @NotNull
  @Override
  public T deserialize( @NotNull XMLStreamReader2 deserializeFrom, @NotNull Lookup context ) throws IOException {
    String type = deserializeFrom.getAttributeValue( null, ATTRIBUTE_TYPE );

    StaxMateSerializingStrategy<T> strategy = findStrategy( type );
    return strategy.deserialize( deserializeFrom );
  }

  @NotNull
  private StaxMateSerializingStrategy<T> findStrategy( @NotNull @NonNls String type ) throws NotFoundException {
    for ( StaxMateSerializingStrategy<? extends T> strategy : strategies ) {
      if ( strategy.getId().equals( type ) ) {
        return ( StaxMateSerializingStrategy<T> ) strategy;
      }
    }

    throw new NotFoundException();
  }

  @NotNull
  private StaxMateSerializingStrategy<T> findStrategy( @NotNull T object ) throws NotFoundException {
    for ( StaxMateSerializingStrategy<? extends T> strategy : strategies ) {
      if ( strategy.supports( object ) ) {
        return ( StaxMateSerializingStrategy<T> ) strategy;
      }
    }

    throw new NotFoundException( "No strategy found for object " + object );
  }

  @NotNull
  public Collection<? extends StaxMateSerializingStrategy<? extends T>> getStrategies() {
    return Collections.unmodifiableList( strategies );
  }
}
