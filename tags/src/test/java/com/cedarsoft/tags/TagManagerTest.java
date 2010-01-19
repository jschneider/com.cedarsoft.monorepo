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

import com.cedarsoft.NotFoundException;
import org.jetbrains.annotations.NotNull;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

/**
 * <p/>
 * Date: Apr 2, 2007<br>
 * Time: 2:33:07 PM<br>
 */
public class TagManagerTest {
  private DefaultTagManager<Object> manager;

  @BeforeMethod
  protected void setUp() throws Exception {
    manager = new DefaultTagManager<Object>();
  }

  @AfterMethod
  protected void tearDown() throws Exception {

  }

  @Test
  public void testTagManagerListeners() {
    final List<TagChangeListener.TagChangeEvent> events = new ArrayList<TagChangeListener.TagChangeEvent>();

    manager.addTagChangeListener( new TagChangeListener() {
      @Override
      public void tagChanged( @NotNull TagChangeEvent event ) {
        events.add( event );
      }
    } );

    //Add the first tag
    manager.getTag( "a" );
    assertEquals( 1, manager.getTags().size() );
    assertEquals( "a", manager.getTags().get( 0 ).getDescription() );

    {
      assertEquals( 1, events.size() );
      TagChangeListener.TagChangeEvent event = events.get( 0 );
      assertEquals( 0, event.getIndex() );
      assertEquals( "a", event.getTag().getDescription() );
    }

    events.clear();

    //Add the second tag
    manager.getTag( "b" );
    assertEquals( 2, manager.getTags().size() );
    assertEquals( "a", manager.getTags().get( 0 ).getDescription() );
    assertEquals( "b", manager.getTags().get( 1 ).getDescription() );
    {
      assertEquals( 1, events.size() );
      TagChangeListener.TagChangeEvent event = events.get( 0 );
      assertEquals( 1, event.getIndex() );
      assertEquals( "b", event.getTag().getDescription() );
    }

    events.clear();

    //Add the third tag
    manager.getTag( "c" );
    assertEquals( 3, manager.getTags().size() );
    assertEquals( "a", manager.getTags().get( 0 ).getDescription() );
    assertEquals( "b", manager.getTags().get( 1 ).getDescription() );
    assertEquals( "c", manager.getTags().get( 2 ).getDescription() );
    {
      assertEquals( 1, events.size() );
      TagChangeListener.TagChangeEvent event = events.get( 0 );
      assertEquals( 2, event.getIndex() );
      assertEquals( "c", event.getTag().getDescription() );
    }

    events.clear();


    //Remove the first tag
    manager.removeTag( "a" );

    assertEquals( 2, manager.getTags().size() );
    assertEquals( "b", manager.getTags().get( 0 ).getDescription() );
    assertEquals( "c", manager.getTags().get( 1 ).getDescription() );
    {
      assertEquals( 1, events.size() );
      TagChangeListener.TagChangeEvent event = events.get( 0 );
      assertEquals( 0, event.getIndex() );
      assertEquals( "a", event.getTag().getDescription() );
    }

    events.clear();

    //Remove the c tag
    manager.removeTag( "c" );

    assertEquals( 1, manager.getTags().size() );
    assertEquals( "b", manager.getTags().get( 0 ).getDescription() );
    {
      assertEquals( 1, events.size() );
      TagChangeListener.TagChangeEvent event = events.get( 0 );
      assertEquals( 1, event.getIndex() );
      assertEquals( "c", event.getTag().getDescription() );
    }

    events.clear();

    //Remove the last tag
    manager.removeTag( "b" );

    assertEquals( 0, manager.getTags().size() );
    {
      assertEquals( 1, events.size() );
      TagChangeListener.TagChangeEvent event = events.get( 0 );
      assertEquals( 0, event.getIndex() );
      assertEquals( "b", event.getTag().getDescription() );
    }
  }

  @Test
  public void testTaggableListeners() {
    final List<TagChangeListener.TagChangeEvent> events = new ArrayList<TagChangeListener.TagChangeEvent>();

    Object object = new Object();

    Taggable taggable = manager.getTaggable( object );
    taggable.addTagChangeListener( new TagChangeListener() {
      @Override
      public void tagChanged( @NotNull TagChangeEvent event ) {
        events.add( event );
      }
    } );

    assertEquals( 0, taggable.getTags().size() );
    manager.addTag( object, "tag0" );
    assertEquals( 1, manager.getTaggable( object ).getTags().size() );

    {
      assertEquals( 1, events.size() );
      TagChangeListener.TagChangeEvent event = events.get( 0 );
      assertEquals( 0, event.getIndex() );
      assertEquals( "tag0", event.getTag().getDescription() );
      assertEquals( TagChangeListener.TagEventType.ADD, event.getType() );
    }

    events.clear();

    assertEquals( 1, manager.getTaggable( object ).getTags().size() );
    manager.addTag( object, "tag1" );
    assertEquals( 2, manager.getTaggable( object ).getTags().size() );

    {
      assertEquals( 1, events.size() );
      TagChangeListener.TagChangeEvent event = events.get( 0 );
      assertEquals( 1, event.getIndex() );
      assertEquals( "tag1", event.getTag().getDescription() );
      assertEquals( TagChangeListener.TagEventType.ADD, event.getType() );
    }

    events.clear();

    assertEquals( 2, manager.getTaggable( object ).getTags().size() );
    manager.removeTag( object, "tag0" );
    assertEquals( 1, manager.getTaggable( object ).getTags().size() );

    {
      assertEquals( 1, events.size() );
      TagChangeListener.TagChangeEvent event = events.get( 0 );
      assertEquals( 0, event.getIndex() );
      assertEquals( "tag0", event.getTag().getDescription() );
      assertEquals( TagChangeListener.TagEventType.REMOVE, event.getType() );
    }

    events.clear();
    assertEquals( 1, manager.getTaggable( object ).getTags().size() );
    manager.removeTag( object, "tag1" );
    assertEquals( 0, manager.getTaggable( object ).getTags().size() );

    {
      assertEquals( 1, events.size() );
      TagChangeListener.TagChangeEvent event = events.get( 0 );
      assertEquals( 0, event.getIndex() );
      assertEquals( "tag1", event.getTag().getDescription() );
      assertEquals( TagChangeListener.TagEventType.REMOVE, event.getType() );
    }
  }

  @Test
  public void testCreate() {
    Object o = new Object();
    Taggable taggable = manager.getTaggable( o );
    assertNotNull( taggable );
    taggable.addTag( new Tag( "asdf" ) );

    assertEquals( "asdf", manager.findTagged( o ).getTags().iterator().next().getDescription() );
    assertEquals( "asdf", manager.findTaggable( o ).getTags().iterator().next().getDescription() );
  }

  public void _testWeak() {
    for ( int i = 0; i < 1000; i++ ) {
      assertEquals( 0, manager.getTags().size() );
      query();
      for ( int j = 0; j < 20; j++ ) {
        System.gc();
      }
      assertEquals( 0, manager.getTags().size() );
    }
  }

  private void query() {
    Object o = new Object();
    assertNotNull( manager.getTaggable( o ) );
    assertEquals( 1, manager.getTags().size() );
    o = null;
    for ( int i = 0; i < 20; i++ ) {
      System.gc();
    }
  }

  @Test
  public void testLookup() {
    Object o = new Object();
    try {
      manager.findTaggable( o );
      fail( "Where is the Exception" );
    } catch ( NotFoundException ignore ) {
    }

    try {
      manager.findTagged( o );
      fail( "Where is the Exception" );
    } catch ( NotFoundException ignore ) {
    }
  }
}
