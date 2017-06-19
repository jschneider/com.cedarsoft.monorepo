/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce
 *         (GPL 3 with Classpath Exception)
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */
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
