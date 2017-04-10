package com.cedarsoft.exceptions.handling;

import java.io.IOException;
import java.net.NoRouteToHostException;
import java.util.concurrent.RejectedExecutionException;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cedarsoft.annotations.AnyThread;
import com.cedarsoft.exceptions.ApplicationException;
import com.cedarsoft.exceptions.NotificationException;

/**
 * Silently handles exceptions
 */
@Singleton
public class SilentExceptionHandler extends ExceptionHandler {
  @Nonnull
  private static final Logger LOG = LoggerFactory.getLogger(SilentExceptionHandler.class.getName());

  @Nonnull
  private final ExceptionReporter exceptionReporter;

  @Inject
  public SilentExceptionHandler(@Nonnull ExceptionReporter exceptionReporter) {
    this.exceptionReporter = exceptionReporter;
  }

  @AnyThread
  @Override
  protected void handle(@Nonnull Thread thread, @Nonnull Throwable throwable, @Nonnull Throwable original) {
    if (throwable instanceof ApplicationException) {
      ignoreException(throwable, original);
      return;
    }

    if (throwable instanceof RejectedExecutionException) {
      handleRejectedExecutionException(throwable, original);
      return;
    }

    if (throwable instanceof NoRouteToHostException) {
      ignoreException(throwable, original);
      return;
    }

    if (throwable instanceof IOException) {
      reportException(throwable);
      return;
    }

    if (throwable instanceof NotificationException) {
      ignoreException(throwable, original);
      return;
    }

    reportException(throwable);
  }

  private static void ignoreException(@Nonnull Throwable throwable, @Nonnull Throwable original) {
    LOG.info("throwable <{}>, original = <{}>, will be ignored", throwable, original);
  }

  private void handleRejectedExecutionException(@Nonnull Throwable throwable, @Nonnull Throwable original) {
    reportException(throwable);
  }

  private void reportException(@Nonnull Throwable throwable) {
    exceptionReporter.report(throwable);
  }
}
