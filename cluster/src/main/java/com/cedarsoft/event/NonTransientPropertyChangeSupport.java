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
