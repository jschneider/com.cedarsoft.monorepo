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

package com.cedarsoft;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * A change listener support with transient and non transient listeners support
 */
public class ClusteredChangeListenerSupport<T> {
  @NotNull
  private final ChangeListenerSupport<T> transientSupport;
  @NotNull
  private final NonTransientChangeListenerSupport<T> nonTransientSupport;

  @Nullable
  private ContextProvider contextProvider;

  public ClusteredChangeListenerSupport( @NotNull T observerdObject ) {
    this( observerdObject, null );
  }

  public ClusteredChangeListenerSupport( @NotNull T observerdObject, @Nullable ContextProvider contextProvider ) {
    this.contextProvider = contextProvider;
    transientSupport = new ChangeListenerSupport<T>( observerdObject );
    nonTransientSupport = new NonTransientChangeListenerSupport<T>( observerdObject );
  }

  public void addChangeListener( @NotNull ChangeListener<T> listener, boolean isTransient ) {
    if ( isTransient ) {
      transientSupport.addChangeListener( listener );
    } else {
      nonTransientSupport.addChangeListener( listener );
    }
  }

  public void removeChangeListener( @NotNull ChangeListener<T> listener ) {
    transientSupport.removeChangeListener( listener );
    nonTransientSupport.removeChangeListener( listener );
  }

  /**
   * Uses the set context provider to recieve the context
   *
   * @param propertiesPath the properties path
   */
  public void changed( @NonNls @NotNull String... propertiesPath ) {
    ContextProvider provider = getContextProvider();
    if ( provider != null ) {
      changed( provider.getContext(), propertiesPath );
    } else {
      changed( null, propertiesPath );
    }
  }

  public void changed( @Nullable Object context, @NonNls @NotNull String... propertiesPath ) {
    if ( propertiesPath.length == 0 ) {
      throw new IllegalArgumentException( "Empty properties path" );
    }

    transientSupport.changed( context, propertiesPath );
    nonTransientSupport.changed( context, propertiesPath );
  }

  @Nullable
  public ContextProvider getContextProvider() {
    return contextProvider;
  }

  public void setContextProvider( @Nullable ContextProvider contextProvider ) {
    this.contextProvider = contextProvider;
  }

  @NotNull
  public PropertyChangeListener createPropertyListenerDelegate( @NotNull @NonNls String... propertiesPath ) {
    final String[] actual = new String[propertiesPath.length + 1];
    System.arraycopy( propertiesPath, 0, actual, 0, propertiesPath.length );

    return new PropertyChangeListener() {
      @Override
      public void propertyChange( PropertyChangeEvent evt ) {
        actual[actual.length - 1] = evt.getPropertyName();
        changed( actual );
      }
    };
  }

  /**
   * Provides the context
   */
  public interface ContextProvider {
    @Nullable
    Object getContext();
  }

}
