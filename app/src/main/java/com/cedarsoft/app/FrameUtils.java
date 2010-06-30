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

import org.jetbrains.annotations.NotNull;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.Container;
import java.lang.InterruptedException;
import java.lang.reflect.InvocationTargetException;

/**
 * <p>FrameUtils class.</p>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class FrameUtils {

  /**
   * Shows a frame with the given content pane
   *
   * @param contentPane the content pane
   * @return the JFrame that is shown
   *
   * @throws InvocationTargetException
   *                                        if any.
   * @throws InterruptedException if any.
   */
  @NotNull
  public static JFrame showFrame( @NotNull Container contentPane ) throws InvocationTargetException, InterruptedException {
    final JFrame frame = new JFrame();
    frame.pack();
    frame.setLocationRelativeTo( null );
    frame.setDefaultCloseOperation( WindowConstants.DISPOSE_ON_CLOSE );

    frame.setContentPane( contentPane );

    Runnable startFrame = new Runnable() {
      @Override
      public void run() {
        frame.pack();
        frame.setSize( 800, 600 );
        frame.setVisible( true );
      }
    };
    if ( SwingUtilities.isEventDispatchThread() ) {
      startFrame.run();
    } else {
      SwingUtilities.invokeAndWait( startFrame );
    }
    return frame;
  }

}
