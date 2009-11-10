package com.cedarsoft.provider;

import org.jetbrains.annotations.NotNull;

/**
 * @param <T> the type that is provided
 * @param <C> the context
 * @param <E> the exception that is thrown
 */
public abstract class AbstractContextualProvider<T, C, E extends Throwable> implements ContextualProvider<T, C, E> {
  @NotNull
  private final ContextualProvider<T, C, E> contextualProvider;

  protected AbstractContextualProvider( @NotNull ContextualProvider<T, C, E> contextualProvider ) {
    this.contextualProvider = contextualProvider;
  }

  @NotNull
  public Provider<T, E> createProvider( @NotNull final C context ) {
    return new Provider<T, E>() {
      @NotNull
      @Override
      public T provide() throws E {
        return contextualProvider.provide( context );
      }

      @NotNull
      @Override
      public String getDescription() {
        return contextualProvider.getDescription( context );
      }
    };
  }
}
