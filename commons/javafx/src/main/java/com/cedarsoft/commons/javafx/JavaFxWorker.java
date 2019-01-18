package com.cedarsoft.commons.javafx;

import java.util.concurrent.ExecutionException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.cedarsoft.annotations.NonUiThread;
import com.cedarsoft.annotations.UiThread;

import javafx.application.Platform;
import javafx.concurrent.Task;

/**
 * Similar to a simple SwingWorker.
 * Must not return "null". Instead use Optional
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public abstract class JavaFxWorker<T> extends Task<T> {
  @Nullable
  @Override
  protected final T call() throws Exception {
    return doInBackground();
  }

  @Nullable
  protected abstract T doInBackground() throws Exception;

  @UiThread
  @Override
  protected final void succeeded() {
    super.succeeded();
  }

  @UiThread
  @Override
  protected final void running() {
    super.running();
  }

  @UiThread
  @Override
  protected final void cancelled() {
    super.cancelled();
  }

  @UiThread
  @Override
  protected final void failed() {
    super.failed();
  }

  @NonUiThread
  @Override
  protected final void done() {
    super.done();

    Platform.runLater(() -> {
      cleanup();
      try {
        success(get());
      }
      catch (InterruptedException e) {
        failure(e);
      }
      catch (ExecutionException e) {
        failure(e.getCause());
      }
      catch (Exception e) {
        failure(e);
      }
    });
  }

  /**
   * Is called before #done(T);
   * This method is called even if {@link #doInBackground()} has thrown an exception
   */
  @UiThread
  protected void cleanup() {
  }

  /**
   * Is called the value that is returned by {@link #doInBackground()}.
   * Is only called if no exception has been thrown in {@link #doInBackground()}.
   */
  @UiThread
  protected void success(@Nullable T t) {
  }

  @UiThread
  protected void failure(@Nonnull Throwable e) {
    throw new RuntimeException(e);
  }

  /**
   * Schedules for execution
   *
   * @return the thread this worker runs its background stuff on
   */
  @Nonnull
  public Thread execute() {
    Thread thread = new Thread(this, "JavaFXWorker-Thread for <" + getClass().getName() + ">");
    thread.start();
    return thread;
  }
}
