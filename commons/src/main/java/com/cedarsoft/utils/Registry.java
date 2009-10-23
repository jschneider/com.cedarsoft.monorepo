package com.cedarsoft.utils;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;

/**
 * @param <T> the type this registry stores
 */
public interface Registry<T> {
  /**
   * Returns the stored objects
   *
   * @return the stored objects
   */
  @NotNull
  List<? extends T> getStoredObjects();

  /**
   * Returns the first object that matches the matcher
   *
   * @param matcher the matcher
   * @return the first object that matches or null
   */
  @Nullable
  T findStoredObject( @NotNull @NonNls Matcher<T> matcher );

  /**
   * Finds the stored objects
   *
   * @param matcher the matcher
   * @return the found objects that match the matcher
   */
  @NotNull
  List<? extends T> findStoredObjects( @NotNull @NonNls Matcher<T> matcher );

  /**
   * Returns the stored objects
   *
   * @param matcher   the matcher
   * @param converter the converter
   * @param <C>       the type of the target of the conversion
   * @return the matched and converted objects
   */
  @NotNull
  <C> List<? extends C> findStoredObjects( @NotNull @NonNls Matcher<T> matcher, @NotNull Converter<T, C> converter );

  /**
   * Stores the object
   *
   * @param object the object that is stored
   * @throws StillContainedException
   */
  void store( @NotNull T object ) throws StillContainedException;

  /**
   * Returns the (optional) comparator
   *
   * @return the used comparator or null if no comparator is available
   */
  @Nullable
  Comparator<T> getComparator();

  /**
   * Whether this registry only contains unique elements
   *
   * @return whether this registry only contains unique elements
   */
  boolean containsOnlyUniqueElements();

  /**
   * Adds a listener
   *
   * @param listener the listener
   */
  void addListener( @NotNull Listener<T> listener );

  /**
   * Removes the listeners
   *
   * @param listener the listener that is removed
   */
  void removeListener( @NotNull Listener<T> listener );

  /**
   * The listener that is notified about changes of the registry
   *
   * @param <T> the type
   */
  interface Listener<T> {
    /**
     * Is called if a new object has been stored
     *
     * @param object the object
     */
    void objectStored( @NotNull T object );
  }

  /**
   * A simple matcher
   *
   * @param <T> the type of the matcher
   */
  interface Matcher<T> {
    /**
     * Whether the object matches
     *
     * @param object the object
     * @return true if the object matches, false otherwise
     */
    boolean matches( @NotNull T object );
  }
}