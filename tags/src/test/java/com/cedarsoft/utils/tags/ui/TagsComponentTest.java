package com.cedarsoft.utils.tags.ui;

import com.cedarsoft.utils.tags.DefaultTagManager;
import com.cedarsoft.utils.tags.TagChangeListener;
import com.cedarsoft.utils.tags.TagManager;
import org.jetbrains.annotations.NotNull;
import static org.testng.Assert.*;
import org.testng.annotations.*;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.JFrame;
import javax.swing.JToggleButton;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * <p/>
 * Date: Apr 3, 2007<br>
 * Time: 1:35:02 PM<br>
 */
public class TagsComponentTest {
  private TagsComponent tagsComponent;
  private DefaultTagsComponentModel model;
  private TagManager<Object> tagManager;

  @BeforeMethod
  protected void setUp() throws Exception {
    tagManager = new DefaultTagManager<Object>();
    tagManager.getTag( "a" );
    tagManager.getTag( "b" );
    tagManager.getTag( "c" );
    tagManager.getTag( "d" );

    model = new DefaultTagsComponentModel( tagManager, tagManager.getTaggable( "keyForTaggable" ) );
    tagsComponent = new TagsComponent( model );
  }

  @AfterMethod
  protected void tearDown() throws Exception {

  }

  @Test
  public void testSize() {
    Dimension preferredSize = tagsComponent.getPreferredSize();
    Dimension minimumSize = tagsComponent.getMinimumSize();
    assertEquals( preferredSize, tagsComponent.getPreferredSize() );

    tagsComponent.getModel().selectTag( tagManager.getTag( "asdf" ) );
    tagsComponent.getModel().selectTag( tagManager.getTag( "asdf2" ) );
    tagsComponent.getTagCombo().setSelectedIndex( 0 );
    assertEquals( 6, tagsComponent.getTagCombo().getItemCount() );

    assertEquals( preferredSize, tagsComponent.getPreferredSize() );
    assertEquals( minimumSize, tagsComponent.getMinimumSize() );
  }

  @Test
  public void testSetup() {
    assertNotNull( model );
    assertNotNull( tagsComponent );
    assertEquals( 4, model.getTagProvider().getTags().size() );
  }

  @Test
  public void testSelectionListeners() {
    final List<TagChangeListener.TagChangeEvent> events = new ArrayList<TagChangeListener.TagChangeEvent>();

    model.getSelectedTags().addTagChangeListener( new TagChangeListener() {
      @java.lang.Override
      public void tagChanged( @NotNull TagChangeEvent event ) {
        events.add( event );
      }
    } );

    assertEquals( 0, events.size() );

    model.selectTag( tagManager.getTag( "a" ) );
    assertEquals( 1, model.getSelectedTags().getTags().size() );

    {
      assertEquals( 1, events.size() );
      TagChangeListener.TagChangeEvent event = events.get( 0 );
      assertEquals( 0, event.getIndex() );
      assertEquals( "a", event.getTag().getDescription() );
      assertEquals( TagChangeListener.TagEventType.ADD, event.getType() );
      assertSame( event.getTag(), model.getSelectedTags().getTags().get( 0 ) );

      events.clear();
    }

    model.unselectTag( tagManager.getTag( "a" ) );
    assertEquals( 0, model.getSelectedTags().getTags().size() );

    {
      assertEquals( 1, events.size() );
      TagChangeListener.TagChangeEvent event = events.get( 0 );
      assertEquals( 0, event.getIndex() );
      assertEquals( "a", event.getTag().getDescription() );
      assertEquals( TagChangeListener.TagEventType.REMOVE, event.getType() );

      events.clear();
    }
  }

  @Test
  public void testModels() {
    TagListModel tagListModel = ( TagListModel ) tagsComponent.getTagList().getModel();
    TagComboBoxModel tagsComboModel = ( TagComboBoxModel ) tagsComponent.getTagCombo().getModel();

    assertEquals( 0, tagListModel.getSize() );
    assertEquals( 4, tagsComboModel.getSize() );
  }

  public static void main( String[] args ) throws Exception {
    final TagsComponent tagsComponent = new TagsComponent();

    JFrame frame = new JFrame();
    frame.getContentPane().add( tagsComponent, BorderLayout.CENTER );
    frame.getContentPane().add( new JToggleButton( new AbstractAction( "Toggle New Tags" ) {
      @java.lang.Override
      public void actionPerformed( ActionEvent e ) {
        tagsComponent.setAllowNewTagCreation( !( ( AbstractButton ) e.getSource() ).isSelected() );
      }
    } ), BorderLayout.SOUTH );

    frame.pack();
    frame.setVisible( true );
  }
}
