package com.cedarsoft.inject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import javax.inject.Provider;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A provider that caches the value it has created the first time
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class CachingProvider<T> implements Provider<T> {
  @Nonnull
  private final ReadWriteLock lock = new ReentrantReadWriteLock();

  @Nonnull
  private final Provider<T> provider;

  @Nullable
  @GuardedBy("lock")
  private T ref;

  public CachingProvider(@Nonnull Provider<T> provider) {
    this.provider = provider;
  }

  @Override
  public T get() {
    lock.writeLock().lock();
    try {
      if (ref != null) {
        return ref;
      }

      ref = provider.get();
      return ref;
    } finally {
      lock.writeLock().unlock();
    }
  }
}
