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

package com.cedarsoft.collection;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Supplies support for list change support.
 */
public class ListChangeSupport<T> {
  private List<ListChangeListener<T>> listeners = new ArrayList<ListChangeListener<T>>();

  public boolean add( @NotNull List<T> backend, @NotNull T element ) {
    int index = backend.size();
    boolean returnValue = backend.add( element );
    fireAddEvent( index, element );
    return returnValue;
  }

  public void fireAddEvent( int index, @NotNull T element ) {
    if ( listeners.isEmpty() ) return;
    for ( ListChangeListener<T> listener : new ArrayList<ListChangeListener<T>>( listeners ) ) {
      listener.elementAdded( index, element );
    }
  }

  public void fireRemoveEvent( int index, @NotNull T element ) {
    if ( listeners.isEmpty() ) return;
    for ( ListChangeListener<T> listener : new ArrayList<ListChangeListener<T>>( listeners ) ) {
      listener.elementRemoved( index, element );
    }
  }

  public boolean remove( @NotNull List<T> backend, @NotNull T element ) {
    int index = backend.indexOf( element );
    boolean returnValue = backend.remove( element );
    fireRemoveEvent( index, element );
    return returnValue;
  }

  public void addListener( @NotNull ListChangeListener<T> listener ) {
    listeners.add( listener );
  }

  public void removeListener( @NotNull ListChangeListener<T> listener ) {
    listeners.remove( listener );
  }

  @NotNull
  public List<ListChangeListener<T>> getListeners() {
    return Collections.unmodifiableList( listeners );
  }
}
