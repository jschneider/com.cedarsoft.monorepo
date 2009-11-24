package com.cedarsoft.lock;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.ReadWriteLock;

/**
 * Implementations provide a lock
 */
public interface Lockable {
  /**
   * Returns the lock
   *
   * @return the lock
   */
  @NotNull
  ReadWriteLock getLock();
}