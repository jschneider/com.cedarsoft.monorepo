package com.cedarsoft.utils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Implementations detect all children of an element that shall be added to the recursion
 *
 * @param <C> the type of the children
 * @param <P> the type of the parent
 */
public interface ChildDetector<P, C> {
  /**
   * Finds the children for the given parent
   *
   * @param parent the parent
   * @return the children
   */
  @NotNull
  List<? extends C> findChildren( @NotNull P parent );

  /**
   * Registers a change listener that is notified when the child detector changes its children
   *
   * @param changeListener the listener
   */
  void addChangeListener( @NotNull ChildChangeListener<P> changeListener );

  /**
   * Removes the change listener
   *
   * @param changeListener the change listener that is removed
   */
  void removeChangeListener( @NotNull ChildChangeListener<P> changeListener );
}
