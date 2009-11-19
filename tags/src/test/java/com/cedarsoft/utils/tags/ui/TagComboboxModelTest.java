package com.cedarsoft.utils.tags.ui;

import com.cedarsoft.utils.tags.DefaultTagManager;
import com.cedarsoft.utils.tags.TagChangeListener;
import com.cedarsoft.utils.tags.TagManager;
import com.cedarsoft.utils.tags.Taggable;
import org.jetbrains.annotations.NotNull;
import static org.testng.Assert.*;
import org.testng.annotations.*;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.lang.Override;
import java.util.ArrayList;
import java.util.List;

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

  @BeforeMethod
  protected void setUp() throws Exception {
    tagManager = new DefaultTagManager<Object>();
    taggable = tagManager.getTaggable( object );
    comboBoxModel = new TagComboBoxModel( tagManager, false );
  }

  @AfterMethod
  protected void tearDown() throws Exception {

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
