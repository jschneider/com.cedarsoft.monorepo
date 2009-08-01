package com.cedarsoft.gdao;

import org.jetbrains.annotations.NotNull;

/**
 *
 */
public interface GenericServiceManager {
  @NotNull
      <T> GenericService<T> getService( @NotNull Class<T> type );
}