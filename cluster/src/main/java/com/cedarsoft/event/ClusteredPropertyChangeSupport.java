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
