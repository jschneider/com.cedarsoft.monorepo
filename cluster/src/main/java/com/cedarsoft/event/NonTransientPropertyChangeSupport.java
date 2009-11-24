package com.cedarsoft.event;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A property change support that has non-transient listeners
 */
public class NonTransientPropertyChangeSupport {
  @NotNull
  private final List<PropertyChangeListener> listeners = new ArrayList<PropertyChangeListener>();
  @NotNull
  private final Object sourceBean;

  public NonTransientPropertyChangeSupport( @NotNull @NonNls Object sourceBean ) {
    this.sourceBean = sourceBean;
  }

  public void removePropertyChangeListener( @NotNull PropertyChangeListener listener ) {
    listeners.remove( listener );
  }

  public void removePropertyChangeListener( @NotNull @NonNls String propertyName, @NotNull PropertyChangeListener listener ) {
    getChild( propertyName ).removePropertyChangeListener( listener );
  }

  public void addPropertyChangeListener( @NotNull PropertyChangeListener listener ) {
    listeners.add( listener );
  }

  public void addPropertyChangeListener( @NotNull @NonNls String propertyName, @NotNull PropertyChangeListener listener ) {
    getChild( propertyName ).addPropertyChangeListener( listener );
  }

  public void firePropertyChange( @NotNull PropertyChangeEvent evt ) {
    for ( PropertyChangeListener listener : listeners ) {
      listener.propertyChange( evt );
    }

    NonTransientPropertyChangeSupport child = children.get( evt.getPropertyName() );
    if ( child != null ) {
      child.firePropertyChange( evt );
    }
  }

  @NotNull
  private NonTransientPropertyChangeSupport getChild( @NotNull @NonNls String propertyName ) {
    NonTransientPropertyChangeSupport child = children.get( propertyName );
    if ( child == null ) {
      child = new NonTransientPropertyChangeSupport( sourceBean );
      children.put( propertyName, child );
    }
    return child;
  }

  @NotNull
  private final Map<String, NonTransientPropertyChangeSupport> children = new HashMap<String, NonTransientPropertyChangeSupport>();

  @NotNull
  public List<? extends PropertyChangeListener> getPropertyChangeListeners() {
    return Collections.unmodifiableList( listeners );
  }
}
