package com.cedarsoft.tags;

import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.jetbrains.annotations.NotNull;
import org.testng.annotations.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

/**
 * <p/>
 * Date: Apr 2, 2007<br>
 * Time: 4:06:42 PM<br>
 */
public class TagPersistenceTest extends TagDbTest {
  @Override
  @BeforeMethod
  protected void setUp() throws Exception {
    super.setUp();
  }

  @Test
  public void testIt() {
    session.save( new Tag( "asdf" ) );
    reopenSession();
    List<Tag> result = session.createCriteria( Tag.class ).list();
    assertFalse( result.isEmpty() );
    assertEquals( "asdf", result.get( 0 ).getDescription() );
  }

  @Test
  public void testLookup() {
    int id;

    {
      Transaction tx = session.beginTransaction();

      AnObject anObject = new AnObject();
      TagSet tagSet = new TagSet( anObject );
      Tag tag = new Tag( "1" );
      tagSet.addTag( tag );
      assertEquals( 1, tagSet.getTags().size() );
      session.save( tagSet );
      session.save( anObject );
      tx.commit();
      id = anObject.getId();
      assertEquals( 1, tagSet.getTags().size() );
    }

    {
      //Adding FUD
      Transaction tx = session.beginTransaction();
      session.save( new TagSet( new AnObject() ) );
      session.save( new TagSet( new AnObject() ) );
      session.save( new TagSet( new AnObject() ) );
      tx.commit();
    }

    reopenSession();

    {
      Transaction tx = session.beginTransaction();

      AnObject loaded = ( AnObject ) session.load( AnObject.class, id );
      List<TagSet> tagSets = session.createCriteria( TagSet.class ).add( Restrictions.eq( "source", loaded ) ).list();
      //      List<TagSet<AnObject>> tagSets = session.createQuery( "FROM com.cedarsoft.tags.TagSet as tagSet WHERE tagSet.source.id="+loaded.getId() ).list();
      assertEquals( 1, tagSets.size() );
      assertEquals( "1", tagSets.get( 0 ).getTags().iterator().next().getDescription() );

      tx.rollback();
    }
  }

  @Test
  public void testTagSet() throws SQLException {
    AnObject anObject = new AnObject();

    {
      Transaction tx = session.beginTransaction();

      TagSet tagSet = new TagSet( anObject );
      Tag tag = new Tag( "1" );
      tagSet.addTag( tag );
      assertEquals( 1, tagSet.getTags().size() );
      session.save( tagSet );
      session.save( anObject );
      tx.commit();
      assertEquals( 1, tagSet.getTags().size() );
    }

    reopenSession();

    {
      Transaction tx = session.beginTransaction();

      checkTable();

      assertEquals( 1, session.createCriteria( Tag.class ).list().size() );
      assertEquals( "1", ( ( List<Tag> ) session.createCriteria( Tag.class ).list() ).get( 0 ).getDescription() );

      List<TagSet> result = session.createCriteria( TagSet.class ).list();
      assertEquals( 1, result.size() );
      TagSet loaded = result.get( 0 );
      assertEquals( 1, loaded.getTags().size() );
      assertEquals( "1", loaded.getTags().iterator().next().getDescription() );
      tx.rollback();
    }

    reopenSession();

    {
      List<TagSet> result = session.createCriteria( TagSet.class ).list();
      assertEquals( 1, result.size() );
      TagSet loaded = result.get( 0 );

      final List<TagChangeListener.TagChangeEvent> events = new ArrayList<TagChangeListener.TagChangeEvent>();
      loaded.addTagChangeListener( new TagChangeListener() {
        @Override
        public void tagChanged( @NotNull TagChangeEvent event ) {
          events.add( event );
        }
      } );

      loaded.addTag( new Tag( "asfafe" ) );
      assertEquals( 1, events.size() );
      TagChangeListener.TagChangeEvent event = events.get( 0 );
      assertEquals( "asfafe", event.getTag().getDescription() );
      assertSame( anObject.getId(), ( ( AnObject ) event.getSource() ).getId() );
      assertEquals( 1, event.getIndex() );
    }
  }

  private void checkTable() throws SQLException {
    System.out.println( "Checking Table...." );
    Connection connection = DriverManager.getConnection( getUrl(), getUser(), getPassword() );

    {
      ResultSet resultSet = connection.createStatement().executeQuery( "SELECT count(*) FROM TagSet_tags" );
      assertNotNull( resultSet );
      while ( resultSet.next() ) {
        System.out.println( "--> " + resultSet.getInt( 1 ) );
        assertFalse( 0 == resultSet.getInt( 1 ), "No Entries in \"TagSet_tags\"" );
      }
    }

    {
      ResultSet resultSet = connection.createStatement().executeQuery( "SELECT * FROM TagSet_tags" );
      assertNotNull( resultSet );
      while ( resultSet.next() ) {
        System.out.println( "--> " + resultSet.getInt( 1 ) + ": " + resultSet.getInt( 2 ) );
      }
    }
  }
}
