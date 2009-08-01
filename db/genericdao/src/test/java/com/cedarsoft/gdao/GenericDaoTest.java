package com.cedarsoft.gdao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.dao.EmptyResultDataAccessException;
import static org.testng.Assert.*;
import org.testng.annotations.*;

import java.util.Collection;

/**
 *
 */
public class GenericDaoTest extends AbstractDaoTest {
  @Override
  @BeforeMethod
  protected void setUp() throws Exception {
    super.setUp();
  }

  @AfterMethod
  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
  }

  @Test
  public void testException() {
    final GenericService<MyObject> service = serviceManager.getService( MyObject.class );
    try {
      service.findById( 1L );
      fail( "Where is the Exception" );
    } catch ( EmptyResultDataAccessException e ) {
    }

    try {
      service.find( new AbstractInstanceFinder<MyObject>( MyObject.class ) {
        protected void addRestrictions( @NotNull Criteria criteria ) {
        }
      } );
      fail( "Where is the Exception" );
    } catch ( EmptyResultDataAccessException ignore ) {
    }
  }

  @Test
  public void testGenericService() {
    final GenericService<MyObject> service = serviceManager.getService( MyObject.class );
    assertNotNull( service );

    GenericDao<MyObject> dao = daoManager.getDao( MyObject.class );
    dao.save( new MyObject( "a" ) );
    dao.save( new MyObject( "b" ) );

    assertEquals( 2, service.findAll().size() );
    assertEquals( 2, service.getCount() );

    service.perform( new GenericService.ServiceCallback<MyObject, Object>() {
      @Nullable
      public Object perform( @NotNull GenericService<MyObject> service ) {
        for ( MyObject myObject : service.findAll() ) {
          myObject.setName( "new" );
          service.save( myObject );
        }
        return null;
      }
    } );

    for ( MyObject myObject : service.findAll() ) {
      assertEquals( "new", myObject.getName() );
      assertNotNull( myObject.getId() );
    }
  }

  @Test
  public void testGenericService2() {
    final GenericService<MyObject> service = serviceManager.getService( MyObject.class );
    assertNotNull( service );

    GenericDao<MyObject> dao = daoManager.getDao( MyObject.class );
    dao.save( new MyObject( "a" ) );
    dao.save( new MyObject( "b" ) );

    assertEquals( 2, service.findAll().size() );

    service.perform( new GenericService.ServiceCallbackWithoutReturnValue<MyObject>() {
      protected void performVoid( @NotNull GenericService<MyObject> service ) {
        for ( MyObject myObject : service.findAll() ) {
          myObject.setName( "new" );
          service.save( myObject );
        }
      }
    } );

    for ( MyObject myObject : service.findAll() ) {
      assertEquals( "new", myObject.getName() );
      assertNotNull( myObject.getId() );
    }
  }

  @Test
  public void testService() {
    MyObjectService service = ( MyObjectService ) context.getBean( "myObjectService" );
    assertNotNull( service );

    GenericDao<MyObject> dao = daoManager.getDao( MyObject.class );
    dao.save( new MyObject( "a" ) );
    dao.save( new MyObject( "b" ) );

    assertEquals( 2, service.findAll().size() );

    service.changeAllNames( "new" );
    for ( MyObject myObject : service.findAll() ) {
      assertEquals( "new", myObject.getName() );
      assertNotNull( myObject.getId() );
    }
  }

  @Test
  public void testSelector() {
    GenericDao<MyObject> dao = daoManager.getDao( MyObject.class );
    dao.save( new MyObject( "markus" ) );

    MyObject loaded = dao.find( new AbstractInstanceFinder<MyObject>( MyObject.class ) {
      protected void addRestrictions( @NotNull Criteria criteria ) {
        criteria.add( Restrictions.eq( "name", "markus" ) );
      }
    } );
    assertEquals( "markus", loaded.getName() );

    {
      Collection<? extends MyObject> loadedList = dao.find( new AbstractCollectionFinder<MyObject>( MyObject.class ) {
        protected void addRestrictions( @NotNull Criteria criteria ) {
          criteria.add( Restrictions.eq( "name", "markus" ) );
        }
      } );
      assertEquals( 1, loadedList.size() );
      assertEquals( "markus", loadedList.iterator().next().getName() );
    }
    {
      MyObjectFinder selector = new MyObjectFinder();
      selector.setName( "markus" );

      Collection<? extends MyObject> loadedList = dao.find( selector );
      assertEquals( 1, loadedList.size() );
      assertEquals( "markus", loadedList.iterator().next().getName() );
    }
  }

  @Test
  public void testOpenClose() {
    assertNotNull( sessionFactory.openSession() );
    sessionFactory.close();
    assertNotNull( sessionFactory.openSession() );
  }

  @Test
  public void testManager() {
    GenericDaoManager manager = ( GenericDaoManager ) context.getBean( "genericDaoManager" );
    assertNotNull( manager );

    GenericDao<MyObject> dao = manager.getDao( MyObject.class );
    assertNotNull( dao );
  }

  @Test
  public void testSaveDelete() {
    Long key;
    {
      MyObject object = new MyObject( "theName" );
      key = daoManager.<MyObject>getDao( MyObject.class ).save( object );
    }

    sessionFactory.close();

    MyObject loaded = daoManager.<MyObject>getDao( MyObject.class ).findById( key );
    assertEquals( "theName", loaded.getName() );

    //Delete
    daoManager.<MyObject>getDao( MyObject.class ).delete( loaded );
    assertEquals( 0, daoManager.<MyObject>getDao( MyObject.class ).findAll().size() );
  }
}
