package com.cedarsoft.serialization;

import com.cedarsoft.NotFoundException;
import com.cedarsoft.lookup.Lookup;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @param <T> the type
 */
public class AbstractDelegatingSerializer<T> extends AbstractSerializer<T> {

  @NotNull
  @NonNls
  private static final String ATTRIBUTE_TYPE = "type";

  @NotNull
  private final List<SerializingStrategy<? extends T>> strategies = new ArrayList<SerializingStrategy<? extends T>>();

  public AbstractDelegatingSerializer( @NotNull String defaultElementName, @NotNull SerializingStrategy<? extends T>... strategies ) {
    this( defaultElementName, Arrays.asList( strategies ) );
  }

  public AbstractDelegatingSerializer( @NotNull String defaultElementName, @NotNull Collection<? extends SerializingStrategy<? extends T>> strategies ) {
    super( defaultElementName );
    this.strategies.addAll( strategies );
  }

  @NotNull
  @Override
  public Element serialize( @NotNull Element element, @NotNull T object, @NotNull Lookup context ) throws IOException {
    SerializingStrategy<T> strategy = findStrategy( object );
    element.setAttribute( ATTRIBUTE_TYPE, strategy.getId() );
    strategy.serialize( element, object );

    return element;
  }

  @NotNull
  @Override
  public T deserialize( @NotNull Element element, @NotNull Lookup context ) throws IOException {
    String type = element.getAttributeValue( ATTRIBUTE_TYPE );

    SerializingStrategy<T> strategy = findStrategy( type );
    return strategy.deserialize( element );
  }

  @NotNull
  private SerializingStrategy<T> findStrategy( @NotNull @NonNls String type ) throws NotFoundException {
    for ( SerializingStrategy<? extends T> strategy : strategies ) {
      if ( strategy.getId().equals( type ) ) {
        return ( SerializingStrategy<T> ) strategy;
      }
    }

    throw new NotFoundException();
  }

  @NotNull
  private SerializingStrategy<T> findStrategy( @NotNull T object ) throws NotFoundException {
    for ( SerializingStrategy<? extends T> strategy : strategies ) {
      if ( strategy.supports( object ) ) {
        return ( SerializingStrategy<T> ) strategy;
      }
    }

    throw new NotFoundException( "No strategy found for object " + object );
  }

  @NotNull
  public Collection<? extends SerializingStrategy<? extends T>> getStrategies() {
    return Collections.unmodifiableList( strategies );
  }
}
