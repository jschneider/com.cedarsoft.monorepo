package com.cedarsoft.history;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public abstract class ElementVisistor<E> {
  @NotNull
  @NonNls
  private final String identifier;

  protected ElementVisistor( @NonNls @NotNull String identifier ) {
    this.identifier = identifier;
  }

  @NotNull
  @NonNls
  public String getIdentifier() {
    return identifier;
  }

  public abstract boolean fits( @NotNull E element );
}