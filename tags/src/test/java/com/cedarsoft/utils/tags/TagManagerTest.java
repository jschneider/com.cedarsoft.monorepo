package com.cedarsoft.utils.tags;

import com.cedarsoft.NotFoundException;
import org.jetbrains.annotations.NotNull;
import static org.testng.Assert.*;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.List;

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
      @java.lang.Override
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
      @java.lang.Override
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
