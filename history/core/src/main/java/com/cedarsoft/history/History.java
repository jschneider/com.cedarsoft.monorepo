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

package com.cedarsoft.history;

import com.cedarsoft.WriteableObjectAccess;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A history contains several entries - each with a validation date
 */
public interface History<E extends HistoryEntry> extends WriteableObjectAccess<E> {
  @NotNull
  @NonNls
  String PROPERTY_ENTRIES = "entries";
  @NotNull
  @NonNls
  String PROPERTY_FIRST_ENTRIES = "firstEntry";

  /**
   * Returns all entries of the history
   *
   * @return the entries
   */
  @NotNull
  List<? extends E> getEntries();

  /**
   * Whether the history contains any entries
   */

  boolean hasEntries();

  /**
   * Adds an entry
   *
   * @param entry the entry that is added
   */
  void addEntry( @NotNull E entry );

  /**
   * Commits the entry
   *
   * @param entry the entry that has been changed
   */
  void commitEntry( @NotNull E entry );

  /**
   * Returns the latest entry
   *
   * @return the latest entry
   *
   * @throws NoValidElementFoundException
   */
  @NotNull
  E getLatestEntry() throws NoValidElementFoundException;

  /**
   * Registers an history listener
   *
   * @param historyListener the listener that is registered
   */
  void addHistoryListener( @NotNull HistoryListener<E> historyListener );

  @NotNull
  List<? extends HistoryListener<E>> getHistoryListeners();

  /**
   * Removes an history listener
   *
   * @param historyListener the listener that is removed
   */
  void removeHistoryListener( @NotNull HistoryListener<E> historyListener );

  /**
   * Returns whether the given entry is the latest entry
   *
   * @param entry the entry
   * @return whether the given entry is the latest entry
   */
  boolean isLatestEntry( @NotNull E entry );

  /**
   * Removes the entry
   *
   * @param entry the entry that is removed
   */
  boolean removeEntry( @NotNull E entry );

  /**
   * Returns the first entry
   *
   * @return the first entry
   *
   * @throws NoValidElementFoundException if no entry is available within this history
   */
  @NotNull
  E getFirstEntry() throws NoValidElementFoundException;

  /**
   * Removes all entries
   */
  void clear();
}