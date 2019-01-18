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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cedarsoft.annotations.AnyThread;
import com.cedarsoft.exceptions.ApplicationException;
import com.cedarsoft.exceptions.NotificationException;
import com.cedarsoft.exceptions.handling.notification.FxNotificationService;
import com.cedarsoft.exceptions.handling.notification.Notification;
import com.cedarsoft.version.Version;
import com.google.common.collect.ImmutableMap;

import javafx.application.Platform;

/**
 * Swing based exception handler
 */
public class FxExceptionHandler extends ExceptionHandler {
  private static final Logger LOG = LoggerFactory.getLogger(FxExceptionHandler.class.getName());

  @Nonnull
  private final FxNotificationService notificationService;
  @Nonnull
  private final Version applicationVersion;
  @Nonnull
  private final ExceptionReporter exceptionReporter;

  @Nonnull
  private final Map<Class<? extends Throwable>, ExceptionTypeHandler> exceptionTypeHandlers;

  public FxExceptionHandler(@Nonnull FxNotificationService notificationService, @Nonnull Version applicationVersion, @Nonnull ExceptionReporter exceptionReporter) {
    this(notificationService, applicationVersion, exceptionReporter, new HashMap<>());
  }

  public FxExceptionHandler(@Nonnull FxNotificationService notificationService, @Nonnull Version applicationVersion, @Nonnull ExceptionReporter exceptionReporter, @Nonnull Map<Class<? extends Throwable>, ExceptionTypeHandler> exceptionTypeHandlers) {
    this.notificationService = notificationService;
    this.applicationVersion = applicationVersion;
    this.exceptionReporter = exceptionReporter;
    this.exceptionTypeHandlers = ImmutableMap.copyOf(exceptionTypeHandlers);
  }

  @Nonnull
  private final AtomicBoolean dialogOpen = new AtomicBoolean(false);

  @Override
  protected void handle(@Nonnull Thread thread, @Nonnull Throwable throwable, @Nonnull Throwable original) {
    //Avoid multiple dialogs
    if (dialogOpen.get()) {
      return;
    }

    @Nullable ExceptionTypeHandler exceptionTypeHandler = exceptionTypeHandlers.get(throwable.getClass());
    if (exceptionTypeHandler != null) {
      exceptionTypeHandler.handle(this, thread, throwable, original);
    }

    if (throwable instanceof ApplicationException) {
      showApplicationExceptionDialog((ApplicationException) throwable);
      return;
    }

    if (throwable instanceof IOException) {
      handleIoException((IOException) throwable, original);
      return;
    }

    if (throwable instanceof NotificationException) {
      handleNotificationException((NotificationException) throwable, original);
      return;
    }

    handleInternalError(throwable, original);
  }

  public void handleNotificationException(@Nonnull NotificationException throwable, @Nonnull Throwable original) {
    Platform.runLater(() -> notificationService.showNotification(new Notification(throwable.getTitle(), throwable.getMessage(), notification -> handleInternalError(original, original))));
  }

  public void handleIoException(@Nonnull IOException throwable, @Nonnull Throwable original) {
    String title = Messages.get("io.exception.title", throwable.getMessage());
    String errorMessage = Messages.get("io.exception.description", throwable.getMessage());

    showExceptionDialog(original);
  }

  /**
   * Shows an internal error dialog
   *
   * @param throwable the (purged) exception
   * @param original  the original exception
   */
  public void handleInternalError(@Nonnull Throwable throwable, @Nonnull final Throwable original) {
    String throwableMessage = throwable.getMessage();
    if (throwableMessage == null) {
      throwableMessage = "";
    }
    String titleErrorMessage = throwableMessage.length() > 64 ? throwableMessage.substring(0, 64) + "..." : throwableMessage;
    String basicErrorMessage = throwableMessage.length() > 512 ? throwableMessage.substring(0, 512) + "..." : throwableMessage;

    showExceptionDialog(original);
  }

  @AnyThread
  private void showExceptionDialog(@Nonnull final Throwable original) {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        try {
          if (dialogOpen.get()) {
            return;
          }

          try {
            dialogOpen.set(true);
            InternalExceptionFxDialog internalExceptionDialog = new InternalExceptionFxDialog(original);
            internalExceptionDialog.showAndWait();
          } finally {
            dialogOpen.set(false);
          }
        } catch (Throwable e) {
          System.err.println("Error on handling exception: " + e.getMessage());
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Shows the application exception dialog
   */
  @AnyThread
  public void showApplicationExceptionDialog(@Nonnull ApplicationException applicationException) {
    Platform.runLater(() -> {
      if (dialogOpen.get()) {
        return;
      }

      try {
        dialogOpen.set(true);
        ApplicationExceptionFxDialog dialog = new ApplicationExceptionFxDialog(applicationException);
        dialog.show();
      } finally {
        dialogOpen.set(false);
      }
    });
  }

  /**
   * Implementing classes support an exception
   */
  @FunctionalInterface
  public interface ExceptionTypeHandler {
    void handle(@Nonnull FxExceptionHandler exceptionHandler, @Nonnull Thread thread, @Nonnull Throwable throwable, @Nonnull Throwable original);
  }
}
