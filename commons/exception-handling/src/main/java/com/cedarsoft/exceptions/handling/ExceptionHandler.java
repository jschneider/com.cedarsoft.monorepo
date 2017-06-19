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

import com.cedarsoft.exceptions.CanceledException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Handles exceptions
 */
public abstract class ExceptionHandler implements Thread.UncaughtExceptionHandler {
  private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandler.class.getName());

  /**
   * Registers the exception handler for all threads.
   */
  @PostConstruct
  public void registerExceptionHandler() {
    Thread.setDefaultUncaughtExceptionHandler(this);
  }

  @Override
  public final void uncaughtException(Thread t, Throwable e) {
    try {
      Throwable purged = ExceptionPurger.purge(e);
      LOG.info("Handling Exception", e);

      handle(t, purged, e);
    } catch (CanceledException ignore) {
    } catch (Throwable throwable) {
      LOG.error("could not handle exception", throwable);
      LOG.error("original exception", e);

      //Last resort if exception handling no longer works
      try {
        SwingUtilities.invokeLater(() -> {
          JOptionPane.showMessageDialog(null, "Internal problem. Please contact our technical support.\nThe application will be closed.\n\n" + extractDetailsFromThrowable(e), "Internal Problem", JOptionPane.ERROR_MESSAGE);
          System.exit(1);
        });
      } catch (Exception lastException) {
        LOG.error("Could not show last exception dialog", lastException);
      }
    }
  }

  /**
   * Extracts the details if possible
   */
  @Nonnull
  private static String extractDetailsFromThrowable(@Nonnull Throwable t) {
    try {
      return t.getClass().getName() + ": " + t.getMessage();
    } catch (Throwable ignore) {
    }
    return "<undefined>";
  }

  /**
   * Handles the given throwable
   *
   * @param thread    the thread the throwable has occurred in
   * @param throwable the throwable (purged)
   * @param original  the original throwable
   */
  protected abstract void handle(@Nonnull Thread thread, @Nonnull Throwable throwable, @Nonnull Throwable original);
}
