package com.cedarsoft.utils.configuration.xml;

import com.cedarsoft.utils.configuration.DefaultValueProvider;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *
 */
public final class EmptyDefaultValueProvider implements DefaultValueProvider {
  @NotNull
  public static final EmptyDefaultValueProvider INSTANCE = new EmptyDefaultValueProvider();

  @NotNull
  public <T> T getDefaultValue( @NotNull @NonNls String key, @NotNull Class<T> type ) {
    if ( Map.class.isAssignableFrom( type ) ) {
      return type.cast( Collections.emptyMap() );
    }
    if ( List.class.isAssignableFrom( type ) ) {
      return type.cast( Collections.emptyList() );
    }
    if ( String.class.isAssignableFrom( type ) ) {
      return type.cast( "" );
    }
    if ( Integer.class.isAssignableFrom( type ) ) {
      return type.cast( 0 );
    }
    if ( Long.class.isAssignableFrom( type ) ) {
      return type.cast( 0 );
    }

    throw new IllegalArgumentException( "Cannot create empty default value of type " + type + " for key " + key );
  }
}
