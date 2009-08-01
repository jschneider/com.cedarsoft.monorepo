package com.cedarsoft.gdao.async;

import com.cedarsoft.async.AsynchroniousInvocationException;
import com.cedarsoft.gdao.AbstractInstanceFinder;
import com.cedarsoft.gdao.DefaultGenericServiceManager;
import com.cedarsoft.gdao.GenericService;
import com.cedarsoft.gdao.GenericServiceManager;
import com.cedarsoft.gdao.MyObject;
import org.hibernate.Criteria;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.EmptyResultDataAccessException;
import static org.testng.Assert.*;
import org.testng.annotations.*;

/**
 *
 */
public class AsyncServiceTest extends AsyncTest {
  protected AsynchronousService<MyObject> service;
  private GenericService<MyObject> genericService;

  @Override
  @BeforeMethod
  protected void setUp() throws Exception {
    super.setUp();
    genericService = serviceManager.getService( MyObject.class );
    service = new AsynchronousService<MyObject>();
    service.initializeDelegatingDao( genericService, null );
  }

  @Override
  @AfterMethod
  protected void tearDown() throws Exception {
    genericService.shutdown();
    service.shutdown();
    super.tearDown();
  }

  @Override
  @NotNull
  protected GenericServiceManager getServiceManager() {
    return new DefaultGenericServiceManager( transactionManager, daoManager );
  }

  @Test
  public void testEmpty() {

  }

  @Test
  public void testIt() {
    assertEquals( 0, service.getCount() );
    MyObject object = new MyObject( "saved" );
    service.save( object );
    assertEquals( 1, service.getCount() );

    assertEquals( "saved", service.getElements().get( 0 ).getName() );
    assertNotNull( object.getId() );

    service.remove( object );
    assertEquals( 0, service.getCount() );
  }

  @Test
  public void testException() {
    assertEquals( 0, service.getCount() );
    try {
      service.findById( 1L );
      fail( "Where is the Exception" );
    } catch ( AsynchroniousInvocationException e ) {
      assertEquals( e.getCause().getClass(), EmptyResultDataAccessException.class );
    }
    assertEquals( 0, service.getCount() );

    try {
      service.find( new AbstractInstanceFinder<MyObject>( MyObject.class ) {
        @Override
        protected void addRestrictions( @NotNull Criteria criteria ) {
          throw new IllegalStateException( "Hehe" );
        }
      } );
      fail( "Where is the Exception" );
    } catch ( AsynchroniousInvocationException e ) {
      assertEquals( e.getCause().getClass(), IllegalStateException.class );
      assertEquals( "Hehe", e.getCause().getMessage() );
    }
  }

  @Test
  public void testService() {
    service.perform( new GenericService.ServiceCallbackWithoutReturnValue<MyObject>() {
      @Override
      protected void performVoid( @NotNull GenericService<MyObject> service ) {
        ensureNotInTestThread();
      }
    } );
  }
}
