package com.cedarsoft.lookup;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Merges two lookups. The first lookup has predecense. Every object that is contained within the first lookup
 * is resolved using the first.
 * Only if no object of a given type is contained within the first lookup the second lookup is queried.
 */
public class MergingLookup extends AbstractLookup implements Lookup {
  private LookupChangeSupport lcs = new LookupChangeSupport( this );

  private Lookup first;
  private Lookup second;

  public MergingLookup( @NotNull Lookup first, @NotNull Lookup second ) {
    this.first = first;
    this.second = second;

    first.addChangeListener( new LookupChangeListener<Object>() {
      @Override
      public void lookupChanged( @NotNull LookupChangeEvent<?> event ) {
        lcs.fireLookupChanged( new LookupChangeEvent<Object>( MergingLookup.this, ( Class<Object> ) event.getType(), event.getOldValue(), event.getNewValue() ) );
      }
    } );

    second.addChangeListener( new LookupChangeListener<Object>() {
      @Override
      public void lookupChanged( @NotNull LookupChangeEvent<?> event ) {
        Class<?> type = event.getType();
        if ( MergingLookup.this.first.lookup( type ) == null ) {
          lcs.fireLookupChanged( new LookupChangeEvent<Object>( MergingLookup.this, ( Class<Object> ) type, event.getOldValue(), event.getNewValue() ) );
        }
      }
    } );
  }

  @Override
  @Nullable
  public <T> T lookup( @NotNull Class<T> type ) {
    T firstValue = first.lookup( type );
    if ( firstValue != null ) {
      return firstValue;
    }
    return second.lookup( type );
  }

  @Override
  @NotNull
  public Map<Class<?>, Object> lookups() {
    Map<Class<?>, Object> map = new HashMap<Class<?>, Object>();
    map.putAll( second.lookups() );
    //automatically overwrite all values from the second now if necessary
    map.putAll( first.lookups() );
    return map;
  }

  @Override
  public <T> void bind( @NotNull Class<T> type, @NotNull LookupChangeListener<? super T> lookupChangeListener ) {
    lcs.bind( type, lookupChangeListener );
  }

  @Override
  public <T> void bind( @NotNull TypedLookupChangeListener<T> lookupChangeListener ) {
    lcs.bind( lookupChangeListener );
  }

  @Override
  public <T> void bindWeak( @NotNull Class<T> type, @NotNull LookupChangeListener<? super T> lookupChangeListener ) {
    lcs.bindWeak( type, lookupChangeListener );
  }

  @Override
  public <T> void bindWeak( @NotNull TypedLookupChangeListener<T> lookupChangeListener ) {
    lcs.bindWeak( lookupChangeListener );
  }

  @Override
  public void addChangeListenerWeak( @NotNull LookupChangeListener<?> lookupChangeListener ) {
    lcs.addChangeListenerWeak( lookupChangeListener );
  }

  @Override
  public <T> void addChangeListenerWeak( @NotNull Class<T> type, @NotNull LookupChangeListener<? super T> lookupChangeListener ) {
    lcs.addChangeListenerWeak( type, lookupChangeListener );
  }

  @Override
  public void addChangeListener( @NotNull LookupChangeListener<?> lookupChangeListener ) {
    lcs.addChangeListener( lookupChangeListener );
  }

  @Override
  public <T> void addChangeListener( @NotNull Class<T> type, @NotNull LookupChangeListener<? super T> lookupChangeListener ) {
    lcs.addChangeListener( type, lookupChangeListener );
  }

  @Override
  public <T> void removeChangeListener( @NotNull Class<T> type, @NotNull LookupChangeListener<? super T> lookupChangeListener ) {
    lcs.removeChangeListener( type, lookupChangeListener );
  }

  @Override
  public void removeChangeListener( @NotNull LookupChangeListener<?> lookupChangeListener ) {
    lcs.removeChangeListener( lookupChangeListener );
  }
}
