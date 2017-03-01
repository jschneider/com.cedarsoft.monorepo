package com.cedarsoft.concurrent;

import com.cedarsoft.test.utils.ThreadRule;
import org.junit.*;

import javax.annotation.Nonnull;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.awaitility.Awaitility.await;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class DefaultNewestOnlyJobManagerTest {
  @Rule
  public ThreadRule threadRule = new ThreadRule();

  private ExecutorService executorService;

  @Before
  public void setUp() throws Exception {
    executorService = Executors.newCachedThreadPool();
  }

  @After
  public void tearDown() throws Exception {
    executorService.shutdownNow();
    assertThat(executorService.awaitTermination(1, TimeUnit.SECONDS)).isTrue();
  }

  @Test
  public void smoke() throws Exception {
    DefaultNewestOnlyJobManager jobManager = new DefaultNewestOnlyJobManager(executorService, 1);
    jobManager.startWorkers();
  }

  @Test(timeout = 1000)
  public void onlyYoungestJobIsExecuted() throws Exception {
    DefaultNewestOnlyJobManager jobManager = new DefaultNewestOnlyJobManager(executorService, 1);

    for (int i = 0; i < 10; i++) {
      jobManager.scheduleJob(new NewestOnlyJobsManager.Job() {
        @Nonnull
        @Override
        public Object getKey() {
          return "asdf";
        }

        @Override
        public void execute() {
          fail("Must not be executed");
        }
      });
    }

    AtomicBoolean executed = new AtomicBoolean();

    jobManager.scheduleJob(new NewestOnlyJobsManager.Job() {
      @Nonnull
      @Override
      public Object getKey() {
        return "asdf";
      }

      @Override
      public void execute() {
        executed.set(true);
      }
    });

    assertThat(executed.get()).isFalse();

    jobManager.startWorkers();
    await().pollDelay(10, TimeUnit.MILLISECONDS).until(executed::get);
  }

  @Test
  public void reproduceDeadLock() throws Exception {
    AtomicBoolean executed = new AtomicBoolean();

    DefaultNewestOnlyJobManager jobManager = new DefaultNewestOnlyJobManager(executorService, 1);
    jobManager.startWorkers();

    jobManager.scheduleJob(new NewestOnlyJobsManager.Job() {
      @Nonnull
      @Override
      public Object getKey() {
        return "asf";
      }

      @Override
      public void execute() {
        executed.set(true);
      }
    });

    await().pollDelay(10, TimeUnit.MILLISECONDS).until(executed::get);
  }
}