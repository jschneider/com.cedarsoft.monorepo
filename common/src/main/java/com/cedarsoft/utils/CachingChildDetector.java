package com.cedarsoft.utils;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.WeakHashMap;

/**
 * Special implementation of a child detector that always returns the same list for a given parent.
 */
public abstract class CachingChildDetector<P, C> extends AbstractChildDetector<P, C> {
  @NotNull
  private final Cache<P, List<? extends C>> childrenCache = new HashedCache<P, List<? extends C>>( new WeakHashMap<P, List<? extends C>>(), new Cache.Factory<P, List<? extends C>>() {
    @NotNull
    public List<? extends C> create( @NotNull P key ) {
      return createChildren( key );
    }
  } );

  @NotNull
  protected abstract List<? extends C> createChildren( @NotNull P parent );

  @NotNull
  public final List<? extends C> findChildren( @NotNull P parent ) {
    return childrenCache.get( parent );
  }

  public void handleModified( @NotNull P parent ) {
    invalidateCache( parent );
  }

  public void invalidateCache( @NotNull P parent ) {
    childrenCache.remove( parent );
    notifyChildrenChangedFor( parent );
  }
}
