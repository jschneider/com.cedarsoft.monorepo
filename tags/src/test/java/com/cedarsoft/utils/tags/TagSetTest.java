package com.cedarsoft.utils.tags;

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
