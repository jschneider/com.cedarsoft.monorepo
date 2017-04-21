package com.cedarsoft.tiles;

import com.cedarsoft.unit.other.px;

import javax.annotation.Nonnull;
import java.awt.geom.Point2D;

/**
 * Default implementation that ensures min/max values
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class DefaultPanAndZoomModifier implements PanAndZoomModifier {
  @px
  @Nonnull
  private final Point2D.Double min;
  @px
  @Nonnull
  private final Point2D.Double max;

  public DefaultPanAndZoomModifier(@Nonnull @px Point2D.Double min, @Nonnull @px Point2D.Double max) {
    this.min = min;
    this.max = max;
  }

  @Override
  public void modifyPanning(@Nonnull TilesComponent tilesComponent, @Nonnull @px Point2D.Double movement, double zoomFactorX, double zoomFactorY) {
    movement.x = Math.max(min.x, Math.min(movement.x, max.x));
    movement.y = Math.max(min.y, Math.min(movement.y, max.y));
  }

  @Override
  public double[] modifyZoomFactors(@Nonnull double[] zoomFactors) {
    return zoomFactors;
  }

  @Nonnull
  @px
  public Point2D.Double getMin() {
    return min;
  }

  @Nonnull
  @px
  public Point2D.Double getMax() {
    return max;
  }
}
