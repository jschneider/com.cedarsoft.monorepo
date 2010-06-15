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

import com.cedarsoft.lookup.Lookup;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * This node is linked to other nodes.
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class LinkedNode implements Node {
  @NotNull
  private final Node source;
  private Node parent;

  /**
   * <p>Constructor for LinkedNode.</p>
   *
   * @param source a {@link com.cedarsoft.commons.struct.Node} object.
   */
  public LinkedNode( @NotNull Node source ) {
    this.source = source;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nullable
  public Node getParent() {
    return parent;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setParent( @Nullable Node parent ) {
    this.parent = parent;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasParent() {
    return parent != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public Lookup getLookup() {
    return source.getLookup();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  @NonNls
  public String getName() {
    return source.getName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public List<? extends Node> getChildren() {
    return source.getChildren();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addChild( int index, @NotNull Node child ) {
    source.addChild( index, child );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addChild( @NotNull Node child ) {
    source.addChild( child );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void detachChild( @NotNull Node child ) {
    source.detachChild( child );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void detachChild( int index ) {
    source.detachChild( index );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public Node findChild( @NotNull @NonNls String childName ) throws ChildNotFoundException {
    return source.findChild( childName );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public Path getPath() {
    return Path.buildPath( this );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isChild( @NotNull StructPart child ) {
    return source.isChild( child );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addStructureListener( @NotNull StructureListener structureListener ) {
    source.addStructureListener( structureListener );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addStructureListenerWeak( @NotNull StructureListener structureListener ) {
    source.addStructureListenerWeak( structureListener );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeStructureListener( @NotNull StructureListener structureListener ) {
    source.removeStructureListener( structureListener );
  }
}
