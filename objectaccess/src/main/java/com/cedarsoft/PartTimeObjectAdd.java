package com.cedarsoft;

import org.jetbrains.annotations.NotNull;

/**
 * Object can't always be added.
 */
public interface PartTimeObjectAdd<T> extends ObjectAdd<T> {
  /**
   * Returns true if objects can be added
   *
   * @return true if objects can be added, false otherwise
   */
  boolean canAdd();

  void addPartTimeListener( @NotNull PartTimeListener listener );

  void removePartTimeListener( @NotNull PartTimeListener listener );

  interface PartTimeListener {

    /**
     * Is called when "add" isn't available anymore
     */
    void addNotAvailable();

    /**
     * Is called when add is available
     */
    void addAvailable();
  }
}