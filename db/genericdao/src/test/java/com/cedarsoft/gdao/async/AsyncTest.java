package com.cedarsoft.gdao.async;

import com.cedarsoft.async.AsyncCallSupport;
import com.cedarsoft.gdao.MyObject;
import org.jetbrains.annotations.NotNull;
import org.testng.annotations.*;

/**
 *
 */
public abstract class AsyncTest extends AbstractThreadedDaoTest {
  @NotNull
  protected AsynchronousDao<MyObject> asyncDao;

  @Override
  @BeforeMethod
  protected void setUp() throws Exception {
    super.setUp();
    asyncDao = new AsynchronousDao<MyObject>();
    asyncDao.initializeDelegatingDao( genericDao );
  }

  @Override
  @AfterMethod
  protected void tearDown() throws Exception {
    asyncDao.shutdown();
    super.tearDown();
    AsyncCallSupport.verifyNoWorkerThreadsLeft();
  }
}
