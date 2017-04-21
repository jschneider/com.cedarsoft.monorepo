package com.cedarsoft.tiles;

import com.cedarsoft.annotations.AnyThread;
import com.cedarsoft.annotations.NonBlocking;
import com.cedarsoft.unit.other.px;

import javax.annotation.Nonnull;

/**
 * Provides the tiles
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public interface TilesProvider {
  /**
   * Returns the width of the tiles
   */
  @px
  @NonBlocking
  @AnyThread
  int getWidth();

  /**
   * Returns the height of the tiles
   */
  @px
  @NonBlocking
  @AnyThread
  int getHeight();

  /**
   * Returns the tile for the given location
   */
  @AnyThread
  @Nonnull
  Tile getTile(@Nonnull TileLocation location);

  /**
   * Returns the placeholder tile.
   * It is allowed to cache the placeholder tile.
   *
   * Therefore this method should return the same instance for each and every call.
   */
  @AnyThread
  @NonBlocking
  @Nonnull
  Tile getPlaceholderTile();
}
