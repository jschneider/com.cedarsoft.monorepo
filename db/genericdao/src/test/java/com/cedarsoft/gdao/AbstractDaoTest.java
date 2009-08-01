package com.cedarsoft.gdao;

import com.cedarsoft.async.AsyncCallSupport;
import com.cedarsoft.gdao.async.AsynchronousServiceManager;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.testng.annotations.*;

/**
 *
 */
public abstract class AbstractDaoTest {
  protected ApplicationContext context;
  protected SessionFactory sessionFactory;
  protected GenericDaoManager daoManager;
  protected GenericServiceManager serviceManager;
  protected GenericDao<MyObject> dao;
  protected GenericService<MyObject> service;
  protected AbstractPlatformTransactionManager transactionManager;

  @BeforeMethod
  protected void setUp() throws Exception {
    AsyncCallSupport.verifyNoWorkerThreadsLeft();

    context = new ClassPathXmlApplicationContext( "test.spr.xml", AbstractDaoTest.class );
    sessionFactory = ( SessionFactory ) context.getBean( "sessionFactory" );
    transactionManager = ( AbstractPlatformTransactionManager ) context.getBean( "transactionManager" );
    daoManager = getDaoManager();
    serviceManager = getServiceManager();
    dao = daoManager.getDao( MyObject.class );
    service = serviceManager.getService( MyObject.class );
  }

  @AfterMethod
  protected void tearDown() throws Exception {
    daoManager.shutdown();
    service.shutdown();
    dao.shutdown();

    ( ( GenericDaoManager ) context.getBean( "asyncDaoManager" ) ).shutdown();
    ( ( AsynchronousServiceManager ) context.getBean( "asyncServiceManager" ) ).shutdown();
    AsyncCallSupport.verifyNoWorkerThreadsLeft();

    context = null;
    sessionFactory = null;
    daoManager = null;
    serviceManager = null;
    dao = null;
    service = null;
    transactionManager = null;
  }

  @NotNull
  protected GenericServiceManager getServiceManager() {
    return ( GenericServiceManager ) context.getBean( "genericServiceManager" );
  }

  @NotNull
  protected GenericDaoManager getDaoManager() {
    return ( GenericDaoManager ) context.getBean( "genericDaoManager" );
  }
}
