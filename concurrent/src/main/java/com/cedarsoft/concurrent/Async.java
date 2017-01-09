package com.cedarsoft.concurrent;


import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.Executor;

/**
 * Only the last call is executed
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@ThreadSafe
public class Async extends AbstractAsync {
  @Nonnull
  private final Executor executor;

  public Async(@Nonnull Executor executor) {
    this.executor = executor;
  }

  @Override
  protected void runInTargetThread(@Nonnull Runnable runnable) {
    executor.execute(runnable);
  }
}