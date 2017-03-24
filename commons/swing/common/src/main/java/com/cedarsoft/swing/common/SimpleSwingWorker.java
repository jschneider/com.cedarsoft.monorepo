package com.cedarsoft.swing.common;

import com.cedarsoft.annotations.UiThread;

import javax.annotation.Nullable;
import javax.swing.SwingWorker;
import java.util.concurrent.ExecutionException;

/**
 * Simple swing worker implementation
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public abstract class SimpleSwingWorker<T> extends SwingWorker<T, Void> {
  @UiThread
  @Override
  protected final void done() {
    super.done();

    cleanup();
    try {
      done(get());
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Is only called if *no* exception is thrown
   */
  @UiThread
  protected void done(@Nullable T t) {

  }

  @UiThread
  protected void cleanup() {

  }
}
