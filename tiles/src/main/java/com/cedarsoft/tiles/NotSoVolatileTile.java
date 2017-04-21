package com.cedarsoft.tiles;


import com.cedarsoft.annotations.AnyThread;
import com.cedarsoft.swing.common.NotSoVolatileImage;

import javax.annotation.Nonnull;

/**
 * Tile based upon NotSoVolatileImage
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class NotSoVolatileTile extends NotSoVolatileImage implements Tile {
  public NotSoVolatileTile(@Nonnull Tile tile) {
    this((ImageTile) tile);
  }

  public NotSoVolatileTile(@Nonnull ImageTile tile) {
    super(tile.getImage());
  }

  @AnyThread
  @Override
  public void dispose() {
    flush();
  }
}
