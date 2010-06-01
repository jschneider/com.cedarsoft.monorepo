/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce
 *         (GPL 3 with Classpath Exception)
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */

package com.cedarsoft.lookup;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Lazy implementation of lookup. This class will call {@link #getValue()} as late as possible and cache the results.
 */
public abstract class LazyLookup<T> extends AbstractLookup {
  /**
   * The instance.
   */
  private T instance;
  /**
   * Caches the superclasses and super interfaces
   */
  private Map<Class<?>, Object> lookups;

  protected LazyLookup() {
  }

  @Override
  @NotNull
  public Map<Class<?>, Object> lookups() {
    if ( this.lookups == null ) {
      Set<Map.Entry<Class<?>, Object>> entries = new HashSet<Map.Entry<Class<?>, Object>>();
      registerSuperTypes( getType(), entries );
      lookups = Collections.unmodifiableMap( new LookupsMap( entries ) );
    }
    //noinspection ReturnOfCollectionOrArrayField
    return lookups;
  }

  private void registerSuperTypes( @NotNull Class<?> clazz, @NotNull Set<Map.Entry<Class<?>, Object>> entries ) {
    Class<?> currentClass = clazz;
    //noinspection ObjectEquality
    while ( currentClass != null && currentClass != Object.class ) {
      entries.add( new LazyEntry( currentClass ) );
      for ( Class<?> aClass : currentClass.getInterfaces() ) {
        registerSuperTypes( aClass, entries );
        entries.add( new LazyEntry( aClass ) );
      }
      currentClass = currentClass.getSuperclass();
    }
  }

  /**
   * This method is only called once as soon as the first lookup request is made.
   *
   * @return an instance of {@link #getType()}
   */
  @NotNull
  protected abstract T createInstance();

  /**
   * The type the instance.
   */
  public abstract Class<? extends T> getType();

  /**
   * This lookup calls {@link #getValue()} when the parameter fits the type ({@link #getType()}).
   *
   * @param type the key
   * @return the found object or null if no object has been found
   */
  @Override
  @Nullable
  public <T> T lookup( @NotNull Class<T> type ) {
    if ( type.isAssignableFrom( getType() ) ) {
      return type.cast( getValue() );
    }
    return null;
  }

  /**
   * This method calls {@link #createInstance()} and caches the value.
   * Subclasses should not override this method but {@link #createInstance()} instead.
   *
   * @return the value for the instance
   */
  @Nullable
  protected T getValue() {
    if ( instance == null ) {
      instance = createInstance();
    }
    return instance;
  }

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
  public <T> void addChangeListener( @NotNull Class<T> type, @NotNull LookupChangeListener<? super T> lookupChangeListener ) {
  }

  @Override
  public <T> void removeChangeListener( @NotNull Class<T> type, @NotNull LookupChangeListener<? super T> lookupChangeListener ) {
  }

  @Override
  public void removeChangeListener( @NotNull LookupChangeListener<?> lookupChangeListener ) {
  }

  @Override
  public void addChangeListener( @NotNull LookupChangeListener<?> lookupChangeListener ) {
  }

  /**
   * A special class map
   */
  private static final class LookupsMap extends AbstractMap<Class<?>, Object> {
    @NotNull
    private final Set<Entry<Class<?>, Object>> entries;

    LookupsMap( @NotNull Set<Entry<Class<?>, Object>> entries ) {
      //noinspection AssignmentToCollectionOrArrayFieldFromParameter
      this.entries = entries;
    }

    @NotNull
    @Override
    public Set<Entry<Class<?>, Object>> entrySet() {
      //noinspection ReturnOfCollectionOrArrayField
      return entries;
    }
  }

  /**
   * An entry containing a lazy value.
   */
  private final class LazyEntry implements Map.Entry<Class<?>, Object> {
    @NotNull
    private final Class<?> aClass;

    LazyEntry( @NotNull Class<?> aClass ) {
      this.aClass = aClass;
    }

    @Override
    @NotNull
    public Class<?> getKey() {
      return aClass;
    }

    @Override
    @Nullable
    public Object getValue() {
      return LazyLookup.this.getValue();
    }

    @Override
    public Object setValue( Object value ) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals( Object o ) {
      if ( this == o ) return true;
      if ( o == null || getClass() != o.getClass() ) return false;

      LazyEntry lazyEntry = ( LazyEntry ) o;

      if ( !aClass.equals( lazyEntry.aClass ) ) return false;

      return true;
    }

    @Override
    public int hashCode() {
      return aClass.hashCode();
    }
  }
}
