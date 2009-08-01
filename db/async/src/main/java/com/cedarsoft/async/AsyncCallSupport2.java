package com.cedarsoft.async;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 */
public class AsyncCallSupport2<T> {
  @NotNull
  private final ExecutorService executorService;

  public AsyncCallSupport2() {
    executorService = Executors.newFixedThreadPool( 1 );
  }

  @NotNull
  public Future<T> invoke( @NotNull Callable<T> callable ) throws AsynchroniousInvocationException {
    return executorService.submit( callable );
  }

  public void shutdown() {
    executorService.shutdown();
  }

  public boolean isShutdown() {
    return executorService.isShutdown();
  }
}
