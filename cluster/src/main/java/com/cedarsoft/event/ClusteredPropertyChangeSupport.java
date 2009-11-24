package com.cedarsoft.event;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A special property change support that offers both transient and non-transient listeners.
 * This support is useful in conjunction with terracotta
 */
public class ClusteredPropertyChangeSupport {
  @NotNull
  private final Object sourceBean;
  @NotNull
  private final PropertyChangeSupport transientSupport;
  @NotNull
  private final NonTransientPropertyChangeSupport nonTransientSupport;

  public ClusteredPropertyChangeSupport( @NotNull Object sourceBean ) {
    this.sourceBean = sourceBean;
    transientSupport = new PropertyChangeSupport( sourceBean );
    nonTransientSupport = new NonTransientPropertyChangeSupport( sourceBean );
  }

  public void removePropertyChangeListener( @NotNull PropertyChangeListener listener ) {
    transientSupport.removePropertyChangeListener( listener );
    nonTransientSupport.removePropertyChangeListener( listener );
  }

  public void removePropertyChangeListener( @NotNull @NonNls String propertyName, @NotNull PropertyChangeListener listener ) {
    transientSupport.removePropertyChangeListener( propertyName, listener );
    nonTransientSupport.removePropertyChangeListener( propertyName, listener );
  }

  public void addPropertyChangeListener( @NotNull PropertyChangeListener listener ) {
    addPropertyChangeListener( listener, true );
  }

  public void addPropertyChangeListener( @NotNull PropertyChangeListener listener, boolean isTransient ) {
    if ( isTransient ) {
      transientSupport.addPropertyChangeListener( listener );
    } else {
      nonTransientSupport.addPropertyChangeListener( listener );
    }
  }

  public void addPropertyChangeListener( @NotNull @NonNls String propertyName, @NotNull PropertyChangeListener listener ) {
    addPropertyChangeListener( propertyName, listener, true );
  }

  public void addPropertyChangeListener( @NotNull @NonNls String propertyName, @NotNull PropertyChangeListener listener, boolean isTransient ) {
    if ( isTransient ) {
      transientSupport.addPropertyChangeListener( propertyName, listener );
    } else {
      nonTransientSupport.addPropertyChangeListener( propertyName, listener );
    }
  }

  public void firePropertyChange( @NotNull @NonNls String propertyName, @Nullable Object oldValue, @Nullable Object newValue ) {
    if ( oldValue != null && newValue != null && oldValue.equals( newValue ) ) {
      return;
    }
    firePropertyChange( createEvent( propertyName, oldValue, newValue ) );
  }

  public void firePropertyChange( @NotNull PropertyChangeEvent evt ) {
    Object oldValue = evt.getOldValue();
    Object newValue = evt.getNewValue();
    if ( oldValue != null && newValue != null && oldValue.equals( newValue ) ) {
      return;
    }

    transientSupport.firePropertyChange( evt );
    nonTransientSupport.firePropertyChange( evt );
  }

  /**
   * Creates the event
   *
   * @param propertyName the name
   * @param oldValue     the old value
   * @param newValue     the new value
   * @return the event
   */
  @NotNull
  protected PropertyChangeEvent createEvent( @NotNull @NonNls String propertyName, @Nullable Object oldValue, @Nullable Object newValue ) {
    return new PropertyChangeEvent( sourceBean, propertyName, oldValue, newValue );
  }

  @NotNull
  public Object getSourceBean() {
    return sourceBean;
  }

  @NotNull
  PropertyChangeSupport getTransientSupport() {
    return transientSupport;
  }

  @NotNull
  public List<? extends PropertyChangeListener> getNonTransientListeners() {
    return Collections.unmodifiableList( nonTransientSupport.getPropertyChangeListeners() );
  }

  @NotNull
  public List<? extends PropertyChangeListener> getTransientListeners() {
    return Collections.unmodifiableList( Arrays.asList( getTransientSupport().getPropertyChangeListeners() ) );
  }
}
