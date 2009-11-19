package com.cedarsoft.gdao.caching;

import com.cedarsoft.gdao.AbstractDaoTest;
import com.cedarsoft.gdao.AbstractInstanceFinder;
import com.cedarsoft.gdao.GenericService;
import com.cedarsoft.gdao.MyObject;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.jetbrains.annotations.NotNull;
import static org.testng.Assert.*;
import org.testng.annotations.*;

import java.lang.Override;


/**
 *
 */
public class CachingServiceTest extends AbstractDaoTest {
  @NotNull
  private CachingService<MyObject> cachingService;

  @Override
  @BeforeMethod
  protected void setUp() throws Exception {
    super.setUp();
    cachingService = new CachingService<MyObject>( service );
  }

  @AfterMethod
  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
  }

  @Test
  public void testDPerform() {
    cachingService.add( new MyObject( "old" ) );
    cachingService.add( new MyObject( "old" ) );
    cachingService.add( new MyObject( "old" ) );
    cachingService.add( new MyObject( "old" ) );
    cachingService.add( new MyObject( "old" ) );

    assertEquals( 5, cachingService.getCount() );

    cachingService.perform( new GenericService.ServiceCallbackWithoutReturnValue<MyObject>() {
      @Override
      protected void performVoid( @NotNull GenericService<MyObject> service ) {
        for ( MyObject myObject : service.getElements() ) {
          myObject.setName( "new" );
          service.saveOrUpdate( myObject );
        }
      }
    } );

    assertEquals( 5, service.getCount() );
    assertEquals( 5, cachingService.getCount() );

    for ( MyObject myObject : cachingService.getElements() ) {
      assertEquals( "new", myObject.getName() );
    }
  }

  @Test
  public void testPerform2() {
    cachingService.perform( new GenericService.ServiceCallbackWithoutReturnValue<MyObject>() {
      @Override
      protected void performVoid( @NotNull GenericService<MyObject> service ) {
        for ( int i = 0; i < 5; i++ ) {
          MyObject myObject = new MyObject( "new" );
          service.save( myObject );
        }
      }
    } );

    assertEquals( 5, service.getCount() );
    assertEquals( 5, cachingService.getCount() );

    for ( MyObject myObject : cachingService.getElements() ) {
      assertEquals( "new", myObject.getName() );
    }
  }

  @Test
  public void testFinder() {
    MyObject saved = new MyObject( "asdf" );
    final Long id = cachingService.save( saved );
    assertSame( saved, cachingService.find( new AbstractInstanceFinder<MyObject>( MyObject.class ) {
      @Override
      protected void addRestrictions( @NotNull Criteria criteria ) {
        criteria.add( Restrictions.eq( "id", id ) );
      }
    } ) );
    assertSame( saved, cachingService.find( new AbstractInstanceFinder<MyObject>( MyObject.class ) {
      @Override
      protected void addRestrictions( @NotNull Criteria criteria ) {
        criteria.add( Restrictions.eq( "name", "asdf" ) );
      }
    } ) );
  }

  @Test
  public void testRemove() {
    cachingService.add( new MyObject() );
    cachingService.add( new MyObject() );
    cachingService.add( new MyObject() );

    assertEquals( 3, cachingService.getCount() );
    cachingService.remove( cachingService.getElements().get( 1 ) );
    assertEquals( 2, cachingService.getCount() );
    cachingService.remove( cachingService.getElements().get( 0 ) );
    assertEquals( 1, cachingService.getCount() );
    cachingService.remove( cachingService.getElements().get( 0 ) );
    assertEquals( 0, cachingService.getCount() );
  }

  @Test
  public void testSaveUpdate() {
    MyObject myObject = new MyObject();
    cachingService.saveOrUpdate( myObject );
    assertEquals( 1, cachingService.getCount() );
    cachingService.saveOrUpdate( myObject );
    assertEquals( 1, cachingService.getCount() );
    cachingService.saveOrUpdate( new MyObject() );
    assertEquals( 2, cachingService.getCount() );
  }

  @Test
  public void testUpdate() {
    MyObject myObject = new MyObject();
    cachingService.add( myObject );
    cachingService.update( myObject );

    assertEquals( 1, cachingService.getCount() );
  }

  @Test
  public void testInitia() {
    dao.add( new MyObject() );
    dao.add( new MyObject() );

    cachingService = new CachingService<MyObject>( service );
    cachingService.initializeCache( service.getElements() );
    assertEquals( 2, cachingService.getCount() );
  }

  @Test
  public void testNoNotificationGet() {
    assertEquals( 0, cachingService.getCount() );
    assertEquals( 0, cachingService.getElements().size() );

    dao.add( new MyObject() );

    assertEquals( 0, cachingService.getCount() );
    assertEquals( 0, cachingService.getElements().size() );
  }

  @Test
  public void testSimple() {
    assertEquals( 0, cachingService.getCount() );
    assertEquals( 0, cachingService.getElements().size() );
    assertEquals( 0, dao.getCount() );
    assertEquals( 0, dao.getElements().size() );

    cachingService.add( new MyObject() );

    assertEquals( 1, cachingService.getCount() );
    assertEquals( 1, cachingService.getElements().size() );
    assertEquals( 1, dao.getCount() );
    assertEquals( 1, dao.getElements().size() );
  }


}
