package com.cedarsoft.tiles;

import com.cedarsoft.unit.other.px;

import javax.annotation.Nonnull;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * This class holds all relevant information that is required to paint
 * tiles on the screen
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class TileTranslation {

  @px
  private final long minXZoomed;
  @px
  private final long maxXZoomed;
  @px
  private final long minYZoomed;
  @px
  private final long maxYZoomed;
  @Model
  private final double minX;
  @Model
  private final double maxX;
  @Model
  private final double minY;
  @Model
  private final double maxY;

  @TileIndex
  private final int tileX;
  @TileIndex
  private final int tileY;

  private final double zoomFactorX;
  private final double zoomFactorY;

  @px
  private final long tileWidth;
  @px
  private final long tileHeight;

  public TileTranslation(@TileIndex int tileX, @TileIndex int tileY, double zoomFactorX, double zoomFactorY, @px int tileWidth, @px int tileHeight) {
    this.tileX = tileX;
    this.tileY = tileY;
    this.zoomFactorX = zoomFactorX;
    this.zoomFactorY = zoomFactorY;
    this.tileWidth = tileWidth;
    this.tileHeight = tileHeight;
    minXZoomed = (long) tileX * tileWidth;
    maxXZoomed = ((long) tileX + 1) * tileWidth;
    minYZoomed = (long) tileY * tileHeight;
    maxYZoomed = ((long) tileY + 1) * tileHeight;

    minX = minXZoomed / zoomFactorX;
    maxX = maxXZoomed / zoomFactorX;
    minY = minYZoomed / zoomFactorY;
    maxY = maxYZoomed / zoomFactorY;
  }

  @px
  public double model2tileX(@Model double modelValue) {
    @Model double modelRelative = modelValue - minX;
    return modelRelative * zoomFactorX;
  }

  @px
  public double model2tileY(@Model double modelValue) {
    @Model double modelRelative = modelValue - minY;
    return modelRelative * zoomFactorY;
  }

  @Model
  public double tile2modelX(@px double x) {
    return x / zoomFactorX + minX;
  }

  @Model
  public double tile2modelY(@px double y) {
    return y / zoomFactorY + minY;
  }

  @px
  public long getMinXZoomed() {
    return minXZoomed;
  }

  @px
  public long getMaxXZoomed() {
    return maxXZoomed;
  }

  @px
  public long getMinYZoomed() {
    return minYZoomed;
  }

  @px
  public long getMaxYZoomed() {
    return maxYZoomed;
  }

  @Model
  public double getMinXModel() {
    return minX;
  }

  @Model
  public double getMaxXModel() {
    return maxX;
  }

  @Model
  public double getMinYModel() {
    return minY;
  }

  @Model
  public double getMaxYModel() {
    return maxY;
  }

  public boolean isInModelYRange(@Model double modelY) {
    return getMinYModel() <= modelY && getMaxYModel() >= modelY;
  }

  public boolean isBelowModelYRange(@Model double modelY) {
    return getMinYModel() > modelY;
  }

  public boolean isAboveModelYRange(@Model double modelY) {
    return getMaxYModel() < modelY;
  }

  public boolean isInModelXRange(@Model double modelX) {
    return getMinXModel() <= modelX && getMaxXModel() >= modelX;
  }

  @Model
  @Nonnull
  public Point2D.Double getCenterModel() {
    return new Point2D.Double(getCenterXModel(), getCenterYModel());
  }

  @Model
  public double getCenterYModel() {
    return getMinYModel() + (getMaxYModel() - getMinYModel()) / 2.0;
  }

  @Model
  public double getCenterXModel() {
    return getMinXModel() + (getMaxXModel() - getMinXModel()) / 2.0;
  }

  @px
  public long getTileWidth() {
    return tileWidth;
  }

  @px
  public long getTileHeight() {
    return tileHeight;
  }

  @TileIndex
  public int getTileX() {
    return tileX;
  }

  @TileIndex
  public int getTileY() {
    return tileY;
  }

  public double getZoomFactorX() {
    return zoomFactorX;
  }

  public double getZoomFactorY() {
    return zoomFactorY;
  }

  /**
   * ATTENTION: Modifies the given line
   */
  @px
  @Nonnull
  public Line2D.Double transformToTile(@Model Line2D.Double line) {
    line.x1 = model2tileX(line.x1);
    line.x2 = model2tileX(line.x2);
    line.y1 = model2tileY(line.y1);
    line.y2 = model2tileY(line.y2);
    return line;
  }

  @Override
  public String toString() {
    return "TileTranslation{" +
      "tileX=" + tileX +
      ", tileY=" + tileY +
      '}';
  }
}
