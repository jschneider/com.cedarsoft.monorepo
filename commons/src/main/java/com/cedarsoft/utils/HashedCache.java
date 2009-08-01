package com.cedarsoft.utils;

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

  public boolean containsKey( Object key ) {
    lock.readLock().lock();
    try {
      return store.containsKey( key );
    } finally {
      lock.readLock().unlock();
    }
  }

  public boolean containsValue( Object value ) {
    lock.readLock().lock();
    try {
      return store.containsValue( value );
    } finally {
      lock.readLock().unlock();
    }
  }

  public T put( K key, T value ) {
    throw new UnsupportedOperationException( "Use the factory instead" );
  }

  @Nullable
  public T remove( @NotNull Object key ) {
    lock.writeLock().lock();
    try {
      return store.remove( key );
    } finally {
      lock.writeLock().unlock();
    }
  }

  public void putAll( Map<? extends K, ? extends T> m ) {
    throw new UnsupportedOperationException( "Use the factory instead" );
  }

  public Set<K> keySet() {
    lock.readLock().lock();
    try {
      return store.keySet();
    } finally {
      lock.readLock().unlock();
    }
  }

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
  @NotNull
  public Set<Entry<K, T>> entrySet() {
    lock.readLock().lock();
    try {
      return store.entrySet();
    } finally {
      lock.readLock().unlock();
    }
  }

  public int size() {
    lock.readLock().lock();
    try {
      return store.size();
    } finally {
      lock.readLock().unlock();
    }
  }

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

  public void clear() {
    throw new UnsupportedOperationException();
  }
}
