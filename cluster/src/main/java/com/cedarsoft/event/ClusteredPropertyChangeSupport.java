/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce
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
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class ClusteredPropertyChangeSupport {
  @NotNull
  private final Object sourceBean;
  @NotNull
  private final PropertyChangeSupport transientSupport;
  @NotNull
  private final NonTransientPropertyChangeSupport nonTransientSupport;

  /**
   * <p>Constructor for ClusteredPropertyChangeSupport.</p>
   *
   * @param sourceBean a {@link java.lang.Object} object.
   */
  public ClusteredPropertyChangeSupport( @NotNull Object sourceBean ) {
    this.sourceBean = sourceBean;
    transientSupport = new PropertyChangeSupport( sourceBean );
    nonTransientSupport = new NonTransientPropertyChangeSupport( sourceBean );
  }

  /**
   * <p>removePropertyChangeListener</p>
   *
   * @param listener a {@link java.beans.PropertyChangeListener} object.
   */
  public void removePropertyChangeListener( @NotNull PropertyChangeListener listener ) {
    transientSupport.removePropertyChangeListener( listener );
    nonTransientSupport.removePropertyChangeListener( listener );
  }

  /**
   * <p>removePropertyChangeListener</p>
   *
   * @param propertyName a {@link java.lang.String} object.
   * @param listener a {@link java.beans.PropertyChangeListener} object.
   */
  public void removePropertyChangeListener( @NotNull @NonNls String propertyName, @NotNull PropertyChangeListener listener ) {
    transientSupport.removePropertyChangeListener( propertyName, listener );
    nonTransientSupport.removePropertyChangeListener( propertyName, listener );
  }

  /**
   * <p>addPropertyChangeListener</p>
   *
   * @param listener a {@link java.beans.PropertyChangeListener} object.
   */
  public void addPropertyChangeListener( @NotNull PropertyChangeListener listener ) {
    addPropertyChangeListener( listener, true );
  }

  /**
   * <p>addPropertyChangeListener</p>
   *
   * @param listener a {@link java.beans.PropertyChangeListener} object.
   * @param isTransient a boolean.
   */
  public void addPropertyChangeListener( @NotNull PropertyChangeListener listener, boolean isTransient ) {
    if ( isTransient ) {
      transientSupport.addPropertyChangeListener( listener );
    } else {
      nonTransientSupport.addPropertyChangeListener( listener );
    }
  }

  /**
   * <p>addPropertyChangeListener</p>
   *
   * @param propertyName a {@link java.lang.String} object.
   * @param listener a {@link java.beans.PropertyChangeListener} object.
   */
  public void addPropertyChangeListener( @NotNull @NonNls String propertyName, @NotNull PropertyChangeListener listener ) {
    addPropertyChangeListener( propertyName, listener, true );
  }

  /**
   * <p>addPropertyChangeListener</p>
   *
   * @param propertyName a {@link java.lang.String} object.
   * @param listener a {@link java.beans.PropertyChangeListener} object.
   * @param isTransient a boolean.
   */
  public void addPropertyChangeListener( @NotNull @NonNls String propertyName, @NotNull PropertyChangeListener listener, boolean isTransient ) {
    if ( isTransient ) {
      transientSupport.addPropertyChangeListener( propertyName, listener );
    } else {
      nonTransientSupport.addPropertyChangeListener( propertyName, listener );
    }
  }

  /**
   * <p>firePropertyChange</p>
   *
   * @param propertyName a {@link java.lang.String} object.
   * @param oldValue a {@link java.lang.Object} object.
   * @param newValue a {@link java.lang.Object} object.
   */
  public void firePropertyChange( @NotNull @NonNls String propertyName, @Nullable Object oldValue, @Nullable Object newValue ) {
    if ( oldValue != null && newValue != null && oldValue.equals( newValue ) ) {
      return;
    }
    firePropertyChange( createEvent( propertyName, oldValue, newValue ) );
  }

  /**
   * <p>firePropertyChange</p>
   *
   * @param evt a {@link java.beans.PropertyChangeEvent} object.
   */
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

  /**
   * <p>Getter for the field <code>sourceBean</code>.</p>
   *
   * @return a {@link java.lang.Object} object.
   */
  @NotNull
  public Object getSourceBean() {
    return sourceBean;
  }

  @NotNull
  PropertyChangeSupport getTransientSupport() {
    return transientSupport;
  }

  /**
   * <p>getNonTransientListeners</p>
   *
   * @return a {@link java.util.List} object.
   */
  @NotNull
  public List<? extends PropertyChangeListener> getNonTransientListeners() {
    return Collections.unmodifiableList( nonTransientSupport.getPropertyChangeListeners() );
  }

  /**
   * <p>getTransientListeners</p>
   *
   * @return a {@link java.util.List} object.
   */
  @NotNull
  public List<? extends PropertyChangeListener> getTransientListeners() {
    return Collections.unmodifiableList( Arrays.asList( getTransientSupport().getPropertyChangeListeners() ) );
  }
}
