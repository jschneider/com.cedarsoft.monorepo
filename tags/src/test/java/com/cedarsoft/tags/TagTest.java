package com.cedarsoft.tags;

import org.jetbrains.annotations.NotNull;
import static org.testng.Assert.*;
import org.testng.annotations.*;

import java.lang.Override;
import java.util.ArrayList;
import java.util.List;

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
