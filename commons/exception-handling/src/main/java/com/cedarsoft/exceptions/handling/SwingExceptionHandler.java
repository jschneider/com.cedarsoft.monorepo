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

import com.cedarsoft.annotations.AnyThread;
import com.cedarsoft.annotations.UiThread;
import com.cedarsoft.exceptions.ApplicationException;
import com.cedarsoft.exceptions.NotificationException;
import com.cedarsoft.exceptions.handling.notification.Notification;
import com.cedarsoft.exceptions.handling.notification.NotificationService;
import com.cedarsoft.swing.common.SwingHelper;
import com.cedarsoft.swing.common.dialog.OptionDialog;
import com.cedarsoft.version.Version;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Swing based exception handler
 */
public class SwingExceptionHandler extends ExceptionHandler {
  private static final Logger LOG = LoggerFactory.getLogger(SwingExceptionHandler.class.getName());

  @Nonnull
  private final NotificationService notificationService;
  @Nonnull
  private final Version applicationVersion;
  @Nonnull
  private final ExceptionReporter exceptionReporter;

  @Nonnull
  private final Map<Class<? extends Throwable>, ExceptionTypeHandler> exceptionTypeHandlers;

  public SwingExceptionHandler(@Nonnull NotificationService notificationService, @Nonnull Version applicationVersion, @Nonnull ExceptionReporter exceptionReporter) {
    this(notificationService, applicationVersion, exceptionReporter, new HashMap<>());
  }

  public SwingExceptionHandler(@Nonnull NotificationService notificationService, @Nonnull Version applicationVersion, @Nonnull ExceptionReporter exceptionReporter, @Nonnull Map<Class<? extends Throwable>, ExceptionTypeHandler> exceptionTypeHandlers) {
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
    SwingUtilities.invokeLater(() -> notificationService.showNotification(new Notification(throwable.getTitle(), throwable.getMessage(), notification -> handleInternalError(original, original))));
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

  private void showExceptionDialog(@Nonnull final Throwable original) {
    SwingUtilities.invokeLater(new Runnable() {
      @UiThread
      @Override
      public void run() {
        //Remember the parent frame
        try {
          if (dialogOpen.get()) {
            return;
          }

          try {
            dialogOpen.set(true);
            InternalExceptionDialog internalExceptionDialog = new InternalExceptionDialog(SwingHelper.getFrameSafe(), original, new ExceptionReporter() {
              @Override
              public void report(@Nonnull Throwable throwable) {
                ReportingExceptionsDialog reportingExceptionsDialog = new ReportingExceptionsDialog(SwingHelper.getFrameSafe());
                reportingExceptionsDialog.setVisibleNonBlocking();

                new SwingWorker<Void, Void>() {
                  @Override
                  @Nullable
                  protected Void doInBackground() throws Exception {
                    exceptionReporter.report(throwable);
                    return null;
                  }

                  @Override
                  protected void done() {
                    super.done();
                    reportingExceptionsDialog.cancel();

                    try {
                      get();
                      OptionDialog.showMessageDialog(SwingHelper.getFrameSafe(), "Problem has been reported successfully", "The exception has been reported successfully", OptionDialog.OptionType.OK_OPTION);
                    } catch (InterruptedException | ExecutionException e) {
                      //We can't do anything about this. Just log it...
                      LOG.warn("Exception reporting failed due to", e);

                      OptionDialog.showMessageDialog(SwingHelper.getFrameSafe(), "Problem reporting has failed due to: \n" + e.getClass().getName() + ":\n" + e.getMessage(), "Problem reporting has failed", OptionDialog.OptionType.OK_OPTION);
                    }
                  }
                }.execute();
              }
            }, applicationVersion);
            internalExceptionDialog.setVisible(true);
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
    SwingUtilities.invokeLater(() -> {
      if (dialogOpen.get()) {
        return;
      }

      try {
        dialogOpen.set(true);
        ApplicationExceptionDialog dialog = new ApplicationExceptionDialog(SwingHelper.getFrameSafe(), applicationException, null);
        dialog.setVisible(true);
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
    void handle(@Nonnull SwingExceptionHandler swingExceptionHandler, @Nonnull Thread thread, @Nonnull Throwable throwable, @Nonnull Throwable original);
  }
}
