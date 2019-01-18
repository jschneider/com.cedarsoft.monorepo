package com.cedarsoft.commons.javafx;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

import javax.annotation.Nonnull;

/**
 * A {@link Executor} that swallows {@link RejectedExecutionException} exceptions.
 */
public class SilentExecutor implements Executor {
  @Nonnull
  private final Executor executor;

  public SilentExecutor(@Nonnull Executor executor) {
    this.executor = executor;
  }

  @Nonnull
  public static Executor wrap(@Nonnull Executor executor) {
    return new SilentExecutor(executor);
  }

  @Override
  public void execute(@Nonnull Runnable command) {
    try {
      executor.execute(command);
    } catch (RejectedExecutionException ignored) {
      // ignored
    }
  }

}
