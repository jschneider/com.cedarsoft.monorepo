package com.cedarsoft.concurrent;

import com.cedarsoft.unit.si.ms;

import javax.annotation.Nonnull;
import javax.swing.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

/**
 * Creates threads/timers/executors and stuff
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public interface ThreadService extends ThreadFactory {
  /**
   * Creates a new thread
   */
  @Override
  @Nonnull
  Thread newThread(@Nonnull Runnable r);

  /**
   * Creates a new fixed thread pool
   */
  @Nonnull
  ExecutorService newFixedThreadPool(int nThreads, @Nonnull String name);

  /**
   * Creates a new cached thread pool
   */
  @Nonnull
  ExecutorService newCachedThreadPool(@Nonnull String name);

  /**
   * Creates a new scheduled executor service
   */
  @Nonnull
  ScheduledExecutorService newScheduledThreadPool(int nThreads, @Nonnull String name);

  /**
   * Creates a new timer
   */
  @Nonnull
  java.util.Timer newTimer(@Nonnull String name);

  /**
   * Creates a new swing timer
   */
  @Nonnull
  Timer newSwingTimer(@ms int delay);
}
