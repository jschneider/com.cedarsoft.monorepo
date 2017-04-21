package com.cedarsoft.tiles;

import com.cedarsoft.annotations.NonUiThread;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.collect.ImmutableSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.Image;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Caches tiles
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class CachingTilesProvider {
  @Nonnull
  private final TilesProvider delegate;

  @Nonnull
  private final LoadingCache<TileLocation, Tile> cache;

  public CachingTilesProvider(@Nonnull TilesProvider delegate) {
    this.delegate = delegate;
    cache = CacheBuilder.<TileLocation, Image>newBuilder()
      .recordStats()
      .removalListener((RemovalListener<TileLocation, Tile>) notification -> {
        //Flush the volatile image on removal
        @Nullable Tile value = notification.getValue();
        if (value != null) {
          value.dispose();
        }
      })
      .build(new CacheLoader<TileLocation, Tile>() {
        @Override
        @NonUiThread
        public Tile load(@Nonnull TileLocation key) throws Exception {
          return delegate.getTile(key);
        }
      });
  }

  public int getWidth() {
    return delegate.getWidth();
  }

  public int getHeight() {
    return delegate.getHeight();
  }

  @Nonnull
  public Tile getTile(@Nonnull TileLocation identifier) {
    try {
      return cache.get(identifier);
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Refreshes the given tile
   * @param identifier the identifier
   * @return the refreshed tile
   */
  @Nonnull
  public Tile refreshTile(@Nonnull TileLocation identifier) {
    cache.refresh(identifier); //This call blocks until the new object has been loaded
    return getTile(identifier);
  }

  /**
   * Invalidates the tile
   *
   * @param identifier the identifier
   */
  public void invalidate(@Nonnull TileLocation identifier) {
    cache.invalidate(identifier);
  }

  public void invalidateAll() {
    cache.invalidateAll();
  }

  /**
   * Returns the tile from cache if it is there
   *
   * @param identifier the identifier
   * @return the tile or null if the tile is not cached
   */
  @Nullable
  public Tile getTileFromCache(@Nonnull TileLocation identifier) {
    return cache.getIfPresent(identifier);
  }

  @Nonnull
  public Tile getPlaceholderTile() {
    return delegate.getPlaceholderTile();
  }

  /**
   * Returns the keys contains within this cache
   */
  @Nonnull
  public Set<? extends TileLocation> getKeySet() {
    return ImmutableSet.copyOf(cache.asMap().keySet());
  }

  @Nonnull
  public LoadingCache<TileLocation, Tile> getCache() {
    return cache;
  }

  /**
   * Returns the stats
   *
   * @return the stats
   */
  @Nonnull
  public CacheStats getStats() {
    return cache.stats();
  }
}
