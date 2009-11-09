package com.cedarsoft.gdao.caching;

import com.cedarsoft.gdao.GenericService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.Override;

/**
 *
 */
public class CachingService<T> extends CachingDao<T> implements GenericService<T> {
  public CachingService( @NotNull GenericService<T> backingService ) {
    super( backingService );
  }

  @NotNull
  protected GenericService<T> getBackingService() {
    return ( GenericService<T> ) super.getBackingDao();
  }

  @Override
  @Nullable
  public <R> R perform( @NotNull final ServiceCallback<T, R> callback ) {
    R returnValue = getBackingService().perform( callback );
    cache.getLock().writeLock().lock();
    try {
      cache.clear();
      cache.addAll( getBackingService().findAll() );
    } finally {
      cache.getLock().writeLock().unlock();
    }
    return returnValue;
  }
}
