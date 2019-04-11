package com.cedarsoft.commons.javafx;

import javax.annotation.Nonnull;

import com.cedarsoft.unit.other.px;
import com.cedarsoft.unit.si.ns;

import javafx.animation.AnimationTimer;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Abstract base class for a canvas that is automatically repainted if necessary<p/>
 * Subclasses must call {@link #paint(GraphicsContext)} whenever the model is updated
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public abstract class AbstractCanvas extends Canvas {
  /**
   * Is set to true if a repaint is required
   */
  protected boolean repaintRequired;

  protected AbstractCanvas() {
    //Automatically mark as dirty on resize
    registerDirtyListener(widthProperty());
    registerDirtyListener(heightProperty());

    //Repaint when snap has changed
    registerDirtyListener(snapYValuesToPixel);
    registerDirtyListener(snapXValuesToPixel);


    new AnimationTimer() {
      @Override
      public void handle(@ns long now) {
        if (getScene() == null) {
          return;
        }
        if (!isVisible()) {
          return;
        }
        if (repaintRequired) {
          repaintRequired = false;
          paint(getGraphicsContext2D());
        }
      }
    }.start();
  }

  public final void registerDirtyListener(@Nonnull Observable property) {
    registerDirtyListener(this, property);
  }

  /**
   * Marks the canvas as dirty. It will be repainted on the next occasion
   */
  public void markAsDirty() {
    repaintRequired = true;
  }

  /**
   * Clears the background in a way that avoids memory leaks.
   */
  protected void clearBackground(@Nonnull GraphicsContext gc) {
    // Workaround to avoid a memory leak
    gc.clearRect(-1000, -1000, getWidth() + 2000, getHeight() + 2000);
  }

  /**
   * Paint the content.
   * <p>
   * Implementations probably want to call {@link #clearBackground(GraphicsContext)} first
   */
  protected abstract void paint(@Nonnull GraphicsContext gc);


  /**
   * If set to true the x values are snapped to pixel.
   * Should be set to false while the x axis is animated
   */
  @Nonnull
  private final BooleanProperty snapXValuesToPixel = new SimpleBooleanProperty(true);

  /**
   * Should be disabled when the y axis is animated
   */
  @Nonnull
  private final BooleanProperty snapYValuesToPixel = new SimpleBooleanProperty(true);


  public boolean isSnapXValuesToPixel() {
    return snapXValuesToPixel.get();
  }

  @Nonnull
  public BooleanProperty snapXValuesToPixelProperty() {
    return snapXValuesToPixel;
  }

  public boolean isSnapYValuesToPixel() {
    return snapYValuesToPixel.get();
  }

  @Nonnull
  public BooleanProperty snapYValuesToPixelProperty() {
    return snapYValuesToPixel;
  }

  @px
  protected double snapXValue(@px double value) {
    return FxPaintingUtils.snapPosition(value, isSnapXValuesToPixel());
  }

  @px
  protected double snapXSize(@px double value) {
    return FxPaintingUtils.snapSize(value, isSnapXValuesToPixel());
  }

  @px
  protected double snapYValue(@px double value) {
    return FxPaintingUtils.snapPosition(value, isSnapYValuesToPixel());
  }

  @px
  protected double snapYSize(@px double value) {
    return FxPaintingUtils.snapSize(value, isSnapYValuesToPixel());
  }

  @Deprecated
  @px
  protected static double snapSize(@px double value) {
    return FxPaintingUtils.snapSize(value, false);
  }

  @Deprecated
  @px
  protected static double snapPosition(@px double value) {
    return FxPaintingUtils.snapPosition(value);
  }

  /**
   * Can be used to enable debug output
   */
  protected static final boolean DEBUG_ENABLED = false;

  protected void paintDebugInfo(@Nonnull GraphicsContext gc) {
    //Around
    gc.beginPath();
    gc.rect(snapXValue(0), snapYValue(0), snapXSize(getWidth()), snapYSize(getHeight()));
    gc.closePath();
    gc.setStroke(Color.ORANGE);
    gc.setLineWidth(3);
    gc.stroke();

    //cross wire to mark the center
    gc.beginPath();
    gc.moveTo(snapXValue(0), snapYValue(getHeight() / 2.0));
    gc.lineTo(snapXValue(getWidth()), snapYValue(getHeight() / 2.0));
    gc.moveTo(snapXValue(getWidth() / 2.0), 0);
    gc.lineTo(snapXValue(getWidth() / 2.0), snapYValue(getHeight()));

    gc.closePath();
    gc.setLineWidth(2);
    gc.stroke();

    //oval in the corners
    //Bottom right
    gc.strokeOval(snapXValue(getWidth() - 10), snapYValue(getHeight() - 10), snapXSize(20), snapYSize(20));
    //Top right
    gc.strokeOval(snapXValue(getWidth() - 10), snapYValue(0 - 10), snapXSize(20), snapYSize(20));
    //Bottom left
    gc.strokeOval(snapXValue(0 - 10), snapYValue(getHeight() - 10), snapXSize(20), snapYSize(20));
    //top left
    gc.strokeOval(snapXValue(0 - 10), snapYValue(0 - 10), snapXSize(20), snapYSize(20));
  }

  public static void registerDirtyListener(@Nonnull AbstractCanvas canvas, @Nonnull Observable property) {
    property.addListener(observable -> canvas.markAsDirty());
  }
}
