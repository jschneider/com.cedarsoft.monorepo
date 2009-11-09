package com.cedarsoft.lookup;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.Override;
import java.util.Collections;
import java.util.Map;

/**
 * This is a simpel Lookup that contains just one object under a given key
 */
public class SingletonLookup<T> extends AbstractLookup {
  private final T singleton;
  private final Class<T> singletonType;

  public SingletonLookup( @NotNull Class<T> type, @NotNull T value ) {
    this.singletonType = type;
    this.singleton = value;
  }

  @Override
  @Nullable
  public <T> T lookup( @NotNull Class<T> type ) {
    if ( type == this.singletonType ) {
      return type.cast( singleton );
    }
    return null;
  }

  @Override
  @NotNull
  public Map<Class<?>, Object> lookups() {
    return Collections.<Class<?>, Object>singletonMap( ( Class<?> ) singletonType, ( Object ) singleton );
  }

  @NotNull
  public T getSingleton() {
    return singleton;
  }

  @NotNull
  public Class<T> getSingletonType() {
    return singletonType;
  }

  /*
   * The value can't be changed. Therefore the listeners are not supported
   */

  @Override
  public <T> void bind( @NotNull Class<T> type, @NotNull LookupChangeListener<? super T> lookupChangeListener ) {
    lookupChangeListener.lookupChanged( new LookupChangeEvent<T>( this, type, null, lookup( type ) ) );
  }

  @Override
  public <T> void bind( @NotNull TypedLookupChangeListener<T> lookupChangeListener ) {
    bind( lookupChangeListener.getType(), lookupChangeListener );
  }

  @Override
  public <T> void bindWeak( @NotNull Class<T> type, @NotNull LookupChangeListener<? super T> lookupChangeListener ) {
    lookupChangeListener.lookupChanged( new LookupChangeEvent<T>( this, type, null, lookup( type ) ) );
  }

  @Override
  public <T> void bindWeak( @NotNull TypedLookupChangeListener<T> lookupChangeListener ) {
    bindWeak( lookupChangeListener.getType(), lookupChangeListener );
  }

  @Override
  public void addChangeListenerWeak( @NotNull LookupChangeListener<?> lookupChangeListener ) {
  }

  @Override
  public <T> void addChangeListenerWeak( @NotNull Class<T> type, @NotNull LookupChangeListener<? super T> lookupChangeListener ) {
  }

  @Override
  public void addChangeListener( @NotNull LookupChangeListener<?> lookupChangeListener ) {
  }

  @Override
  public <T> void addChangeListener( @NotNull Class<T> type, @NotNull LookupChangeListener<? super T> lookupChangeListener ) {
  }

  @Override
  public void removeChangeListener( @NotNull LookupChangeListener<?> lookupChangeListener ) {
  }

  @Override
  public <T> void removeChangeListener( @NotNull Class<T> type, @NotNull LookupChangeListener<? super T> lookupChangeListener ) {
  }
}
