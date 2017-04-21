package com.cedarsoft.tiles;

import com.cedarsoft.annotations.AnyThread;
import com.cedarsoft.annotations.NonUiThread;
import com.cedarsoft.annotations.UiThread;
import com.cedarsoft.concurrent.NewestOnlyJobsManager;
import com.google.common.collect.ImmutableSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Tiles source that uses background threads to prepare the tiles
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class MultiThreadedObservableTilesProvider extends AbstractObservableTilesProvider implements VisibleTilesAwareTilesProvider {
  @Nonnull
  private final TilesProvider delegate;

  @Nonnull
  private final NewestOnlyJobsManager jobsManager;
  @Nonnull
  private final CachingTilesProvider cachingTilesProvider;
  /**
   * The place holder tile is cached to improve performance
   */
  @Nonnull
  private final Tile placeholderTile;

  public MultiThreadedObservableTilesProvider(@Nonnull TilesProvider delegate, @Nonnull NewestOnlyJobsManager jobsManager) {
    this.delegate = delegate;
    this.jobsManager = jobsManager;
    this.cachingTilesProvider = new CachingTilesProvider(delegate);

    if (delegate instanceof ObservableTilesProvider) {
      ((ObservableTilesProvider) delegate).addListener((source, modelXMin, modelXMax) -> {
        for (TileLocation tileLocation : ImmutableSet.copyOf(cachingTilesProvider.getKeySet())) {
          if (!tileLocation.contains(modelXMin, modelXMax)) {
            continue;
          }

          if (isVisible(tileLocation)) {
            jobsManager.scheduleJob(new TileCreationJob(tileLocation, true));
          }
          else {
            //not visible, just invalidate. Will be recreated if necessary later
            cachingTilesProvider.invalidate(tileLocation);
          }
        }
      });
    }
    placeholderTile = delegate.getPlaceholderTile();
  }

  /**
   * Invalidates all tiles
   */
  @NonUiThread
  @UiThread
  public void invalidateAll() {
    for (TileLocation tileLocation : ImmutableSet.copyOf(cachingTilesProvider.getKeySet())) {
      if (isVisible(tileLocation)) {
        //Refresh because the tile is visible
        jobsManager.scheduleJob(new TileCreationJob(tileLocation, true));
      }
      else {
        //Just invalidate the tile, will be generated later if necessary
        cachingTilesProvider.invalidate(tileLocation);
      }
    }
  }

  /**
   * Holds the reference to the currently visible tiles
   */
  @Nonnull
  private final AtomicReference<VisibleTiles> visibleTiles = new AtomicReference<>(VisibleTiles.NONE);

  /**
   * Returns true if the given tile is visible, false otherwise
   */
  @AnyThread
  public boolean isVisible(@Nonnull TileLocation tileLocation) {
    return visibleTiles.get().getIdentifiers().contains(tileLocation);
  }

  @UiThread
  @Override
  public void visibleTilesChanged(@Nonnull TilesComponent source, @Nonnull VisibleTiles visibleTiles) {
    this.visibleTiles.set(visibleTiles);

    Set<? extends TileLocation> keys = getCachingTilesProvider().getKeySet();
    for (TileLocation tileLocation : keys) {
      //If the tile is no longer visible, invalidate the entry
      if (!visibleTiles.getIdentifiers().contains(tileLocation)) {
        getCachingTilesProvider().invalidate(tileLocation);
        //remove the jobs for the given tile identifier, if there are any
        jobsManager.clearJobs(tileLocation);
      }
    }
  }

  @Override
  public int getWidth() {
    return delegate.getWidth();
  }

  @Override
  public int getHeight() {
    return delegate.getHeight();
  }

  @UiThread
  @Nonnull
  @Override
  public Tile getTile(@Nonnull TileLocation location) {
    @Nullable Tile cachedTile = cachingTilesProvider.getTileFromCache(location);
    if (cachedTile != null) {
      return cachedTile;
    }

    jobsManager.scheduleJob(new TileCreationJob(location, false));
    return getPlaceholderTile();
  }

  @Override
  @Nonnull
  public Tile getPlaceholderTile() {
    return placeholderTile;
  }

  @Nonnull
  public CachingTilesProvider getCachingTilesProvider() {
    return cachingTilesProvider;
  }

  /**
   * A job that creates a tile for a given location
   */
  public class TileCreationJob implements NewestOnlyJobsManager.Job {
    @Nonnull
    private final TileLocation tileLocation;

    private final boolean refresh;

    public TileCreationJob(@Nonnull TileLocation tileLocation, boolean refresh) {
      this.tileLocation = tileLocation;
      this.refresh = refresh;
    }

    @Nonnull
    public TileLocation getTileLocation() {
      return tileLocation;
    }

    public boolean isRefresh() {
      return refresh;
    }

    @Override
    public void execute() {
      //always call refresh to be sure
      final Tile tile;
      if (isRefresh()) {
        tile = cachingTilesProvider.refreshTile(tileLocation);
      }
      else {
        tile = cachingTilesProvider.getTile(tileLocation);
      }

      notifyUpdated(0, 1); //TODO improve event
    }

    @Nonnull
    @Override
    public Object getKey() {
      /*
       * The tiles source must be part of the key since there is only one job manager for several tiles components
       */
      return Objects.hash(getTileLocation(), MultiThreadedObservableTilesProvider.this);
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null || getClass() != obj.getClass()) {
        return false;
      }
      TileCreationJob that = (TileCreationJob) obj;
      return refresh == that.refresh &&
        Objects.equals(tileLocation, that.tileLocation);
    }

    @Override
    public int hashCode() {
      return Objects.hash(tileLocation, refresh);
    }
  }
}
