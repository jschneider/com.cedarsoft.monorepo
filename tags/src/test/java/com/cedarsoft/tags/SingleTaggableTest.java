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

package com.cedarsoft.tags;

import junit.framework.AssertionFailedError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

/**
 * <p/>
 * Date: May 7, 2007<br>
 * Time: 1:52:12 PM<br>
 */
public class SingleTaggableTest {
  private SingleTaggable taggable;

  @BeforeMethod
  protected void setUp() throws Exception {
    taggable = new SingleTaggable( this );
  }

  @AfterMethod
  protected void tearDown() throws Exception {

  }

  @Test
  public void testSetup() {
    assertNotNull( taggable );
  }

  @Test
  public void testSelection() {
    assertEquals( 0, taggable.getTags().size() );
    taggable.addTag( new Tag( "a" ) );
    assertEquals( 1, taggable.getTags().size() );
    taggable.addTag( new Tag( "b" ) );
    assertEquals( 1, taggable.getTags().size() );
    taggable.addTag( new Tag( "c" ) );
    assertEquals( 1, taggable.getTags().size() );
    taggable.setSelectedTag( new Tag( "d" ) );
    assertEquals( 1, taggable.getTags().size() );
    assertEquals( "d", taggable.getTags().get( 0 ).getDescription() );

    taggable.setSelectedTag( null );
    assertEquals( 0, taggable.getTags().size() );
  }

  @Test
  public void testListeners() {
    final List<TagChangeListener.TagChangeEvent> events = new ArrayList<TagChangeListener.TagChangeEvent>();
    taggable.addTagChangeListener( new TagChangeListener() {
      @Override
      public void tagChanged( @NotNull TagChangeEvent event ) {
        events.add( event );
      }
    } );

    taggable.addTag( new Tag( "a" ) );
    ensureEvents( events, null, "a" );
    taggable.addTag( new Tag( "b" ) );
    ensureEvents( events, "a", "b" );

    taggable.setSelectedTag( new Tag( "c" ) );
    ensureEvents( events, "b", "c" );
    taggable.setSelectedTag( null );
    ensureEvents( events, "c", null );
  }

  private static void ensureEvents( @NotNull List<TagChangeListener.TagChangeEvent> events, @Nullable String oldDescription, @Nullable String newDescription ) {
    assertFalse( events.isEmpty() );

    TagChangeListener.TagChangeEvent oldEvent;
    TagChangeListener.TagChangeEvent newEvent;

    if ( oldDescription != null && newDescription != null ) {
      assertEquals( 2, events.size() );
      oldEvent = events.get( 0 );
      newEvent = events.get( 1 );
    } else if ( oldDescription != null ) {
      assertEquals( 1, events.size() );
      oldEvent = events.get( 0 );
      newEvent = null;
    } else if ( newDescription != null ) {
      assertEquals( 1, events.size() );
      oldEvent = null;
      newEvent = events.get( 0 );
    } else {
      throw new AssertionFailedError( "Hmm" );
    }

    if ( oldEvent != null ) {
      assertEquals( 0, oldEvent.getIndex() );
      assertEquals( oldDescription, oldEvent.getTag().getDescription() );
      assertEquals( TagChangeListener.TagEventType.REMOVE, oldEvent.getType() );
    }

    if ( newEvent != null ) {
      assertEquals( 0, newEvent.getIndex() );
      assertEquals( newDescription, newEvent.getTag().getDescription() );
      assertEquals( TagChangeListener.TagEventType.ADD, newEvent.getType() );
    }

    events.clear();
  }

}
