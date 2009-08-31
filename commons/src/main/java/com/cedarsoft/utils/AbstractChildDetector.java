package com.cedarsoft.utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for child detectors
 *
 * @param <C> the type of the children
 * @param <P> the type of the parent
 */
public abstract class AbstractChildDetector<P, C> implements ChildDetector<P, C> {
  @NotNull
  private final List<ChildChangeListener<P>> listeners = new ArrayList<ChildChangeListener<P>>();

  /**
   * Notifies the listeners that the children have been changed for the given parent
   *
   * @param parent the parent
   */
  protected void notifyChildrenChangedFor( @NotNull P parent ) {
    for ( ChildChangeListener<P> listener : new ArrayList<ChildChangeListener<P>>( listeners ) ) {
      listener.notifyChildrenChangedFor( parent );
    }
  }

  public void addChangeListener( @NotNull ChildChangeListener<P> changeListener ) {
    listeners.add( changeListener );
  }

  public void removeChangeListener( @NotNull ChildChangeListener<P> changeListener ) {
    listeners.remove( changeListener );
  }
}
