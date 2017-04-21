package com.cedarsoft.tiles;

import com.cedarsoft.annotations.UiThread;

import javax.annotation.Nonnull;

/**
 * Is notified about tiles that become visible/invisible
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public interface VisibleTilesAwareTilesProvider extends TilesProvider {
  /**
   * Is called if tiles become visible/invisible
   */
  @UiThread
  void visibleTilesChanged(@Nonnull TilesComponent source, @Nonnull VisibleTiles visibleTiles);
}
