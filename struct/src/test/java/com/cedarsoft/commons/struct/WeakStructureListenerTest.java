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

package com.cedarsoft.commons.struct;

import org.jetbrains.annotations.NotNull;
import org.testng.annotations.*;

import static org.testng.Assert.*;

/**
 *
 */
public class WeakStructureListenerTest {
  private DefaultNode node;

  @Test
  public void testAdd() {
    node.addStructureListener( new StructureListener() {
      @Override
      public void childAdded( @NotNull StructureChangedEvent event ) {

      }

      @Override
      public void childDetached( @NotNull StructureChangedEvent event ) {
      }
    } );

    assertEquals( 1, node.getChildrenSupport().getStructureListeners().size() );
  }

  @Test
  public void testAddRemoveWeakListener() {
    StructureListener listener = new StructureListener() {
      @Override
      public void childAdded( @NotNull StructureChangedEvent event ) {
      }

      @Override
      public void childDetached( @NotNull StructureChangedEvent event ) {
      }
    };
    node.addStructureListenerWeak( listener );
    assertEquals( 1, node.getChildrenSupport().getStructureListeners().size() );
    WeakStructureListener weakStructureListener = ( WeakStructureListener ) node.getChildrenSupport().getStructureListeners().get( 0 );
    assertSame( listener, weakStructureListener.getWrappedListener() );

    node.removeStructureListener( listener );
    assertEquals( 0, node.getChildrenSupport().getStructureListeners().size() );
  }

  @Test
  public void testAutoRemoving() {
    assertEquals( 0, node.getChildrenSupport().getStructureListeners().size() );
    node.addStructureListenerWeak( new StructureListener() {
      @Override
      public void childAdded( @NotNull StructureChangedEvent event ) {
      }

      @Override
      public void childDetached( @NotNull StructureChangedEvent event ) {
      }
    } );
    assertEquals( 1, node.getChildrenSupport().getStructureListeners().size() );
    System.gc();
    System.gc();
    System.gc();
    System.gc();
    System.gc();
    node.addChild( new DefaultNode( "asf" ) );
    assertEquals( 0, node.getChildrenSupport().getStructureListeners().size() );
  }

  @BeforeMethod
  protected void setUp() throws Exception {
    node = new DefaultNode( "node" );
  }
}
