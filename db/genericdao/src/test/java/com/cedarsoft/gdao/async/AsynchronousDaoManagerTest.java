package com.cedarsoft.gdao.async;

import com.cedarsoft.async.AsyncCallSupport;
import com.cedarsoft.async.AsynchroniousInvocationException;
import com.cedarsoft.gdao.AbstractInstanceFinder;
import com.cedarsoft.gdao.GenericDao;
import com.cedarsoft.gdao.GenericDaoManager;
import com.cedarsoft.gdao.GenericDaoTest;
import com.cedarsoft.gdao.GenericService;
import com.cedarsoft.gdao.GenericServiceManager;
import com.cedarsoft.gdao.MyObject;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import static org.testng.Assert.*;
import org.testng.annotations.*;

/**
 *
 */
public class AsynchronousDaoManagerTest {
  private ApplicationContext context;
  private SessionFactory sessionFactory;
  private GenericDaoManager daoManager;
  private GenericServiceManager serviceManager;

  @BeforeMethod
  protected void setUp() throws Exception {
    context = new ClassPathXmlApplicationContext( "test.spr.xml", GenericDaoTest.class );
    sessionFactory = ( SessionFactory ) context.getBean( "sessionFactory" );
    daoManager = ( GenericDaoManager ) context.getBean( "asyncDaoManager" );
    serviceManager = ( GenericServiceManager ) context.getBean( "asyncServiceManager" );
  }

  @AfterMethod
  protected void tearDown() throws Exception {
    daoManager.shutdown();
    ( ( GenericDaoManager ) context.getBean( "asyncDaoManager" ) ).shutdown();
    ( ( AsynchronousServiceManager ) context.getBean( "asyncServiceManager" ) ).shutdown();
    AsyncCallSupport.verifyNoWorkerThreadsLeft();
  }

  @Test
  public void testResolving() {
    assertSame( daoManager.getDao( MyObject.class ), daoManager.getDao( MyObject.class ) );
  }

  @Test
  public void testInstance() {
    assertNotNull( daoManager );
    assertNotNull( serviceManager );

    assertTrue( daoManager instanceof AsynchronousDaoManager );
    assertTrue( serviceManager instanceof AsynchronousServiceManager );
  }

  @Test
  public void testService() {
    GenericDao<MyObject> dao = daoManager.getDao( MyObject.class );
    assertTrue( dao instanceof AsynchronousDao );
    dao.save( new MyObject( "a" ) );
    dao.save( new MyObject( "b" ) );
    assertEquals( 2, dao.getCount() );
    assertEquals( 2, dao.getElements().size() );

    GenericService<MyObject> service = serviceManager.getService( MyObject.class );
    assertTrue( service instanceof AsynchronousService );
    assertEquals( 2, service.findAll().size() );

    service.perform( new GenericService.ServiceCallbackWithoutReturnValue<MyObject>() {
      @Override
      protected void performVoid( @NotNull GenericService<MyObject> service ) {
        for ( MyObject myObject : service.getElements() ) {
          myObject.setName( "new" );
          service.saveOrUpdate( myObject );
        }
      }
    } );

    for ( MyObject myObject : service.findAll() ) {
      assertNotNull( myObject.getId() );
    }
  }

  @Test
  public void testException() {
    final GenericService<MyObject> service = serviceManager.getService( MyObject.class );
    try {
      service.findById( 1L );
      fail( "Where is the Exception" );
    } catch ( AsynchroniousInvocationException e ) {
      assertEquals( e.getCause().getClass(), EmptyResultDataAccessException.class );
    }

    try {
      service.find( new AbstractInstanceFinder<MyObject>( MyObject.class ) {
        @java.lang.Override
        protected void addRestrictions( @NotNull Criteria criteria ) {
        }
      } );
      fail( "Where is the Exception" );
    } catch ( AsynchroniousInvocationException e ) {
      assertEquals( e.getCause().getClass(), EmptyResultDataAccessException.class );
    }
  }


}
