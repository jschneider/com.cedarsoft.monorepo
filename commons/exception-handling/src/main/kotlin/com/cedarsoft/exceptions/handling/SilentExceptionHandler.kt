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

import it.neckar.open.annotations.AnyThread
import com.cedarsoft.exceptions.ApplicationException
import com.cedarsoft.exceptions.NotificationException
import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.NoRouteToHostException
import java.util.concurrent.RejectedExecutionException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Silently handles exceptions
 */
@Singleton
class SilentExceptionHandler @Inject
constructor(
  private val exceptionReporter: ExceptionReporter
) : ExceptionHandler() {

  @AnyThread
  override fun handle(thread: Thread, throwable: Throwable, original: Throwable) {
    if (throwable is ApplicationException) {
      ignoreException(throwable, original)
      return
    }

    if (throwable is RejectedExecutionException) {
      handleRejectedExecutionException(original)
      return
    }

    if (throwable is NoRouteToHostException) {
      ignoreException(throwable, original)
      return
    }

    if (throwable is IOException) {
      reportException(throwable)
      return
    }

    if (throwable is NotificationException) {
      ignoreException(throwable, original)
      return
    }

    reportException(throwable)
  }

  private fun handleRejectedExecutionException(original: Throwable) {
    reportException(original)
  }

  private fun reportException(throwable: Throwable) {
    exceptionReporter.report(throwable)
  }

  companion object {
    private val LOG = LoggerFactory.getLogger(SilentExceptionHandler::class.java.name)

    private fun ignoreException(throwable: Throwable, original: Throwable) {
      LOG.info("throwable <{}>, original = <{}>, will be ignored", throwable, original)
    }
  }
}
