/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.cedarsoft.org/gpl3ce
 * (GPL 3 with Classpath Exception)
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

package com.cedarsoft.exceptions.handling

import com.cedarsoft.annotations.AnyThread
import com.cedarsoft.commons.javafx.FxUtils
import com.cedarsoft.exceptions.ApplicationException
import com.cedarsoft.exceptions.NotificationException
import com.cedarsoft.exceptions.handling.notification.DetailsCallback
import com.cedarsoft.exceptions.handling.notification.FxNotificationService
import com.cedarsoft.exceptions.handling.notification.Notification
import com.cedarsoft.version.Version
import com.google.common.collect.ImmutableMap
import javafx.application.Platform
import java.io.IOException
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Swing based exception handler
 */
open class FxExceptionHandler @JvmOverloads constructor(
  private val notificationService: FxNotificationService,
  val applicationVersion: Version,
  val exceptionReporter: ExceptionReporter,
  exceptionTypeHandlers: Map<Class<out Throwable>, ExceptionTypeHandler> = HashMap()
) :
  ExceptionHandler() {

  private val exceptionTypeHandlers: Map<Class<out Throwable>, ExceptionTypeHandler>

  private val dialogOpen = AtomicBoolean(false)

  init {
    this.exceptionTypeHandlers = ImmutableMap.copyOf(exceptionTypeHandlers)
  }

  override fun handle(thread: Thread, throwable: Throwable, original: Throwable) {
    //Avoid multiple dialogs
    if (dialogOpen.get()) {
      return
    }

    val exceptionTypeHandler = exceptionTypeHandlers[throwable.javaClass]
    if (exceptionTypeHandler != null) {
      exceptionTypeHandler.handle(this, thread, throwable, original)
      return
    }

    if (throwable is ApplicationException) {
      showApplicationExceptionDialog(throwable)
      return
    }

    if (throwable is IOException) {
      handleIoException(throwable, original)
      return
    }

    if (throwable is NotificationException) {
      handleNotificationException(throwable, original)
      return
    }

    handleInternalError(throwable, original)
  }

  fun handleNotificationException(throwable: NotificationException, original: Throwable) {
    Platform.runLater {
      val notification = Notification(throwable.title, throwable.message ?: "", object : DetailsCallback {
        override fun detailsClicked(notification: Notification) {
          handleInternalError(original, original)
        }
      })
      notificationService.showNotification(notification)
    }
  }

  open fun handleIoException(throwable: IOException, original: Throwable) {
    //val title = Messages.get("io.exception.title", throwable.message)
    //val errorMessage = Messages.get("io.exception.description", throwable.message)

    showExceptionDialog(original)
  }

  /**
   * Shows an internal error dialog
   *
   * @param throwable the (purged) exception
   * @param original  the original exception
   */
  open fun handleInternalError(throwable: Throwable, original: Throwable) {
    //var throwableMessage: String? = throwable.message
    //if (throwableMessage == null) {
    //  throwableMessage = ""
    //}
    //val titleErrorMessage = if (throwableMessage.length > 64) throwableMessage.substring(0, 64) + "..." else throwableMessage
    //val basicErrorMessage = if (throwableMessage.length > 512) throwableMessage.substring(0, 512) + "..." else throwableMessage

    showExceptionDialog(original)
  }

  @AnyThread
  private fun showExceptionDialog(original: Throwable) {
    Platform.runLater(Runnable {
      try {
        if (dialogOpen.get()) {
          return@Runnable
        }

        try {
          dialogOpen.set(true)
          val internalExceptionDialog = InternalExceptionFxDialog(original)
          internalExceptionDialog.showAndWait()
        } finally {
          dialogOpen.set(false)
        }
      } catch (e: Throwable) {
        System.err.println("Error on handling exception: " + e.message)
        e.printStackTrace()
      }
    })
  }

  /**
   * Shows the application exception dialog
   */
  @AnyThread
  fun showApplicationExceptionDialog(applicationException: ApplicationException) {
    Platform.runLater {
      if (dialogOpen.get()) {
        return@runLater
      }

      try {
        dialogOpen.set(true)
        val dialog = ApplicationExceptionFxDialog(applicationException)
        dialog.initOwner(FxUtils.stageSafe)
        dialog.show()
      } finally {
        dialogOpen.set(false)
      }
    }
  }

  /**
   * Implementing classes support an exception
   */
  @FunctionalInterface
  interface ExceptionTypeHandler {
    fun handle(exceptionHandler: FxExceptionHandler, thread: Thread, throwable: Throwable, original: Throwable)
  }
}
