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

package com.cedarsoft.tags.ui;

import com.cedarsoft.tags.DefaultTagManager;
import com.cedarsoft.tags.TagManager;
import com.cedarsoft.tags.Taggable;
import org.testng.annotations.*;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

/**
 * <p/>
 * Date: May 3, 2007<br>
 * Time: 3:09:38 PM<br>
 */
public class TagListModelTest {
  private TagManager<Object> tagManager;
  private final Object object = "asdf";
  private TagListModel model;
  private Taggable taggable;

  @BeforeMethod
  protected void setUp() throws Exception {
    tagManager = new DefaultTagManager<Object>();
    taggable = tagManager.getTaggable( object );
    model = new TagListModel( taggable, true );
  }

  @AfterMethod
  protected void tearDown() throws Exception {

  }

  @Test
  public void testNotNull() {
    assertEquals( 1, model.getSize() );
    model = new TagListModel( taggable, false );
    assertEquals( 0, model.getSize() );
  }

  @Test
  public void testNullable() {
    assertEquals( 1, model.getSize() );
    assertNull( model.getElementAt( 0 ) );
  }

  @Test
  public void testIt() {
    assertEquals( 1, model.getSize() );
    taggable.addTag( tagManager.getTag( "a" ) );
    taggable.addTag( tagManager.getTag( "b" ) );
    taggable.addTag( tagManager.getTag( "c" ) );
    assertEquals( 4, model.getSize() );
    assertNull( model.getElementAt( 0 ) );
    assertEquals( "a", model.getElementAt( 1 ).getDescription() );
    assertEquals( "b", model.getElementAt( 2 ).getDescription() );
    assertEquals( "c", model.getElementAt( 3 ).getDescription() );
  }

  @Test
  public void testListeners() {
    assertEquals( 1, model.getSize() );
    taggable.addTag( tagManager.getTag( "a" ) );
    taggable.addTag( tagManager.getTag( "b" ) );
    taggable.addTag( tagManager.getTag( "c" ) );
    assertEquals( 4, model.getSize() );

    final List<ListDataEvent> events = new ArrayList<ListDataEvent>();

    model.addListDataListener( new ListDataListener() {
      @Override
      public void intervalAdded( ListDataEvent e ) {
        events.add( e );
      }

      @Override
      public void intervalRemoved( ListDataEvent e ) {
        events.add( e );
      }

      @Override
      public void contentsChanged( ListDataEvent e ) {
        events.add( e );
      }
    } );

    //Add a tag
    {
      taggable.addTag( tagManager.getTag( "d" ) );
      assertEquals( 1, events.size() );
      ListDataEvent event = events.get( 0 );

      assertEquals( 3, event.getIndex0() );
      assertEquals( 3, event.getIndex1() );
      assertEquals( ListDataEvent.INTERVAL_ADDED, event.getType() );
    }

    events.clear();

    //Remove a tag
    {
      taggable.removeTag( tagManager.getTag( "b" ) );
      assertEquals( 1, events.size() );
      ListDataEvent event = events.get( 0 );

      assertEquals( 1, event.getIndex0() );
      assertEquals( 1, event.getIndex1() );
      assertEquals( ListDataEvent.INTERVAL_REMOVED, event.getType() );
    }
  }

  public static void main( String[] args ) throws Exception {
    TagListModelTest test = new TagListModelTest();
    test.setUp();
    test.taggable.addTag( test.tagManager.getTag( "a" ) );
    test.taggable.addTag( test.tagManager.getTag( "b" ) );
    test.taggable.addTag( test.tagManager.getTag( "c" ) );

    JFrame frame = new JFrame();
    frame.setSize( 800, 600 );

    JList jList = new JList( test.model );
    jList.setCellRenderer( new TagListCellRenderer() );
    frame.getContentPane().add( jList );
    frame.setVisible( true );

    Thread.sleep( 1000 );
    test.taggable.addTag( test.tagManager.getTag( "d" ) );
    test.taggable.addTag( test.tagManager.getTag( "e" ) );
    test.taggable.addTag( test.tagManager.getTag( "f" ) );

    Thread.sleep( 1000 );
    test.taggable.removeTag( test.tagManager.getTag( "a" ) );
    test.taggable.removeTag( test.tagManager.getTag( "c" ) );
    test.taggable.removeTag( test.tagManager.getTag( "e" ) );
  }
}
