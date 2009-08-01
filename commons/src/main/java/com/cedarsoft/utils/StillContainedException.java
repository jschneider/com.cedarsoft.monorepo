package com.cedarsoft.utils;

import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class StillContainedException extends RuntimeException {
  @NotNull
  private final Object object;

  public StillContainedException( @NotNull Object object ) {
    super( "The object <" + object + "> is still contained" );
    this.object = object;
  }

  @NotNull
  public Object getObject() {
    return object;
  }
}
