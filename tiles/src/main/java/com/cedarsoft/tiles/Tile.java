package com.cedarsoft.tiles;

import com.cedarsoft.annotations.AnyThread;
import com.cedarsoft.annotations.UiThread;

import javax.annotation.Nonnull;
import java.awt.Graphics2D;

/**
 * Identifies a tile
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public interface Tile {
  /**
   * Draw the tile to the given graphics context
   * @param g2d the graphics context
   * @param x the x
   * @param y the y
   */
  @UiThread
  void draw(@Nonnull Graphics2D g2d, int x, int y);

  /**
   * Disposes the tile, marks it as no longer required
   */
  @AnyThread
  void dispose();
}
