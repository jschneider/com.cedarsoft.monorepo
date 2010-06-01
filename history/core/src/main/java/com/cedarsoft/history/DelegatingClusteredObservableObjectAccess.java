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

package com.cedarsoft.history;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;

/**
 *
 */
public abstract class DelegatingClusteredObservableObjectAccess<T> implements ClusteredObservableObjectAccess<T> {
  @Override
  @NotNull
  public ReadWriteLock getLock() {
    return getDelegate().getLock();
  }

  @Override
  public void commit( @NotNull T element ) {
    getDelegate().commit( element );
  }

  @Override
  public void remove( @NotNull T element ) {
    getDelegate().remove( element );
  }

  @Override
  public void add( @NotNull T element ) {
    getDelegate().add( element );
  }

  @Override
  public void setElements( @NotNull List<? extends T> elements ) {
    getDelegate().setElements( elements );
  }

  @Override
  @NotNull
  public List<? extends T> getElements() {
    return getDelegate().getElements();
  }

  @Override
  public void addElementListener( @NotNull ElementsListener<? super T> listener ) {
    getDelegate().addElementListener( listener );
  }

  @Override
  public void addElementListener( @NotNull ElementsListener<? super T> listener, boolean isTransient ) {
    getDelegate().addElementListener( listener, isTransient );
  }

  @Override
  public void removeElementListener( @NotNull ElementsListener<? super T> listener ) {
    getDelegate().removeElementListener( listener );
  }

  @Override
  @NotNull
  public List<? extends ElementsListener<? super T>> getTransientElementListeners() {
    return getDelegate().getTransientElementListeners();
  }

  /**
   * Returns the delegate
   *
   * @return the delegate
   */
  @NotNull
  public abstract ClusteredObservableObjectAccess<T> getDelegate();
}
