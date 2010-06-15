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

/**
 * Clustered history listener support
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class ClusteredHistoryListenerSupport<E> {
  @NotNull
  private final HistoryListenerSupport<E> transientDelegate = new HistoryListenerSupport<E>();
  @NotNull
  private final NonTransientHistoryListenerSupport<E> nonTransientDelegate = new NonTransientHistoryListenerSupport<E>();


  /**
   * <p>addHistoryListener</p>
   *
   * @param historyListener a {@link com.cedarsoft.history.HistoryListener} object.
   * @param isTransient a boolean.
   */
  public void addHistoryListener( @NotNull HistoryListener<E> historyListener, boolean isTransient ) {
    if ( isTransient ) {
      transientDelegate.addHistoryListener( historyListener );
    } else {
      nonTransientDelegate.addHistoryListener( historyListener );
    }
  }

  /**
   * <p>removeHistoryListener</p>
   *
   * @param historyListener a {@link com.cedarsoft.history.HistoryListener} object.
   */
  public void removeHistoryListener( @NotNull HistoryListener<E> historyListener ) {
    transientDelegate.removeHistoryListener( historyListener );
    nonTransientDelegate.removeHistoryListener( historyListener );
  }

  /**
   * <p>notifyEntryChanged</p>
   *
   * @param entry a E object.
   */
  public void notifyEntryChanged( @NotNull E entry ) {
    transientDelegate.notifyEntryChanged( entry );
    nonTransientDelegate.notifyEntryChanged( entry );
  }

  /**
   * <p>notifyEntryAdded</p>
   *
   * @param entry a E object.
   */
  public void notifyEntryAdded( @NotNull E entry ) {
    transientDelegate.notifyEntryAdded( entry );
    nonTransientDelegate.notifyEntryAdded( entry );
  }

  /**
   * <p>notifyEntryRemoved</p>
   *
   * @param entry a E object.
   */
  public void notifyEntryRemoved( @NotNull E entry ) {
    transientDelegate.notifyEntryRemoved( entry );
    nonTransientDelegate.notifyEntryRemoved( entry );
  }

  /**
   * <p>getTransientHistoryListeners</p>
   *
   * @return a {@link java.util.List} object.
   */
  @NotNull
  public List<? extends HistoryListener<E>> getTransientHistoryListeners() {
    return transientDelegate.getHistoryListeners();
  }
}
