package com.cedarsoft.tiles;

import com.cedarsoft.unit.si.ms;
import com.cedarsoft.unit.si.ns;

import javax.annotation.Nonnull;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class BenchmarkingTilesProvider implements TilesProvider {
  @Nonnull
  private final TilesProvider delegate;

  public BenchmarkingTilesProvider(@Nonnull TilesProvider delegate) {
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
    @ns long start = System.nanoTime();
    Tile tile = delegate.getTile(location);
    @ns long finished = System.nanoTime();
    @ms double ms = (finished - start) / 1000.0 / 1000.0;

    if (ms > 2) {
      System.out.println(location.getX() + "/" + location.getY() + "took " + (finished - start) / 1000.0 / 1000.0 + " ms");
    }

    return tile;
  }

  @Nonnull
  @Override
  public Tile getPlaceholderTile() {
    return delegate.getPlaceholderTile();
  }
}
