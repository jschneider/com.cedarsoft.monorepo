package com.cedarsoft.gdao.caching;

import com.cedarsoft.gdao.AbstractInstanceFinder;
import com.cedarsoft.gdao.MyObject;
import com.cedarsoft.gdao.async.AsyncTest;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.jetbrains.annotations.NotNull;
import static org.testng.Assert.*;
import org.testng.annotations.*;


/**
 *
 */
public class CachingAsyncTest extends AsyncTest {
  @NotNull
  private CachingDao<MyObject> cachingDao;

  @Override
  @BeforeMethod
  protected void setUp() throws Exception {
    super.setUp();
    cachingDao = new CachingDao<MyObject>( asyncDao );
  }

  @Override
  @AfterMethod
  protected void tearDown() throws Exception {
    cachingDao.shutdown();
    super.tearDown();
  }

  @Test
  public void testFinder() {
    MyObject saved = new MyObject( "asdf" );
    final Long id = cachingDao.save( saved );
    assertSame( saved, cachingDao.find( new AbstractInstanceFinder<MyObject>( MyObject.class ) {
      protected void addRestrictions( @NotNull Criteria criteria ) {
        criteria.add( Restrictions.eq( "id", id ) );
      }
    } ) );
    assertSame( saved, cachingDao.find( new AbstractInstanceFinder<MyObject>( MyObject.class ) {
      protected void addRestrictions( @NotNull Criteria criteria ) {
        criteria.add( Restrictions.eq( "name", "asdf" ) );
      }
    } ) );
  }

  @Test
  public void testRemove() {
    cachingDao.add( new MyObject() );
    cachingDao.add( new MyObject() );
    cachingDao.add( new MyObject() );

    assertEquals( 3, cachingDao.getCount() );
    cachingDao.remove( cachingDao.getElements().get( 1 ) );
    assertEquals( 2, cachingDao.getCount() );
    cachingDao.remove( cachingDao.getElements().get( 0 ) );
    assertEquals( 1, cachingDao.getCount() );
    cachingDao.remove( cachingDao.getElements().get( 0 ) );
    assertEquals( 0, cachingDao.getCount() );
  }

  @Test
  public void testSaveUpdate() {
    MyObject myObject = new MyObject();
    cachingDao.saveOrUpdate( myObject );
    assertEquals( 1, cachingDao.getCount() );
    cachingDao.saveOrUpdate( myObject );
    assertEquals( 1, cachingDao.getCount() );
    cachingDao.saveOrUpdate( new MyObject() );
    assertEquals( 2, cachingDao.getCount() );
  }

  @Test
  public void testUpdate() {
    MyObject myObject = new MyObject();
    cachingDao.add( myObject );
    cachingDao.update( myObject );

    assertEquals( 1, cachingDao.getCount() );
  }

  @Test
  public void testInitia() {
    dao.add( new MyObject() );
    dao.add( new MyObject() );

    cachingDao = new CachingDao<MyObject>( dao );
    cachingDao.initializeCache( dao.findAll() );

    assertEquals( 2, cachingDao.getCount() );
  }

  @Test
  public void testNoNotificationGet() {
    assertEquals( 0, cachingDao.getCount() );
    assertEquals( 0, cachingDao.getElements().size() );

    dao.add( new MyObject() );

    assertEquals( 0, cachingDao.getCount() );
    assertEquals( 0, cachingDao.getElements().size() );
  }

  @Test
  public void testSimple() {
    assertEquals( 0, cachingDao.getCount() );
    assertEquals( 0, cachingDao.getElements().size() );
    assertEquals( 0, dao.getCount() );
    assertEquals( 0, dao.getElements().size() );

    cachingDao.add( new MyObject() );

    assertEquals( 1, cachingDao.getCount() );
    assertEquals( 1, cachingDao.getElements().size() );
    assertEquals( 1, dao.getCount() );
    assertEquals( 1, dao.getElements().size() );
  }
}
