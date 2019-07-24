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

import com.cedarsoft.exceptions.CanceledException
import org.slf4j.LoggerFactory
import javax.annotation.PostConstruct
import javax.swing.JOptionPane
import javax.swing.SwingUtilities
import kotlin.system.exitProcess

/**
 * Handles exceptions
 */
abstract class ExceptionHandler : Thread.UncaughtExceptionHandler {
  /**
   * Registers the exception handler for all threads.
   */
  @PostConstruct
  fun registerExceptionHandler() {
    Thread.setDefaultUncaughtExceptionHandler(this)
  }

  override fun uncaughtException(t: Thread, e: Throwable) {
    try {
      val purged = ExceptionPurger.purge(e)
      LOG.info("Handling Exception", e)

      handle(t, purged, e)
    } catch (ignore: CanceledException) {
    } catch (throwable: Throwable) {
      LOG.error("could not handle exception", throwable)
      LOG.error("original exception", e)

      //Last resort if exception handling no longer works
      try {
        SwingUtilities.invokeLater {
          JOptionPane.showMessageDialog(null, "Internal problem. Please contact our technical support.\nThe application will be closed.\n\n" + extractDetailsFromThrowable(e), "Internal Problem", JOptionPane.ERROR_MESSAGE)
          exitProcess(1)
        }
      } catch (lastException: Exception) {
        LOG.error("Could not show last exception dialog", lastException)
      }
    }
  }

  /**
   * Handles the given throwable
   *
   * @param thread    the thread the throwable has occurred in
   * @param throwable the throwable (purged)
   * @param original  the original throwable
   */
  protected abstract fun handle(thread: Thread, throwable: Throwable, original: Throwable)

  companion object {
    private val LOG = LoggerFactory.getLogger(ExceptionHandler::class.java.name)

    /**
     * Extracts the details if possible
     */
    private fun extractDetailsFromThrowable(t: Throwable): String {
      try {
        return t.javaClass.name + ": " + t.message
      } catch (ignore: Throwable) {
      }

      return "<undefined>"
    }
  }
}
