package com.cedarsoft.utils;

import org.jetbrains.annotations.NotNull;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.Container;
import java.lang.reflect.InvocationTargetException;

/**
 *
 */
public class FrameUtils {

  /**
   * Shows a frame with the given content pane
   *
   * @param contentPane the content pane
   * @return the JFrame that is shown
   */
  @NotNull
  public static JFrame showFrame( @NotNull Container contentPane ) throws InvocationTargetException, InterruptedException {
    final JFrame frame = new JFrame();
    frame.pack();
    frame.setLocationRelativeTo( null );
    frame.setDefaultCloseOperation( WindowConstants.DISPOSE_ON_CLOSE );

    frame.setContentPane( contentPane );

    Runnable startFrame = new Runnable() {
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
