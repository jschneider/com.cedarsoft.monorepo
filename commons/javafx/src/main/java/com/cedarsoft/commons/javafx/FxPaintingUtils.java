package com.cedarsoft.commons.javafx;

import javax.annotation.Nonnull;

import com.cedarsoft.unit.other.px;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

/**
 * Helper methods for fx painting
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class FxPaintingUtils {
  private FxPaintingUtils() {
  }

  /**
   * Returnst he smaller value
   */
  @px
  public static double snapSize(@px double value) {
    return snapSize(value, true);
  }

  /**
   * Returns the *lower* value if snapToPixel is set to true
   */
  @px
  public static double snapSize(@px double value, boolean snapToPixel) {
    if (!snapToPixel) {
      return value;
    }

    return Math.ceil(value);
  }

  /**
   * Rounds the position
   */
  @px
  public static double snapPosition(@px double value) {
    return snapPosition(value, true);
  }

  /**
   * Rounds the position
   */
  @px
  public static double snapPosition(@px double value, boolean snapToPixel) {
    if (!snapToPixel) {
      return value;
    }

    return Math.round(value);
  }

  @Nonnull
  public static ImagePattern createHatch(@Nonnull Color stroke, double lineWidth, @px double width, @px double height) {
    final Canvas canvas = new Canvas(width, height);
    final GraphicsContext gc = canvas.getGraphicsContext2D();

    gc.setLineWidth(lineWidth);
    gc.setStroke(stroke);

    hatch1(gc, width, height);

    final Image patternImage = canvas.snapshot(new SnapshotParameters(), null);
    return new ImagePattern(patternImage, 0, 0, width, height, false);
  }

  @Nonnull
  public static ImagePattern createCrossed(@Nonnull Color stroke, double lineWidth, @px double width, @px double height) {
    final Canvas canvas = new Canvas(width, height);
    final GraphicsContext gc = canvas.getGraphicsContext2D();

    gc.setLineWidth(lineWidth);
    gc.setStroke(stroke);

    hatch1(gc, width, height);
    hatch2(gc, width, height);

    final Image patternImage = canvas.snapshot(new SnapshotParameters(), null);
    return new ImagePattern(patternImage, 0, 0, width, height, false);
  }

  private static void hatch1(@Nonnull GraphicsContext gc, @px double width, @px double height) {
    gc.beginPath();
    gc.moveTo(0, 0);
    gc.lineTo(width, height);
    gc.stroke();

    gc.beginPath();
    gc.moveTo(width / 2.0, -height / 2.0);
    gc.lineTo(width * 1.5, height / 2.0);
    gc.stroke();

    gc.beginPath();
    gc.moveTo(-width / 2.0, height / 2.0);
    gc.lineTo(width / 2.0, height * 1.5);
    gc.stroke();
  }

  private static void hatch2(@Nonnull GraphicsContext gc, @px double width, @px double height) {
    gc.beginPath();
    gc.moveTo(width, 0);
    gc.lineTo(0, height);
    gc.stroke();

    gc.beginPath();
    gc.moveTo(-width / 2.0, height / 2.0);
    gc.lineTo(width / 2.0, -height / 2.0);
    gc.stroke();

    gc.beginPath();
    gc.moveTo(width / 2.0, height * 1.5);
    gc.lineTo(width * 1.5, height / 2.0);
    gc.stroke();
  }
}
