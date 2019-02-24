package com.cedarsoft.commons.javafx;

import javax.annotation.Nonnull;

import com.cedarsoft.unit.other.px;
import com.cedarsoft.unit.si.ns;

import javafx.animation.AnimationTimer;
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
    widthProperty().addListener(observable -> markAsDirty());
    heightProperty().addListener(observable -> markAsDirty());

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
          paint(getGraphicsContext2D());
          repaintRequired = false;
        }
      }
    }.start();
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

  @px
  protected double snapXValue(@px double value) {
    return FxPaintingUtils.snapPosition(value, isSnapXValuesToPixel());
  }

  public boolean isSnapYValuesToPixel() {
    return snapYValuesToPixel.get();
  }

  @Nonnull
  public BooleanProperty snapYValuesToPixelProperty() {
    return snapYValuesToPixel;
  }

  @px
  protected double snapYValue(@px double value) {
    return FxPaintingUtils.snapPosition(value, isSnapYValuesToPixel());
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
    gc.rect(0, 0, getWidth(), getHeight());
    gc.closePath();
    gc.setStroke(Color.ORANGE);
    gc.stroke();

    //Center
    gc.beginPath();
    gc.moveTo(0, snapSize(getHeight() / 2.0));
    gc.lineTo(getWidth(), snapSize(getHeight() / 2.0));
    gc.moveTo(getWidth() / 2.0, 0);
    gc.lineTo(getWidth() / 2.0, snapSize(getHeight()));

    gc.closePath();
    gc.stroke();


    gc.strokeOval(getWidth() - 5, getHeight() / 2.0 - 5, 10, 10);
  }
}
