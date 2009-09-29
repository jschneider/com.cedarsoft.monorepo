package com.cedarsoft.history;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public abstract class ElementVisitor<E> {
  @NotNull
  @NonNls
  private final String identifier;

  protected ElementVisitor( @NonNls @NotNull String identifier ) {
    this.identifier = identifier;
  }

  @NotNull
  @NonNls
  public String getIdentifier() {
    return identifier;
  }

  public abstract boolean fits( @NotNull E element );
}