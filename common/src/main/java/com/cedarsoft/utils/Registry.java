package com.cedarsoft.utils;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @param <T> the type that is stored within this registry
 */
public class Registry<T> {
  @NotNull
  @NonNls
  protected final List<T> storedObjects = new ArrayList<T>();
  @NotNull
  protected final ReadWriteLock lock = new ReentrantReadWriteLock();

  /**
   * This comparator may optionally be set to ensure the registry only contains unique values
   */
  @Nullable
  protected final Comparator<T> comparator;

  public Registry() {
    comparator = null;
  }

  public Registry( @NotNull List<? extends T> storedObjects ) {
    this( storedObjects, null );
  }

  public Registry( @NotNull List<? extends T> storedObjects, @Nullable Comparator<T> comparator ) {
    this.comparator = comparator;
    this.storedObjects.addAll( storedObjects );
  }

  /**
   * Returns the available cameras
   *
   * @return the cameras
   */
  @NotNull
  public List<? extends T> getStoredObjects() {
    lock.readLock().lock();
    try {
      return Collections.unmodifiableList( storedObjects );
    } finally {
      lock.readLock().unlock();
    }
  }

  @Nullable
  public T findStoredObject( @NotNull @NonNls Matcher<T> matcher ) {
    lock.readLock().lock();
    try {
      for ( T object : storedObjects ) {
        if ( matcher.matches( object ) ) {
          return object;
        }
      }

      return null;
    } finally {
      lock.readLock().unlock();
    }
  }

  @NotNull
  public List<? extends T> findStoredObjects( @NotNull @NonNls Matcher<T> matcher ) {

    lock.readLock().lock();
    try {
      List<T> found = new ArrayList<T>();
      for ( T object : storedObjects ) {
        if ( matcher.matches( object ) ) {
          found.add( object );
        }
      }

      return found;
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * Stores the object within the registry
   *
   * @param object the object
   * @throws StillContainedException if a comparator is set and the object still exists within this registry
   */
  public void store( @NotNull T object ) throws StillContainedException {
    lock.writeLock().lock();
    try {
      if ( comparator != null ) {
        for ( T storedObject : storedObjects ) {
          if ( comparator.compare( storedObject, object ) == 0 ) {
            throw new StillContainedException( object );
          }
        }
      }

      storedObjects.add( object );
    } finally {
      lock.writeLock().unlock();
    }

    listenersLock.readLock().lock();
    try {
      for ( Listener<T> listener : listeners ) {
        listener.objectAdded( object );
      }
    } finally {
      listenersLock.readLock().unlock();
    }
  }

  @Nullable
  public Comparator<T> getComparator() {
    return comparator;
  }

  public boolean containsOnlyUniqueElements() {
    return comparator != null;
  }

  @NotNull
  protected final ReadWriteLock listenersLock = new ReentrantReadWriteLock();

  @NotNull
  protected final List<Listener<T>> listeners = new ArrayList<Listener<T>>();

  public void addListener( @NotNull Listener<T> listener ) {
    listenersLock.writeLock().lock();
    try {
      this.listeners.add( listener );
    } finally {
      listenersLock.writeLock().unlock();
    }
  }

  /**
   * The listener that is notified about changes of the registry
   *
   * @param <T> the type
   */
  public interface Listener<T> {
    /**
     * Is called if a new object has been stored
     *
     * @param object the object
     */
    void objectAdded( @NotNull T object );
  }

  public interface Matcher<T> {
    boolean matches( @NotNull T object );
  }
}
