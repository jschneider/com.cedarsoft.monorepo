package com.cedarsoft.annotations.verification;

import javax.annotation.Nullable;
import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class VerifyUiThread {
  @Nullable
  private static final Method SWT_GET_CURRENT_METHOD = detectSwtGetCurrentMethod();
  @Nullable
  private static final Method FX_IS_FX_APPLICATION_THREAD_METHOD = detectFxMethod();

  @Nullable
  private static Method detectFxMethod() {
    try {
      Class<?> display = Class.forName( "javafx.application.Platform" );
      return display.getMethod( "isFxApplicationThread" );
    } catch ( ClassNotFoundException ignore ) {
    } catch ( NoSuchMethodException ignore ) {
    }
    return null;
  }

  @Nullable
  private static Method detectSwtGetCurrentMethod() {
    try {
      Class<?> display = Class.forName( "org.eclipse.swt.widgets.Display" );
      return display.getMethod( "getCurrent" );
    } catch ( ClassNotFoundException ignore ) {
    } catch ( NoSuchMethodException ignore ) {
    }
    return null;
  }

  public static void verifyUiThreadAsserted() {
    assert verifyUiThread();
  }

  public static boolean verifyUiThread() {
    try {
      if ( SWT_GET_CURRENT_METHOD != null && SWT_GET_CURRENT_METHOD.invoke( null, null ) != null ) {
        return true;
      }
    } catch ( IllegalAccessException e ) {
      throw new RuntimeException( e ); //TODO remove exception(?)
    } catch ( InvocationTargetException e ) {
      throw new RuntimeException( e ); //TODO remove exception(?)
    }

    try {
      if ( FX_IS_FX_APPLICATION_THREAD_METHOD != null && FX_IS_FX_APPLICATION_THREAD_METHOD.invoke( null, null ) == Boolean.TRUE ) {
        return true;
      }
    } catch ( IllegalAccessException e ) {
      throw new RuntimeException( e ); //TODO remove exception(?)
    } catch ( InvocationTargetException e ) {
      throw new RuntimeException( e ); //TODO remove exception(?)
    }

    if ( SwingUtilities.isEventDispatchThread() ) {
      return true;
    }

    throw new IllegalStateException( "Called from illegal thread. Must be called from UI thread" );
  }
}
