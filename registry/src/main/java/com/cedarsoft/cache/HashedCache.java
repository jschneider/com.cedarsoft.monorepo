/**
 * Copyright (C) 2010 cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce.txt
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

package com.cedarsoft.cache;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A cache implementation that is backed up using a map
 */
public class HashedCache<K, T> implements Cache<K, T> {
  @NotNull
  private final ReadWriteLock lock = new ReentrantReadWriteLock();

  @NotNull
  private final Map<K, T> store;
  @NotNull
  private final Factory<K, T> factory;

  /**
   * Creates a new hashed cache
   *
   * @param factory the factory
   */
  public HashedCache( @NotNull Factory<K, T> factory ) {
    this.factory = factory;
    store = new HashMap<K, T>();
  }

  /**
   * Use with care. It is preferred to use {@link #HashedCache(Cache.Factory)} instead.
   * Uses the given store.
   *
   * @param store   the store
   * @param factory the factory
   */
  public HashedCache( @NotNull Map<K, T> store, @NotNull Factory<K, T> factory ) {
    this.factory = factory;
    //noinspection AssignmentToCollectionOrArrayFieldFromParameter
    this.store = store;
  }

  /**
   * Returns the value for the given key.
   * If no value is stored, a new one is created using the registered factory
   *
   * @param key the key
   * @return the value
   */
  @Override
  public T get( @NotNull Object key ) {
    lock.writeLock().lock();
    try {
      T element = store.get( key );
      if ( element == null ) {
        element = factory.create( ( K ) key );
        store.put( ( K ) key, element );
      }
      return element;
    } finally {
      lock.writeLock().unlock();
    }
  }

  /*
 MAP DELEGATES
  */

  @Override
  public boolean containsKey( Object key ) {
    lock.readLock().lock();
    try {
      return store.containsKey( key );
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public boolean containsValue( Object value ) {
    lock.readLock().lock();
    try {
      return store.containsValue( value );
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public T put( K key, T value ) {
    throw new UnsupportedOperationException( "Use the factory instead" );
  }

  @Override
  @Nullable
  public T remove( @NotNull Object key ) {
    lock.writeLock().lock();
    try {
      return store.remove( key );
    } finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public void putAll( Map<? extends K, ? extends T> m ) {
    throw new UnsupportedOperationException( "Use the factory instead" );
  }

  @Override
  public Set<K> keySet() {
    lock.readLock().lock();
    try {
      return store.keySet();
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public Collection<T> values() {
    lock.readLock().lock();
    try {
      return store.values();
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * Returns the *LIVE* entry set. Use with care!
   *
   * @return the live entry set for the cache
   */
  @Override
  @NotNull
  public Set<Entry<K, T>> entrySet() {
    lock.readLock().lock();
    try {
      return store.entrySet();
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public int size() {
    lock.readLock().lock();
    try {
      return store.size();
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public boolean isEmpty() {
    lock.readLock().lock();
    try {
      return store.isEmpty();
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * Returns the internal store.
   * USE WITH CARE!
   */
  @Deprecated
  @NotNull
  public Map<K, T> getInternalStore() {
    lock.readLock().lock();
    try {
      //noinspection ReturnOfCollectionOrArrayField
      return store;
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public void clear() {
    throw new UnsupportedOperationException();
  }
}
