package com.cedarsoft.tiles;

import com.cedarsoft.annotations.UiThread;
import com.cedarsoft.unit.other.px;

import javax.annotation.Nonnull;
import java.awt.geom.Point2D;

/**
 * Modifies the panning and zooming
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public interface PanAndZoomModifier {
  /**
   * Default implementation that doesn't modify anything
   */
  @Nonnull
  PanAndZoomModifier NONE = new PanAndZoomModifier() {
    @Override
    public void modifyPanning(@Nonnull TilesComponent tilesComponent, @Nonnull @View @px Point2D.Double movement, double zoomFactorX, double zoomFactorY) {
    }

    @Override
    public double[] modifyZoomFactors(@Nonnull double[] zoomFactors) {
      return zoomFactors;
    }
  };

  /**
   * Modifies the panning of the given tiles component
   */
  @UiThread
  void modifyPanning(@Nonnull TilesComponent tilesComponent, @Nonnull @View @px Point2D.Double movement, double zoomFactorX, double zoomFactorY);

  /**
   * Modifies the zoom factors.
   * Use an array with two elements (x,y) for the zoom factors for both axis
   */
  @UiThread
  double[] modifyZoomFactors(@Nonnull double[] zoomFactors);
}
