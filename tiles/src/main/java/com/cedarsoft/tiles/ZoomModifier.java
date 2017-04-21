package com.cedarsoft.tiles;

import javax.annotation.Nonnull;

/**
 * Modifies the zoom (e.g. based upon pressed keys)
 */
public interface ZoomModifier {
  /**
   * Default implementation that does nothing
   */
  @Nonnull
  ZoomModifier NONE = new ZoomModifier() {
    @Override
    public int getZoomX(int amount, boolean controlPressed, boolean shiftPressed) {
      return amount;
    }

    @Override
    public int getZoomY(int amount, boolean controlPressed, boolean shiftPressed) {
      return amount;
    }
  };

  /**
   * Modifies x zoom factor
   */
  int getZoomX(int amount, boolean controlPressed, boolean shiftPressed);

  int getZoomY(int amount, boolean controlPressed, boolean shiftPressed);
}