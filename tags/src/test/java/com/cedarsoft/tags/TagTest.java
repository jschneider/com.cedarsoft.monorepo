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

package com.cedarsoft.tags;

import org.jetbrains.annotations.NotNull;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * <p/>
 * Date: Apr 2, 2007<br>
 * Time: 2:00:01 PM<br>
 */
public class TagTest {
  @Test
  public void testCreation() {
    Tag tag = new Tag( "asdf" );
    assertEquals( "asdf", tag.getDescription() );
  }

  @Test
  public void testTagSupport() {
    TagSet tagSet = new TagSet( this );
    tagSet.addTag( new Tag( "1" ) );
    assertEquals( "1", tagSet.getTags().iterator().next().getDescription() );
  }

  @Test
  public void testTagChangeListener() {
    TaggedObject taggedObject = new TaggedObject();

    final List<TagChangeListener.TagChangeEvent> events = new ArrayList<TagChangeListener.TagChangeEvent>();

    taggedObject.addTagChangeListener( new TagChangeListener() {
      @Override
      public void tagChanged( @NotNull TagChangeEvent event ) {
        events.add( event );
      }
    } );

    assertTrue( events.isEmpty() );

    Tag tag = new Tag( "asdf" );
    taggedObject.addTag( tag );
    assertEquals( 1, events.size() );
    assertEquals( "asdf", events.get( 0 ).getTag().getDescription() );
    assertEquals( TagChangeListener.TagEventType.ADD, events.get( 0 ).getType() );

    taggedObject.removeTag( tag );
    assertEquals( 2, events.size() );
    assertSame( tag, events.get( 1 ).getTag() );
    assertEquals( TagChangeListener.TagEventType.REMOVE, events.get( 1 ).getType() );

    assertTrue( taggedObject.getTags().isEmpty() );

    taggedObject.removeTag( new Tag( "doesNotExist" ) );
    assertEquals( 2, events.size() );

    //Object
    for ( TagChangeListener.TagChangeEvent event : events ) {
      assertSame( taggedObject, event.getSource() );
    }
  }
}
