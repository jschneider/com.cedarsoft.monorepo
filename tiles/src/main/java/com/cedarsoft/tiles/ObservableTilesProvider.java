package com.cedarsoft.tiles;

import com.cedarsoft.annotations.NonUiThread;
import com.cedarsoft.annotations.UiThread;

import javax.annotation.Nonnull;

/**
 * Implementations may provider informations about updated tiles
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public interface ObservableTilesProvider extends TilesProvider {
  /**
   * Adds a listener
   */
  void addListener(@Nonnull Listener listener);

  /**
   * Removes a listener
   */
  void removeListener(@Nonnull Listener listener);

  /**
   * The listener is notified whenever a tile has been updated
   */
  interface Listener {
    /**
     * Is called when the tiles have changed
     */
    @NonUiThread
    @UiThread
    void tileUpdated(@Nonnull ObservableTilesProvider source, @Model double modelXMin, @Model double modelXMax);
  }
}
