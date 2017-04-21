package com.cedarsoft.tiles;

import com.cedarsoft.unit.other.px;

import java.util.Objects;

/**
 * Location of a tile
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class TileLocation {
  @TileIndex
  private final int x;
  @TileIndex
  private final int y;

  private final double zoomFactorX;
  private final double zoomFactorY;

  @px
  private final int width;
  @px
  private final int height;

  public TileLocation(@TileIndex int x, @TileIndex int y, double zoomFactorX, double zoomFactorY, @px int width, @px int height) {
    this.x = x;
    this.y = y;
    this.zoomFactorY = zoomFactorY;
    this.zoomFactorX = zoomFactorX;
    this.width = width;
    this.height = height;
  }

  @TileIndex
  public int getX() {
    return x;
  }

  @TileIndex
  public int getY() {
    return y;
  }

  public double getZoomFactorX() {
    return zoomFactorX;
  }

  public double getZoomFactorY() {
    return zoomFactorY;
  }

  @px
  public int getWidth() {
    return width;
  }

  @px
  public int getHeight() {
    return height;
  }

  @px
  @View
  public double getMinX() {
    return x * (long) width;
  }

  @px
  @View
  public double getMinY() {
    return y * (long) height;
  }

  @px
  @View
  public double getMaxX() {
    return x * (long) width + width;
  }

  @px
  @View
  public double getMaxY() {
    return y * (long) height + height;
  }

  @Model
  public double getModelMinX() {
    return Converter.view2ModelX(getMinX(), zoomFactorX);
  }

  @Model
  public double getModelMinY() {
    return Converter.view2ModelY(getMinY(), zoomFactorX);
  }

  @Model
  public double getModelMaxX() {
    return Converter.view2ModelX(getMaxX(), zoomFactorX);
  }

  @Model
  public double getModelMaxY() {
    return Converter.view2ModelY(getMaxY(), zoomFactorX);
  }

  /**
   * Returns true if the tile on this location contains any of the given model values range
   */
  public boolean contains(@Model double modelXMin, @Model double modelXMax) {
    return modelXMin <= getModelMaxX() && modelXMax >= getModelMinX();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    TileLocation that = (TileLocation) obj;
    return x == that.x &&
      y == that.y &&
      Double.compare(that.zoomFactorX, zoomFactorX) == 0 &&
      Double.compare(that.zoomFactorY, zoomFactorY) == 0 &&
      width == that.width &&
      height == that.height;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y, zoomFactorX, zoomFactorY, width, height);
  }

  @Override
  public String toString() {
    return "TileLocation{" +
      "x=" + x +
      ", y=" + y +
      ", zoomFactorX=" + zoomFactorX +
      ", zoomFactorY=" + zoomFactorY +
      ", width=" + width +
      ", height=" + height +
      '}';
  }
}
