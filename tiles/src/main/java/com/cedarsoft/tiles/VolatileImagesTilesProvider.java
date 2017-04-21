package com.cedarsoft.tiles;

import javax.annotation.Nonnull;

/**
 * Wraps a tiles provider and returns volatile images
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class VolatileImagesTilesProvider implements TilesProvider {
  @Nonnull
  private final TilesProvider delegate;

  public VolatileImagesTilesProvider(@Nonnull TilesProvider delegate) {
    this.delegate = delegate;
  }

  @Override
  public int getWidth() {
    return delegate.getWidth();
  }

  @Override
  public int getHeight() {
    return delegate.getHeight();
  }

  @Nonnull
  @Override
  public Tile getTile(@Nonnull TileLocation location) {
    return new NotSoVolatileTile(delegate.getTile(location));
  }

  @Nonnull
  @Override
  public Tile getPlaceholderTile() {
    return new NotSoVolatileTile(delegate.getPlaceholderTile());
  }
}
