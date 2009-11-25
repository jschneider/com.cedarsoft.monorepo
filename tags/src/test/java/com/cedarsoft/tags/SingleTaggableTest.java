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
