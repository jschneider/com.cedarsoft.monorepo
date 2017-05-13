package com.cedarsoft.commons;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Represents a field that is instantiated on first access
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class LazyField<T> {
  @Nonnull
  private final ReadWriteLock lock;

  @Nonnull
  private final InstanceFactory<T> instanceFactory;

  @Nullable
  private T instance;

  public LazyField( @Nonnull InstanceFactory<T> instanceFactory ) {
    this( instanceFactory, new ReentrantReadWriteLock() );
  }

  public LazyField( @Nonnull InstanceFactory<T> instanceFactory, @Nonnull ReadWriteLock lock ) {
    this.instanceFactory = instanceFactory;
    this.lock = lock;
  }

  @Nonnull
  public T getInstance() {
    lock.readLock().lock();
    try {
      if ( instance != null ) {
        return instance;
      }
    } finally {
      lock.readLock().unlock();
    }

    lock.writeLock().lock();
    try {
      //noinspection ConstantConditions
      if ( instance != null ) {
        return instance;
      }

      instance = instanceFactory.create();
      return instance;
    } finally {
      lock.writeLock().unlock();
    }
  }

  @Nullable
  public T getInstanceNullable() {
    lock.readLock().lock();
    try {
      return instance;
    } finally {
      lock.readLock().unlock();
    }
  }

  @Nonnull
  public ReadWriteLock getLock() {
    return lock;
  }

  public interface InstanceFactory<T> {
    @Nonnull
    T create();
  }
}