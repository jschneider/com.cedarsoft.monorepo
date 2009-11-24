package com.cedarsoft.hierarchy;

import com.cedarsoft.cache.Cache;
import com.cedarsoft.cache.HashedCache;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.WeakHashMap;

/**
 * Special implementation of a child detector that always returns the same list for a given parent.
 *
 * @param <C> the type of the children
 * @param <P> the type of the parent
 */
public abstract class CachingChildDetector<P, C> extends AbstractChildDetector<P, C> {
  @NotNull
  private final Cache<P, List<? extends C>> childrenCache = new HashedCache<P, List<? extends C>>( new WeakHashMap<P, List<? extends C>>(), new Cache.Factory<P, List<? extends C>>() {
    @Override
    @NotNull
    public List<? extends C> create( @NotNull P key ) {
      return createChildren( key );
    }
  } );

  @NotNull
  protected abstract List<? extends C> createChildren( @NotNull P parent );

  @Override
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
