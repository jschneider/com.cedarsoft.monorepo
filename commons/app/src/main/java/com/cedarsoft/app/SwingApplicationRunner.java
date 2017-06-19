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
package com.cedarsoft.app;

import com.cedarsoft.annotations.NonBlocking;
import com.cedarsoft.annotations.NonUiThread;
import com.cedarsoft.annotations.UiThread;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.SplashScreen;
import java.io.IOException;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class SwingApplicationRunner {
  @MainFrame
  @Nullable
  private JFrame mainFrame;
  @Nonnull
  private final Application application;

  public SwingApplicationRunner( @Nonnull Application application ) {
    this.application = application;
    application.prepareExceptionHandling();
    application.prepare();
  }

  @NonUiThread
  @NonBlocking
  public void boot() {
    SwingUtilities.invokeLater( new Runnable() {
      @Override
      public void run() {
        final SplashScreen splash = SplashScreen.getSplashScreen();
        if ( splash == null ) {
          return;
        }

        application.updateSplash( splash );
      }
    } );

    mainFrame = application.createFrame();

    SwingUtilities.invokeLater( new Runnable() {
      @Override
      public void run() {
        try {
          showFrame( mainFrame );
        } catch ( Exception e ) {
          throw new RuntimeException( e );
        }
      }
    } );
  }

  @UiThread
  private void showFrame( @Nonnull final JFrame frame ) throws IOException {
    application.prepareFrameForShowing( frame );
    frame.pack();
    application.setSizeAndLocation( frame );
    frame.setVisible( true );
  }

  @Nullable
  @MainFrame
  public JFrame getMainFrame() {
    return mainFrame;
  }


  public interface Application {
    /**
     * Prepares the exception handling - e.g. registering a default exception handler
     */
    @NonUiThread
    void prepareExceptionHandling();

    /**
     * Prepares the application.
     * E.g. should create the injector and return the frame (Guice: Key.get( JFrame.class, MainFrame.class ))
     */
    @NonUiThread
    void prepare();

    /**
     * Is only called if there is a splash screen
     *
     * @param splash the splash
     */
    @UiThread
    void updateSplash( @Nonnull SplashScreen splash );

    /**
     * Creates the frame.
     * This method is called from the background thread. Do *not* call pack() or setVisible()
     *
     * @return the created frame
     */
    @NonUiThread
    @Nonnull
    JFrame createFrame();

    /**
     * Is called before the frame is shown.
     *
     * @param frame the frame
     */
    @UiThread
    void prepareFrameForShowing( @Nonnull JFrame frame );

    /**
     * Sets the size and location for the given frame
     *
     * @param frame the frame
     */
    @UiThread
    void setSizeAndLocation( @Nonnull JFrame frame );
  }

  /**
   * Abstract base class for application
   */
  public abstract static class AbstractApplication implements Application {
    @Override
    public void prepareFrameForShowing( @Nonnull JFrame frame ) {
      frame.setDefaultCloseOperation( WindowConstants.DISPOSE_ON_CLOSE );
    }

    @Override
    public void setSizeAndLocation( @Nonnull JFrame frame ) {
      frame.setSize( 1024, 768 );
      frame.setLocationRelativeTo( null );
    }
  }

}
