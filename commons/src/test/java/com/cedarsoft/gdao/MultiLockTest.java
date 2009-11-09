package com.cedarsoft.gdao;

import com.cedarsoft.utils.lock.MultiLock;
import com.cedarsoft.utils.ThreadUtils;
import org.jetbrains.annotations.Nullable;
import static org.testng.Assert.*;
import org.testng.annotations.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 */
public class MultiLockTest {
  private Lock lock0;
  private Lock lock1;
  private MultiLock multiLock;

  @BeforeMethod
  protected void setUp() throws Exception {
    lock0 = new ReentrantLock();
    lock1 = new ReentrantLock();
    multiLock = new MultiLock( lock0, lock1 );
  }

  @AfterMethod
  protected void tearDown() throws Exception {

  }

  @Test
  public void testBasic() throws ExecutionException, InterruptedException {
    multiLock.lock();

    ThreadUtils.inokeInOtherThread( new Callable<Object>() {
      @java.lang.Override
      @Nullable
      public Object call() throws Exception {
        assertFalse( lock0.tryLock() );
        assertFalse( lock1.tryLock() );
        return null;
      }
    } );

    multiLock.unlock();
  }
}
