package com.cedarsoft.utils;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
public class PropertiesPath {
  @NotNull
  @NonNls
  public static final String PROPERTY_REPRESENTATION = "presentation";
  @NotNull
  @NonNls
  public static final String PROPERTY_ROOT_PROPERTY = "rootProperty";
  @NotNull
  @NonNls
  public static final String PROPERTY_ELEMENTS = "elements";

  @NotNull
  @NonNls
  private final List<String> elements = new ArrayList<String>();

  /**
   * Hibernate
   */
  @Deprecated
  protected PropertiesPath() {
  }

  public PropertiesPath( @NotNull @NonNls String... elements ) {
    this( Arrays.asList( elements ) );
  }

  public PropertiesPath( @NotNull @NonNls List<String> elements ) {
    if ( elements.isEmpty() ) {
      throw new IllegalArgumentException( "Need at least one element in path" );
    }
    this.elements.addAll( elements );
  }

  @NotNull
  @NonNls
  public String getRootProperty() {
    return elements.get( 0 );
  }

  @NotNull
  public List<String> getElements() {
    return Collections.unmodifiableList( elements );
  }

  @Override
  public String toString() {
    return getPresentation();
  }

  @NotNull
  @NonNls
  public String getPresentation() {
    StringBuilder builder = new StringBuilder();

    for ( Iterator<String> it = elements.iterator(); it.hasNext(); ) {
      String element = it.next();
      builder.append( element );
      if ( it.hasNext() ) {
        builder.append( '.' );
      }
    }

    return builder.toString();
  }
}
