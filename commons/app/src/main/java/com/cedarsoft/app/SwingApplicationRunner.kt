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
package com.cedarsoft.app

import com.cedarsoft.annotations.NonBlocking
import com.cedarsoft.annotations.NonUiThread
import com.cedarsoft.annotations.UiThread
import java.awt.SplashScreen
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.WindowConstants

/**
 */
class SwingApplicationRunner(
  val application: Application
) {
  @MainFrame
  @get:MainFrame
  var mainFrame: JFrame? = null
    private set

  init {
    application.prepareExceptionHandling()
    application.prepare()
  }

  @NonUiThread
  @NonBlocking
  fun boot() {
    SwingUtilities.invokeLater(Runnable {
      val splash = SplashScreen.getSplashScreen() ?: return@Runnable

      application.updateSplash(splash)
    })

    application.createFrame().let {
      mainFrame = it
      SwingUtilities.invokeLater {
        showFrame(it)
      }
    }
  }

  @UiThread
  private fun showFrame(frame: JFrame) {
    application.prepareFrameForShowing(frame)
    frame.pack()
    application.setSizeAndLocation(frame)
    frame.isVisible = true
  }


  interface Application {
    /**
     * Prepares the exception handling - e.g. registering a default exception handler
     */
    @NonUiThread
    fun prepareExceptionHandling()

    /**
     * Prepares the application.
     * E.g. should create the injector and return the frame (Guice: Key.get( JFrame.class, MainFrame.class ))
     */
    @NonUiThread
    fun prepare()

    /**
     * Is only called if there is a splash screen
     *
     * @param splash the splash
     */
    @UiThread
    fun updateSplash(splash: SplashScreen)

    /**
     * Creates the frame.
     * This method is called from the background thread. Do *not* call pack() or setVisible()
     *
     * @return the created frame
     */
    @NonUiThread
    fun createFrame(): JFrame

    /**
     * Is called before the frame is shown.
     *
     * @param frame the frame
     */
    @UiThread
    fun prepareFrameForShowing(frame: JFrame)

    /**
     * Sets the size and location for the given frame
     *
     * @param frame the frame
     */
    @UiThread
    fun setSizeAndLocation(frame: JFrame)
  }

  /**
   * Abstract base class for application
   */
  abstract class AbstractApplication : Application {
    override fun prepareFrameForShowing(frame: JFrame) {
      frame.defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
    }

    override fun setSizeAndLocation(frame: JFrame) {
      frame.setSize(1024, 768)
      frame.setLocationRelativeTo(null)
    }
  }
}
