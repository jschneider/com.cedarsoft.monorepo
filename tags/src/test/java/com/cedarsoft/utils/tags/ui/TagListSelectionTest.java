package com.cedarsoft.utils.tags.ui;

import com.cedarsoft.utils.tags.DefaultTagManager;
import com.cedarsoft.utils.tags.Tag;
import com.cedarsoft.utils.tags.TagChangeListener;
import com.cedarsoft.utils.tags.TagManager;
import org.jetbrains.annotations.NotNull;
import static org.testng.Assert.*;
import org.testng.annotations.*;

import javax.swing.JList;
import java.util.ArrayList;
import java.util.List;

/**
 * <p/>
 * Date: May 7, 2007<br>
 * Time: 1:12:20 PM<br>
 */
public class TagListSelectionTest {
  private TagManager<Object> tagManager;
  private TagListModel model;
  private JList list;
  private TagListSelectionMode selectionModel;

  @BeforeMethod
  protected void setUp() throws Exception {

    tagManager = new DefaultTagManager<Object>();
    tagManager.getTag( "a" );
    tagManager.getTag( "b" );
    tagManager.getTag( "c" );
    tagManager.getTag( "d" );

    model = new TagListModel( tagManager, false );
    list = new JList( model );
    selectionModel = new TagListSelectionMode( model );
    list.setSelectionModel( selectionModel );
  }

  @AfterMethod
  protected void tearDown() throws Exception {

  }

  @Test
  public void testSetup() {
    assertEquals( 4, tagManager.getTags().size() );
    assertEquals( 4, model.getSize() );
  }

  @Test
  public void testBasic() {
    assertTrue( selectionModel.isSelectionEmpty() );
    selectionModel.setSelectionInterval( 1, 1 );
    assertFalse( selectionModel.isSelectionEmpty() );
    assertNotNull( list.getSelectedValue() );
    assertEquals( "b", ( ( Tag ) list.getSelectedValue() ).getDescription() );
  }

  @Test
  public void testListener() {
    final List<TagChangeListener.TagChangeEvent> events = new ArrayList<TagChangeListener.TagChangeEvent>();

    selectionModel.addTagChangeListener( new TagChangeListener() {
      public void tagChanged( @NotNull TagChangeEvent event ) {
        events.add( event );
      }
    } );

    list.setSelectionInterval( 1, 1 );
    assertEquals( 1, events.size() );
    TagChangeListener.TagChangeEvent event = events.get( 0 );

    assertEquals( -1, event.getIndex() );
    assertEquals( selectionModel, event.getSource() );
  }
}
