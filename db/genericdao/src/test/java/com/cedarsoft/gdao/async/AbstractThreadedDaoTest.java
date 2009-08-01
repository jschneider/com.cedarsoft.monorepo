package com.cedarsoft.gdao.async;

import com.cedarsoft.gdao.AbstractDaoTest;
import com.cedarsoft.gdao.GenericDao;
import com.cedarsoft.gdao.MyObject;
import org.testng.annotations.*;

/**
 *
 */
public abstract class AbstractThreadedDaoTest extends AbstractDaoTest {
  protected Thread testThread;
  protected GenericDao<MyObject> genericDao;

  @Override
  @BeforeMethod
  protected void setUp() throws Exception {
    super.setUp();
    genericDao = daoManager.getDao( MyObject.class );
    testThread = Thread.currentThread();
  }

  @Override
  @AfterMethod
  protected void tearDown() throws Exception {
    genericDao.shutdown();
    super.tearDown();
  }

  protected void ensureNotInTestThread() {
    if ( Thread.currentThread() == testThread ) {
      throw new IllegalStateException( "Must not be called in test thread" );
    }
  }
}
