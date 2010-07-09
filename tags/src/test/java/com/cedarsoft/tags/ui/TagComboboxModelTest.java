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
import com.cedarsoft.tags.TagChangeListener;
import com.cedarsoft.tags.TagManager;
import com.cedarsoft.tags.Taggable;
import org.jetbrains.annotations.NotNull;
import org.junit.*;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * <p/>
 * Date: May 3, 2007<br>
 * Time: 3:26:29 PM<br>
 */
public class TagComboboxModelTest {
  private TagManager<Object> tagManager;
  private final Object object = "asdf";
  private Taggable taggable;
  private TagComboBoxModel comboBoxModel;

  @Before
  public void setUp() throws Exception {
    tagManager = new DefaultTagManager<Object>();
    taggable = tagManager.getTaggable( object );
    comboBoxModel = new TagComboBoxModel( tagManager, false );
  }

  @After
  public void tearDown() throws Exception {

  }

  @Test
  public void testTaggable() {
    assertEquals( 0, comboBoxModel.getSize() );
    tagManager.getTag( "a" );
    tagManager.getTag( "b" );
    tagManager.getTag( "c" );
    assertEquals( 3, comboBoxModel.getSize() );


    Taggable selection = comboBoxModel.getSelectionTaggable();
    assertNotNull( selection );
    assertTrue( selection.getTags().isEmpty() );

    final List<TagChangeListener.TagChangeEvent> events = new ArrayList<TagChangeListener.TagChangeEvent>();
    selection.addTagChangeListener( new TagChangeListener() {
      @Override
      public void tagChanged( @NotNull TagChangeEvent event ) {
        events.add( event );
      }
    } );

    assertTrue( events.isEmpty() );
    comboBoxModel.setSelectedItem( comboBoxModel.getElementAt( 1 ) );

    {
      assertEquals( 1, events.size() );
      TagChangeListener.TagChangeEvent event = events.get( 0 );
      assertEquals( 0, event.getIndex() );
      assertEquals( TagChangeListener.TagEventType.ADD, event.getType() );
    }

    events.clear();

    //Select another
    comboBoxModel.setSelectedItem( comboBoxModel.getElementAt( 2 ) );
    assertEquals( 2, events.size() );

    {
      TagChangeListener.TagChangeEvent event = events.get( 0 );
      assertEquals( 0, event.getIndex() );
      assertEquals( TagChangeListener.TagEventType.REMOVE, event.getType() );
    }

    {
      TagChangeListener.TagChangeEvent event = events.get( 1 );
      assertEquals( 0, event.getIndex() );
      assertEquals( TagChangeListener.TagEventType.ADD, event.getType() );
    }
  }

  @Test
  public void testNullable() {
    comboBoxModel = new TagComboBoxModel( taggable, true );
    assertEquals( 1, comboBoxModel.getSize() );
    assertNull( comboBoxModel.getElementAt( 0 ) );
  }

  @Test
  public void testIt() {
    assertEquals( 0, comboBoxModel.getSize() );
    assertNull( comboBoxModel.getSelectedItem() );

    taggable.addTag( tagManager.getTag( "a" ) );
    taggable.addTag( tagManager.getTag( "b" ) );
    taggable.addTag( tagManager.getTag( "c" ) );

    assertEquals( 3, comboBoxModel.getSize() );
    assertEquals( "a", comboBoxModel.getElementAt( 0 ).getDescription() );
    assertEquals( "b", comboBoxModel.getElementAt( 1 ).getDescription() );
    assertEquals( "c", comboBoxModel.getElementAt( 2 ).getDescription() );
    assertNull( comboBoxModel.getSelectedItem() );

    comboBoxModel.setSelectedItem( taggable.getTags().get( 1 ) );
    assertEquals( "b", comboBoxModel.getSelectedItem().getDescription() );
  }

  public static void main( String[] args ) throws Exception {
    TagComboboxModelTest test = new TagComboboxModelTest();
    test.setUp();
    test.taggable.addTag( test.tagManager.getTag( "a" ) );
    test.taggable.addTag( test.tagManager.getTag( "b" ) );
    test.taggable.addTag( test.tagManager.getTag( "c" ) );

    JFrame frame = new JFrame();
    frame.setSize( 800, 600 );

    frame.getContentPane().add( new JComboBox( test.comboBoxModel ), BorderLayout.NORTH );
    frame.setVisible( true );

    Thread.sleep( 2000 );
    test.taggable.addTag( test.tagManager.getTag( "d" ) );
    test.taggable.addTag( test.tagManager.getTag( "e" ) );
    test.taggable.addTag( test.tagManager.getTag( "f" ) );

    Thread.sleep( 2000 );
    test.taggable.removeTag( test.tagManager.getTag( "a" ) );
    test.taggable.removeTag( test.tagManager.getTag( "c" ) );
    test.taggable.removeTag( test.tagManager.getTag( "e" ) );
  }
}
