package com.cedarsoft.concurrent;

import com.cedarsoft.annotations.NonBlocking;
import javafx.application.Platform;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import javax.swing.SwingUtilities;

/**
 * Allows lazy calls in the ui thread.
 * Only the *last* call for each runnable is called.
 * <p>
 * This class is useful if there are many background events and
 * the UI should only updated as fast as possible/necessary.
 * <p>
 * Therefore not for every change a new event is generated which might
 * overload the UI thread.
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@ThreadSafe
public class FxAsync extends AbstractAsync {
  /**
   * Run in target thread
   */
  @Override
  @NonBlocking
  protected void runInTargetThread(@Nonnull Runnable runnable) {
    Platform.runLater(runnable);
  }
}
