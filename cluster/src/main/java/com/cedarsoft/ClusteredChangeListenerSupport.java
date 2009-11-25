package com.cedarsoft;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * A change listener support with transient and non transient listeners support
 */
public class ClusteredChangeListenerSupport<T> {
  @NotNull
  private final ChangeListenerSupport<T> transientSupport;
  @NotNull
  private final NonTransientChangeListenerSupport<T> nonTransientSupport;

  @Nullable
  private ContextProvider contextProvider;

  public ClusteredChangeListenerSupport( @NotNull T observerdObject ) {
    this( observerdObject, null );
  }

  public ClusteredChangeListenerSupport( @NotNull T observerdObject, @Nullable ContextProvider contextProvider ) {
    this.contextProvider = contextProvider;
    transientSupport = new ChangeListenerSupport<T>( observerdObject );
    nonTransientSupport = new NonTransientChangeListenerSupport<T>( observerdObject );
  }

  public void addChangeListener( @NotNull ChangeListener<T> listener, boolean isTransient ) {
    if ( isTransient ) {
      transientSupport.addChangeListener( listener );
    } else {
      nonTransientSupport.addChangeListener( listener );
    }
  }

  public void removeChangeListener( @NotNull ChangeListener<T> listener ) {
    transientSupport.removeChangeListener( listener );
    nonTransientSupport.removeChangeListener( listener );
  }

  /**
   * Uses the set context provider to recieve the context
   *
   * @param propertiesPath the properties path
   */
  public void changed( @NonNls @NotNull String... propertiesPath ) {
    ContextProvider provider = getContextProvider();
    if ( provider != null ) {
      changed( provider.getContext(), propertiesPath );
    } else {
      changed( null, propertiesPath );
    }
  }

  public void changed( @Nullable Object context, @NonNls @NotNull String... propertiesPath ) {
    if ( propertiesPath.length == 0 ) {
      throw new IllegalArgumentException( "Empty properties path" );
    }

    transientSupport.changed( context, propertiesPath );
    nonTransientSupport.changed( context, propertiesPath );
  }

  @Nullable
  public ContextProvider getContextProvider() {
    return contextProvider;
  }

  public void setContextProvider( @Nullable ContextProvider contextProvider ) {
    this.contextProvider = contextProvider;
  }

  @NotNull
  public PropertyChangeListener createPropertyListenerDelegate( @NotNull @NonNls String... propertiesPath ) {
    final String[] actual = new String[propertiesPath.length + 1];
    System.arraycopy( propertiesPath, 0, actual, 0, propertiesPath.length );

    return new PropertyChangeListener() {
      @Override
      public void propertyChange( PropertyChangeEvent evt ) {
        actual[actual.length - 1] = evt.getPropertyName();
        changed( actual );
      }
    };
  }

  /**
   * Provides the context
   */
  public interface ContextProvider {
    @Nullable
    Object getContext();
  }

}
