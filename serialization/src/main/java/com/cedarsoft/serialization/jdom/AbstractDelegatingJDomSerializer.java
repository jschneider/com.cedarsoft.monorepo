package com.cedarsoft.serialization.jdom;

import com.cedarsoft.NotFoundException;
import com.cedarsoft.lookup.Lookup;
import com.cedarsoft.serialization.jdom.JDomSerializingStrategy;
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
public class AbstractDelegatingJDomSerializer<T> extends AbstractJDomSerializer<T> {

  @NotNull
  @NonNls
  private static final String ATTRIBUTE_TYPE = "type";

  @NotNull
  private final List<JDomSerializingStrategy<? extends T>> strategies = new ArrayList<JDomSerializingStrategy<? extends T>>();

  public AbstractDelegatingJDomSerializer( @NotNull String defaultElementName, @NotNull JDomSerializingStrategy<? extends T>... strategies ) {
    this( defaultElementName, Arrays.asList( strategies ) );
  }

  public AbstractDelegatingJDomSerializer( @NotNull String defaultElementName, @NotNull Collection<? extends JDomSerializingStrategy<? extends T>> strategies ) {
    super( defaultElementName );
    this.strategies.addAll( strategies );
  }

  @NotNull
  @Override
  public Element serialize( @NotNull Element element, @NotNull T object, @NotNull Lookup context ) throws IOException {
    JDomSerializingStrategy<T> strategy = findStrategy( object );
    element.setAttribute( ATTRIBUTE_TYPE, strategy.getId() );
    strategy.serialize( element, object );

    return element;
  }

  @NotNull
  @Override
  public T deserialize( @NotNull Element element, @NotNull Lookup context ) throws IOException {
    String type = element.getAttributeValue( ATTRIBUTE_TYPE );

    JDomSerializingStrategy<T> strategy = findStrategy( type );
    return strategy.deserialize( element );
  }

  @NotNull
  private JDomSerializingStrategy<T> findStrategy( @NotNull @NonNls String type ) throws NotFoundException {
    for ( JDomSerializingStrategy<? extends T> strategy : strategies ) {
      if ( strategy.getId().equals( type ) ) {
        return ( JDomSerializingStrategy<T> ) strategy;
      }
    }

    throw new NotFoundException();
  }

  @NotNull
  private JDomSerializingStrategy<T> findStrategy( @NotNull T object ) throws NotFoundException {
    for ( JDomSerializingStrategy<? extends T> strategy : strategies ) {
      if ( strategy.supports( object ) ) {
        return ( JDomSerializingStrategy<T> ) strategy;
      }
    }

    throw new NotFoundException( "No strategy found for object " + object );
  }

  @NotNull
  public Collection<? extends JDomSerializingStrategy<? extends T>> getStrategies() {
    return Collections.unmodifiableList( strategies );
  }
}
