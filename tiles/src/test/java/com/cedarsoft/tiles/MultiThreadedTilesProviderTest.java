package com.cedarsoft.tiles;

import com.cedarsoft.concurrent.DefaultNewestOnlyJobManager;
import com.cedarsoft.test.utils.ThreadRule;
import com.cedarsoft.unit.si.ns;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@Ignore
public class MultiThreadedTilesProviderTest {
  @Rule
  public ThreadRule threadRule = new ThreadRule();

  @Test
  public void testDequee() throws Exception {
    BlockingDeque<String> blockingQueue = new LinkedBlockingDeque<>(15);

    Thread thread = new Thread(() -> {
      try {
        blockingQueue.takeFirst();
      } catch (InterruptedException ignore) {
      }
    });
    thread.start();

    Thread.sleep(100);

    assertThat(thread.isInterrupted()).isFalse();
    assertThat(thread.isAlive()).isTrue();

    thread.interrupt();
    assertThat(thread.isInterrupted()).isTrue();
    thread.join();
    assertThat(thread.isAlive()).isFalse();
  }

  @Test
  public void testDequeeExecutor() throws Exception {
    int poolSize = 4;
    int threadSize = 100;

    ExecutorService executorService = Executors.newFixedThreadPool(poolSize);

    BlockingDeque<String> blockingQueue = new LinkedBlockingDeque<>(15);

    List<Future<?>> futures = new ArrayList<>();
    for (int i = 0; i < threadSize; i++) {
      futures.add(executorService.submit(() -> {
        while (!Thread.currentThread().isInterrupted()) {
          try {
            blockingQueue.takeFirst();
          } catch (InterruptedException ignore) {
            return;
          }
        }
      }));
    }

    Thread.sleep(400);

    assertThat(executorService.isShutdown()).isFalse();
    assertThat(executorService.isTerminated()).isFalse();

    List<Runnable> remaining = executorService.shutdownNow();
    assertThat(executorService.isShutdown()).isTrue();

    assertThat(remaining).hasSize(threadSize - poolSize);

    assertThat(executorService.awaitTermination(100, TimeUnit.MILLISECONDS)).isTrue();
    assertThat(executorService.isTerminated()).isTrue();
  }

  @Test
  public void testWorkerEndThread() throws Exception {
    ExecutorService executorService = Executors.newFixedThreadPool(4);

    MultiThreadedObservableTilesProvider multiThreadedTilesSource = new MultiThreadedObservableTilesProvider(new DebugTilesProvider(), new DefaultNewestOnlyJobManager(executorService, 4));

    List<Runnable> runnables = executorService.shutdownNow();
    assertThat(runnables).isEmpty();
    assertThat(executorService.isShutdown()).isTrue();

    assertThat(executorService.awaitTermination(500, TimeUnit.MILLISECONDS)).isTrue();

    assertThat(executorService.isTerminated()).isTrue();
  }

  @Ignore
  @Test
  public void testBlockingQueue() throws Exception {
    BlockingDeque<String> blockingQueue = new LinkedBlockingDeque<>(15);

    blockingQueue.add("daobject");

    new Thread(new Runnable() {
      @Override
      public void run() {
        while (true) {
          try {
            @ns long start = System.nanoTime();
            String received = blockingQueue.takeFirst();
            System.out.println("received <" + received + "> after waiting for " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start) + " ms.\t\t " + blockingQueue.size() + " remaining in deque");
            Thread.sleep(500);
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        }
      }
    }).start();


    int i = 0;
    while (true) {
      Thread.sleep((long) (Math.random() * 500));
      blockingQueue.addFirst("--- " + i);

      while (blockingQueue.size() > 5) {
        //Cleaning up
        System.out.println("cleaning up <" + blockingQueue.removeLast() + ">");
      }

      i++;
    }
  }
}