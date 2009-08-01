package com.cedarsoft;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.Window;
import java.lang.reflect.InvocationTargetException;

/**
 *
 */
public class SwingHelper {
  private SwingHelper() {
  }

  /**
   * Prüft ob der aktuelle Thread der EventDispatchThread ist, ansonsten wird
   * eine Exception geworfen.
   */
  public static void assertEventThread() {
    if ( !SwingUtilities.isEventDispatchThread() ) {
      throw new IllegalThreadStateException( "You are not calling from EventThread" );
    }
  }

  /**
   * Throws an exception if the current thread is the EDT.
   */
  public static void assertNotEventThread() {
    if ( SwingUtilities.isEventDispatchThread() ) {
      throw new IllegalThreadStateException( "You are calling from EDT" );
    }
  }

  public static void invokeAndWait( @NotNull Runnable runnable ) {
    if ( SwingUtilities.isEventDispatchThread() ) {
      runnable.run();
    } else {
      try {
        SwingUtilities.invokeAndWait( runnable );
      }
      catch ( InterruptedException ignore ) {
      }
      catch ( InvocationTargetException e ) {
        throw new RuntimeException( e );
      }
    }
  }

  /**
   * Liefert das Rootfenster (JFrame) für eine Komponente.
   *
   * @param component
   * @return das Rootfenster zu einer Komponente oder null falls die Komponente nicht zu einem
   *         JFrame gehört.
   */
  @Nullable
  public static JFrame rootFrame( @NotNull Component component ) {
    Window window = SwingUtilities.getWindowAncestor( component );
    while ( SwingUtilities.getWindowAncestor( window ) != null ) {
      window = SwingUtilities.getWindowAncestor( window );
    }
    if ( window instanceof JFrame ) {
      return ( JFrame ) window;
    }
    return null;
  }

  /**
   * Calls {@link SwingUtilities#isEventDispatchThread()}
   *
   * @return whether the current thread is the EDT
   */
  public static boolean isEventDispatchThread() {
    return SwingUtilities.isEventDispatchThread();
  }

  /**
   * Waits until the swing thread has been finished
   */
  public static void waitForSwingThread() {
    invokeAndWait( new Runnable() {
      public void run() {
      }
    } );
  }

  /**
   * Invokes the runnable as early as possible.
   * If in EDT the runnable is executed immediately, else in invokeLater
   *
   * @param runnable the runnable
   */
  public static void early( @NotNull Runnable runnable ) {
    if ( SwingHelper.isEventDispatchThread() ) {
      runnable.run();
    } else {
      SwingUtilities.invokeLater( runnable );
    }
  }
}
