package com.cedarsoft.history;

import org.jetbrains.annotations.NotNull;

/**
 * This listener wraps another listener that only wants to listen for a subclass of the given type.
 *
 * @param <D> the type of the ObservableCollection
 * @param <T> the type of the delegating listener
 */
@Deprecated
public class TypeFilteredElementsListener<D, T extends D> implements ElementListener<D> {
  @NotNull
  private final ElementListener<T> delegate;
  @NotNull
  private final Class<T> type;

  public TypeFilteredElementsListener( @NotNull Class<T> type, @NotNull ElementListener<T> delegate ) {
    this.delegate = delegate;
    this.type = type;
  }

  @Override
  public void elementDeleted( @NotNull D element ) {
    if ( isValidType( element ) ) {
      delegate.elementDeleted( type.cast( element ) );
    }
  }

  @Override
  public void elementAdded( @NotNull D element ) {
    if ( isValidType( element ) ) {
      delegate.elementAdded( type.cast( element ) );
    }
  }

  @Override
  public void elementChanged( @NotNull D element ) {
    if ( isValidType( element ) ) {
      delegate.elementChanged( type.cast( element ) );
    }
  }

  private boolean isValidType( @NotNull D element ) {
    return type.isAssignableFrom( element.getClass() );
  }
}
