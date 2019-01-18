package com.cedarsoft.commons.javafx;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


/**
 * Provides methods that support concurrency.
 *
 * @author Christian Erbelding (<a href="mailto:ce@cedarsoft.com">ce@cedarsoft.com</a>)
 */
public class ConcurrentUtil {

  private ConcurrentUtil() {
    // utility class
  }

  @Nonnull
  public static ScheduledExecutorService createSingleThreadScheduledExecutor(boolean spawnDaemonThread, @Nullable String threadName) {
    return Executors.newSingleThreadScheduledExecutor(new CustomizableThreadFactory(spawnDaemonThread, threadName));
  }

  @Nonnull
  public static ExecutorService createSingleThreadExecutor(boolean spawnDaemonThread, @Nullable String threadName) {
    return Executors.newSingleThreadExecutor(new CustomizableThreadFactory(spawnDaemonThread, threadName));
  }

  private static class CustomizableThreadFactory implements ThreadFactory {
    @Nullable
    private final String threadName;
    private final boolean spawnDaemonThread;

    CustomizableThreadFactory(boolean spawnDaemonThread, @Nullable String threadName) {
      this.spawnDaemonThread = spawnDaemonThread;
      this.threadName = threadName;
    }

    @Override
    public Thread newThread(@Nonnull Runnable r) {
      Thread thread = new Thread(r);
      thread.setDaemon(spawnDaemonThread);
      if (threadName != null) {
        thread.setName(threadName);
      }
      return thread;
    }
  }
}

