package com.cedarsoft.tiles;

import com.cedarsoft.annotations.UiThread;
import com.cedarsoft.unit.other.px;

import javax.annotation.Nonnull;
import java.awt.geom.Point2D;

/**
 * Converts values
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class Converter {
  @Nonnull
  private final TilesComponent tilesComponent;

  public Converter(@Nonnull TilesComponent tilesComponent) {
    this.tilesComponent = tilesComponent;
  }

  @VisibleArea
  @px
  public double model2VisibleAreaX(@Model double x) {
    return view2VisibleAreaX(model2ViewX(x));
  }

  @VisibleArea
  @px
  public double model2VisibleAreaY(@Model @px double y) {
    return view2VisibleAreaY(model2ViewY(y));
  }

  @Nonnull
  @Model
  @UiThread
  public Point2D.Double visibleArea2Model(@VisibleArea @px Point2D.Double point) {
    return visibleArea2Model(point.x, point.y);
  }

  @Nonnull
  @Model
  @UiThread
  public Point2D.Double visibleArea2Model(@VisibleArea @px double x, @VisibleArea @px double y) {
    return new Point2D.Double(visibleArea2ModelX(x), visibleArea2ModelY(y));
  }

  @Model
  public double visibleArea2ModelX(@VisibleArea @px double x) {
    return view2ModelX(visibleArea2ViewX(x));
  }

  @Model
  public double visibleArea2ModelY(@VisibleArea @px double y) {
    return view2ModelY(visibleArea2ViewY(y));
  }

  @Nonnull
  @View
  @px
  @UiThread
  public Point2D.Double visibleArea2View(@VisibleArea @px double x, @VisibleArea @px double y) {
    return new Point2D.Double(visibleArea2ViewX(x), visibleArea2ViewY(y));
  }

  @VisibleArea
  @px
  public double visibleArea2ViewX(@View @px double x) {
    return x - tilesComponent.getViewMovement().x;
  }

  @VisibleArea
  @px
  public double visibleArea2ViewY(@View @px double y) {
    return y - tilesComponent.getViewMovement().y;
  }

  @VisibleArea
  @px
  public double view2VisibleAreaX(@View @px double x) {
    return x + tilesComponent.getViewMovement().x;
  }

  @VisibleArea
  @px
  public double view2VisibleAreaY(@View @px double y) {
    return y + tilesComponent.getViewMovement().y;
  }

  @px
  @View
  @UiThread
  public double model2ViewX(@Model double x) {
    return model2ViewX(x, tilesComponent.getZoomFactorX());
  }

  @px
  @View
  @UiThread
  public double model2ViewY(@Model double y) {
    return model2ViewY(y, tilesComponent.getZoomFactorY());
  }

  @Model
  public double view2ModelX(@View @px double x) {
    return view2ModelX(x, tilesComponent.getZoomFactorX());
  }

  @Model
  public double view2ModelY(@View @px double y) {
    return view2ModelY(y, tilesComponent.getZoomFactorY());
  }

  @View
  @px
  public static double model2ViewX(@Model double x, double zoomFactorX) {
    return x * zoomFactorX;
  }

  @View
  @px
  public static double model2ViewY(@Model double y, double zoomFactorY) {
    return y * zoomFactorY;
  }

  @Model
  public static double view2ModelX(@View @px double x, double zoomFactor) {
    return x / zoomFactor;
  }

  @Model
  public static double view2ModelY(@View @px double y, double zoomFactor) {
    return y / zoomFactor;
  }
}
