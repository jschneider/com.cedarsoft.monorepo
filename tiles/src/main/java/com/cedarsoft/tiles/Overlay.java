package com.cedarsoft.tiles;

import com.cedarsoft.annotations.UiThread;

import javax.annotation.Nonnull;
import java.awt.Graphics2D;

/**
 * An overlay is painted above or below the tiles
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public interface Overlay {
  /**
   * Implementations may paint on the graphics context of the given tiles component
   */
  @UiThread
  void paint(@Nonnull Graphics2D g2d, @Nonnull TilesComponent tilesComponent);
}
