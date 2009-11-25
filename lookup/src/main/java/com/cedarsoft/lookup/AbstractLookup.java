package com.cedarsoft.lookup;

import org.jetbrains.annotations.NotNull;

/**
 *
 */
public abstract class AbstractLookup implements Lookup {
  @Override
  @NotNull
  public final <T> T lookupNonNull( @NotNull Class<T> type ) throws IllegalArgumentException {
    T value = lookup( type );
    if ( value == null ) {
      throw new IllegalArgumentException( "Nothing found for " + type.getName() );
    } else {
      return value;
    }
  }
}
