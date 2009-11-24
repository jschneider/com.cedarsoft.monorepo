package com.cedarsoft.tags;

import org.jetbrains.annotations.NotNull;

/**
 * Implementing classes can be observed and notify listeners about tag changes.
 */
public interface TagObservable extends Tagged {
  /**
   * Adds a tag listener
   *
   * @param listener the listener that is registered
   */
  void addTagChangeListener( @NotNull TagChangeListener listener );

  /**
   * Removes a tag listener
   *
   * @param listener the listener that is removed
   */
  void removeTagChangeListener( @NotNull TagChangeListener listener );
}
