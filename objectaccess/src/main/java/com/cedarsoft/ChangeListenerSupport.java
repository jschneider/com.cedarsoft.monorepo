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

package com.cedarsoft;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.Object;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Offers support for change listeners.
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 * @param <T> the type
 */
public class ChangeListenerSupport<T> {
  @NotNull
  private final ReadWriteLock lock = new ReentrantReadWriteLock();

  @NotNull
  protected final T observedObject;

  /**
   * <p>Constructor for ChangeListenerSupport.</p>
   *
   * @param observedObject a T object.
   */
  public ChangeListenerSupport( @NotNull T observedObject ) {
    this.observedObject = observedObject;
  }

  /**
   * <p>Getter for the field <code>transientListeners</code>.</p>
   *
   * @return a {@link List} object.
   */
  @NotNull
  protected List<ChangeListener<T>> getTransientListeners() {
    lock.readLock().lock();
    try {
      if ( transientListeners != null ) {
        //noinspection ReturnOfCollectionOrArrayField
        return transientListeners;
      }
    } finally {
      lock.readLock().unlock();
    }

    lock.writeLock().lock();
    try {
      if ( transientListeners == null ) {
        transientListeners = new ArrayList<ChangeListener<T>>();
      }
      //noinspection ReturnOfCollectionOrArrayField
      return transientListeners;
    } finally {
      lock.writeLock().unlock();
    }
  }

  private transient List<ChangeListener<T>> transientListeners;

  /**
   * <p>addChangeListener</p>
   *
   * @param listener a {@link ChangeListener} object.
   */
  public void addChangeListener( @NotNull ChangeListener<T> listener ) {
    lock.writeLock().lock();
    try {
      getTransientListeners().add( listener );
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * <p>removeChangeListener</p>
   *
   * @param listener a {@link ChangeListener} object.
   */
  public void removeChangeListener( @NotNull ChangeListener<T> listener ) {
    lock.writeLock().lock();
    try {
      getTransientListeners().remove( listener );
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * <p>changed</p>
   *
   * @param context        a {@link Object} object.
   * @param propertiesPath a {@link String} object.
   */
  public void changed( @Nullable Object context, @NotNull @NonNls String... propertiesPath ) {
    ChangedEvent<T> event = new ChangedEvent<T>( observedObject, context, propertiesPath );

    lock.writeLock().lock();
    try {
      List<ChangeListener<T>> listeners = getTransientListeners();
      for ( ChangeListener<T> listener : listeners ) {
        listener.entryChanged( event );
      }
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * <p>createPropertyListenerDelegate</p>
   *
   * @param propertiesPath a {@link String} object.
   * @return a {@link PropertyChangeListener} object.
   */
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
}
