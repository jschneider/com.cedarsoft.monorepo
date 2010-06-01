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

package com.cedarsoft.commons.struct;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * A node extends the interface {@link StructPart} with read/write support.
 */
public interface Node extends StructPart {
  @Override
  @NotNull
  List<? extends Node> getChildren();

  @Override
  @NotNull
  Node findChild( @NotNull @NonNls String childName ) throws ChildNotFoundException;

  /**
   * Returns the parent (if it has one)
   *
   * @return the parent
   */
  @Override
  @Nullable
  Node getParent();

  /**
   * Adds a child
   *
   * @param child the child
   */
  void addChild( @NotNull Node child );

  /**
   * Adds a child at the given position
   *
   * @param index the index
   * @param child the child
   */
  void addChild( int index, @NotNull Node child );

  /**
   * Detaches a child
   *
   * @param child the child
   */
  void detachChild( @NotNull Node child );

  /**
   * Detaches a child
   *
   * @param index the index of the child that is detached
   */
  void detachChild( int index );

  /**
   * Sets the parent
   *
   * @param parent the parent
   */
  void setParent( @Nullable Node parent );
}
