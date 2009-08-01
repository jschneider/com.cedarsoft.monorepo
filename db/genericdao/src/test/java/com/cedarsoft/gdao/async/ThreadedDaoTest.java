package com.cedarsoft.gdao.async;

import com.cedarsoft.async.AsyncCallSupport;
import org.testng.annotations.*;

/**
 *
 */
public class ThreadedDaoTest extends AbstractThreadedDaoTest {
  @Override
  @AfterMethod
  protected void tearDown() throws Exception {
    super.tearDown();
    AsyncCallSupport.verifyNoWorkerThreadsLeft();
  }

  @Test
  public void testIt() {
    //Only to check threads on tear down
  }
}
