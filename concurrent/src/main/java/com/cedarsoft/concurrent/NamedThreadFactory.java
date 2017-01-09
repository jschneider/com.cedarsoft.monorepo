package com.cedarsoft.concurrent;

import javax.annotation.Nonnull;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Thread factory that takes a name
 */
public class NamedThreadFactory implements ThreadFactory {
  @Nonnull
  private final AtomicInteger threadNumber = new AtomicInteger(1);
  @Nonnull
  private final String prefix;

  @Nonnull
  private final ThreadFactory delegate;

  public NamedThreadFactory(@Nonnull String name) {
    this(new ThreadFactory() {
      @Override
      public Thread newThread(@Nonnull Runnable r) {
        return new Thread(r);
      }
    }, name);
  }

  public NamedThreadFactory(@Nonnull ThreadFactory delegate, @Nonnull String name) {
    this.delegate = delegate;
    prefix = name + "-";
  }

  @Nonnull
  @Override
  public Thread newThread(@Nonnull Runnable r) {
    Thread thread = delegate.newThread(r);
    thread.setName(createNewThreadName());
    return thread;
  }

  /**
   * Creates the next thread name
   */
  @Nonnull
  private String createNewThreadName() {
    return prefix + threadNumber.getAndIncrement();
  }
}