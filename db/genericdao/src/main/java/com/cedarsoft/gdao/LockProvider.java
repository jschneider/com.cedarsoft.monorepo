package com.cedarsoft.gdao;

import com.cedarsoft.NullLock;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.Lock;

/**
 * Provides a lock for a given object
 */
public interface LockProvider<T> {
  /**
   * Returns the write lock for the given object.
   * If the object doesn't have a lock this method must return
   * a null object like {@link NullLock}.
   * <p/>
   * This method may return a new instance of write lock each time it is called.
   *
   * @param object the object
   * @return the lock (must not be null)
   */
  @NotNull
  Lock getWriteLock( @NotNull T object );
}