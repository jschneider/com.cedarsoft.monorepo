package com.cedarsoft.gdao;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 */
public interface GenericDaoManager {
  @NotNull
  <T> GenericDao<T> getDao( @NotNull Class<T> type );

  /**
   * Creates a dao for the given lock provider
   *
   * @param type         the tpye
   * @param lockProvider the lock provider
   * @param <T>          the type
   * @return the dao
   *
   * @throws UnsupportedOperationException if a lock provider is not supported
   */
  @NotNull
  <T> GenericDao<T> getDao( @NotNull Class<T> type, @Nullable LockProvider<T> lockProvider ) throws UnsupportedOperationException;

  /**
   * Shuts the dao manager down
   */
  void shutdown();
}