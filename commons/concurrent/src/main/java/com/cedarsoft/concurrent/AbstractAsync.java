package com.cedarsoft.concurrent;

import com.cedarsoft.annotations.NonBlocking;
import com.cedarsoft.annotations.NonUiThread;
import com.cedarsoft.annotations.UiThread;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Abstract base class for async execution of scheduled runnables.
 * Only the latest runnable is executed
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public abstract class AbstractAsync {
  @Nonnull
  private final ReadWriteLock lock = new ReentrantReadWriteLock();

  @GuardedBy("lock")
  @Nonnull
  private final Map<Object, Runnable> scheduledRunnables = new HashMap<>();

  /**
   * Asynchronously calls only the last added runnable.
   */
  @NonUiThread
  @UiThread
  public void last(@Nonnull final Runnable runnable) {
    last(runnable.getClass(), runnable);
  }

  /**
   * Asynchronously calls only the last runnable.
   */
  @NonUiThread
  @UiThread
  public void last(@Nonnull final Object key, @Nonnull final Runnable runnable) {
    lock.writeLock().lock();
    try {
      if (scheduledRunnables.put(key, runnable) != null) {
        //There is another job scheduled, so we do not have to reschedule it
        return;
      }
    } finally {
      lock.writeLock().unlock();
    }

    //There has no other event been scheduled
    runInTargetThread(() -> get(key).run());
  }

  /**
   * Returns the runnable for the given key
   */
  @UiThread
  @Nonnull
  private Runnable get(@Nonnull Object key) {
    lock.writeLock().lock();
    try {
      @Nullable
      Runnable found = scheduledRunnables.remove(key);
      if (found == null) {
        throw new IllegalStateException("No job found for <" + key + ">");
      }
      return found;
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * Run in target thread
   */
  @NonBlocking
  protected abstract void runInTargetThread(@Nonnull Runnable runnable);
}
