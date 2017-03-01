package com.cedarsoft.concurrent;

import com.cedarsoft.test.utils.ThreadRule;
import org.junit.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.fail;
import static org.awaitility.Awaitility.await;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class AsyncTest {
  @Rule
  public ThreadRule threadRule = new ThreadRule();
  private ExecutorService executor;

  @Before
  public void setUp() throws Exception {
    executor = Executors.newSingleThreadExecutor();
  }

  @After
  public void tearDown() throws Exception {
    executor.shutdownNow();
    executor.awaitTermination(100, TimeUnit.MILLISECONDS);
  }

  @Test
  public void basic() throws Exception {
    Async async = new Async(executor);

    AtomicBoolean otherJobsAdded = new AtomicBoolean();

    async.last("asdf", new Runnable() {
      @Override
      public void run() {
        await()
          .pollDelay(10, TimeUnit.MILLISECONDS)
          .until(otherJobsAdded::get);
      }
    });

    for (int i = 0; i < 10; i++) {
      async.last("asdf", new Runnable() {
        @Override
        public void run() {
          fail("Must not be called");
        }
      });
    }

    AtomicBoolean executed = new AtomicBoolean();

    async.last("asdf", new Runnable() {
      @Override
      public void run() {
        executed.set(true);
      }
    });

    //finish first job
    otherJobsAdded.set(true);

    await().pollDelay(10, TimeUnit.MILLISECONDS).until(executed::get);
  }
}