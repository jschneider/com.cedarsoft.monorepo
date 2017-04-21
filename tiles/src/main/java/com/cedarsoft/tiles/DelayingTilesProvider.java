package com.cedarsoft.tiles;

import com.cedarsoft.annotations.NonUiThread;
import com.cedarsoft.unit.si.ms;

import javax.annotation.Nonnull;

/**
 * Sample implementation that delays the creation of the tiles for a given time
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class DelayingTilesProvider implements TilesProvider {
  @Nonnull
  private final TilesProvider delegate;
  @ms
  private final int delay;

  public DelayingTilesProvider(@Nonnull TilesProvider delegate, int delay) {
    this.delegate = delegate;
    this.delay = delay;
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
  @NonUiThread
  public Tile getTile(@Nonnull TileLocation location) {
    try {
      Thread.sleep(delay);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    return delegate.getTile(location);
  }

  @Nonnull
  @Override
  public Tile getPlaceholderTile() {
    return delegate.getPlaceholderTile();
  }
}
