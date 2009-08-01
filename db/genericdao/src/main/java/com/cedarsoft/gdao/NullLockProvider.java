package com.cedarsoft.gdao;

import com.cedarsoft.NullLock;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.Lock;

/**
 *
 */
public class NullLockProvider<T> implements LockProvider<T> {
  @NotNull
  public static final LockProvider<Object> NULL = new LockProvider<Object>() {
    @NotNull
    public Lock getWriteLock( @NotNull Object object ) {
      return NullLock.LOCK;
    }
  };

  @NotNull
  public static <T> LockProvider<T> provider() {
    return ( LockProvider<T> ) NULL;
  }

  @NotNull
  public Lock getWriteLock( @NotNull Object object ) {
    return NullLock.LOCK;
  }
}
