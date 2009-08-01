package com.cedarsoft.utils;

import org.jetbrains.annotations.NotNull;

/**
 * Creates an object from a string
 */
public interface ObjectFactory<T> {
  @NotNull
  T create( @NotNull String representation ) throws InvalidRepresentationException;


  class InvalidRepresentationException extends Exception {
    public InvalidRepresentationException( String message ) {
      super( message );
    }
  }
}