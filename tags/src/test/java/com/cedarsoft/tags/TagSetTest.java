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

import com.cedarsoft.EasyMockTemplate;
import com.cedarsoft.ReflectionMatcher;
import org.easymock.EasyMock;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class TagSetTest {
  private static final String SOURCE = "the source";
  private static final Tag A = new Tag( "a" );
  private static final Tag B = new Tag( "b" );
  private static final Tag C = new Tag( "c" );
  private static final Tag D = new Tag( "d" );

  @Test
  public void testDoNothingSet() throws Exception {
    final TagSet set = new TagSet( SOURCE );

    set.addTag( A );
    set.addTag( B );

    final TagChangeListener listener = EasyMock.createMock( TagChangeListener.class );

    new EasyMockTemplate( listener ) {
      @Override
      protected void expectations() {

      }

      @Override
      protected void codeToTest() {
        set.addTagChangeListener( listener );

        List<Tag> tags = new ArrayList<Tag>( set.getTags() );
        set.setTags( tags );

      }
    }.run();
  }

  @Test
  public void testComleteNewSet() throws Exception {
    final TagSet set = new TagSet( SOURCE );

    set.addTag( A );
    set.addTag( B );

    final TagChangeListener listener = EasyMock.createMock( TagChangeListener.class );

    new EasyMockTemplate( listener ) {
      @Override
      protected void expectations() {
        listener.tagChanged( ReflectionMatcher.create( new TagChangeListener.TagChangeEvent( SOURCE, TagChangeListener.TagEventType.REMOVE, A, 0 ) ) );
        listener.tagChanged( ReflectionMatcher.create( new TagChangeListener.TagChangeEvent( SOURCE, TagChangeListener.TagEventType.REMOVE, B, 0 ) ) );
        listener.tagChanged( ReflectionMatcher.create( new TagChangeListener.TagChangeEvent( SOURCE, TagChangeListener.TagEventType.ADD, C, 0 ) ) );
        listener.tagChanged( ReflectionMatcher.create( new TagChangeListener.TagChangeEvent( SOURCE, TagChangeListener.TagEventType.ADD, D, 1 ) ) );
      }

      @Override
      protected void codeToTest() {
        set.addTagChangeListener( listener );
        set.setTags( C, D );
      }
    }.run();
  }

  @Test
  public void testsetMerge() throws Exception {
    final TagSet set = new TagSet( SOURCE );

    set.addTag( A );
    set.addTag( B );

    final TagChangeListener listener = EasyMock.createMock( TagChangeListener.class );

    new EasyMockTemplate( listener ) {
      @Override
      protected void expectations() {
        listener.tagChanged( ReflectionMatcher.create( new TagChangeListener.TagChangeEvent( SOURCE, TagChangeListener.TagEventType.REMOVE, A, 0 ) ) );
        listener.tagChanged( ReflectionMatcher.create( new TagChangeListener.TagChangeEvent( SOURCE, TagChangeListener.TagEventType.ADD, C, 1 ) ) );
      }

      @Override
      protected void codeToTest() {
        set.addTagChangeListener( listener );

        List<Tag> tags = new ArrayList<Tag>( set.getTags() );
        tags.remove( 0 );
        tags.add( C );

        set.setTags( tags );
      }
    }.run();
  }


}
