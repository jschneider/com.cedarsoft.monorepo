package com.cedarsoft.commons.javafx;

import javax.annotation.Nonnull;

import com.cedarsoft.unit.other.px;

import javafx.beans.binding.Bindings;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;

/**
 * Holds a canvas and resizes the canvas automatically
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class CanvasHolder extends Pane {
  public CanvasHolder(@Nonnull Canvas canvas) {
    this(canvas, new DefaultCalculator());
  }

  public CanvasHolder(@Nonnull Canvas canvas, @Nonnull SizeBindingType sizeBindingType) {
    this(canvas, new DefaultCalculator(), sizeBindingType);
  }

  public CanvasHolder(@Nonnull Canvas canvas, @Nonnull Calculator calculator) {
    this(canvas, calculator, SizeBindingType.BOTH);
  }

  public CanvasHolder(@Nonnull Canvas canvas, @Nonnull Calculator calculator, @Nonnull SizeBindingType sizeBindingType) {
    setMinHeight(50);
    setMinWidth(50);

    canvas.setManaged(false);

    if (sizeBindingType.isBindX()) {
      canvas.widthProperty().bind(Bindings.createDoubleBinding(() -> calculator.calculateWidth(widthProperty().get()), widthProperty()));
    }
    else {
      prefWidthProperty().bind(canvas.widthProperty());
      minWidthProperty().bind(prefWidthProperty());
      maxWidthProperty().bind(prefWidthProperty());
    }

    if (sizeBindingType.isBindY()) {
      canvas.heightProperty().bind(Bindings.createDoubleBinding(() -> calculator.calculateHeight(heightProperty().get()), heightProperty()));
    }
    else {
      prefHeightProperty().bind(canvas.heightProperty());
      minHeightProperty().bind(prefHeightProperty());
      maxHeightProperty().bind(prefHeightProperty());
    }

    getChildren().add(canvas);
  }

  @Override
  protected void layoutChildren() {
    //Layout the canvas in the middle to distribute the (optional) overscan on both sides
    assert getChildren().size() == 1;

    Canvas canvas = (Canvas) getChildren().get(0);
    double x = getWidth() / 2.0 - canvas.getWidth() / 2.0;

    canvas.resizeRelocate(x, 0, canvas.getWidth(), canvas.getHeight());
  }

  /**
   * Calculates the width and height of the canvas based upon the width/height of the parent
   */
  public interface Calculator {
    @px
    double calculateWidth(@px double holderWidth);

    @px
    double calculateHeight(@px double holderHeight);
  }

  /**
   * Default implementation that just returns the width/height of the holder
   */
  public static class DefaultCalculator implements Calculator {
    @Override
    public double calculateWidth(double holderWidth) {
      return holderWidth;
    }

    @Override
    public double calculateHeight(double holderHeight) {
      return holderHeight;
    }
  }

  /**
   * The type of the size binding
   */
  public enum SizeBindingType {
    BOTH(true, true),
    ONLY_X(true, false),
    ONLY_Y(false, true),
    NONE(false, false),
    ;

    private boolean bindX;
    private boolean bindY;

    SizeBindingType(boolean bindX, boolean bindY) {
      this.bindX = bindX;
      this.bindY = bindY;
    }

    public boolean isBindX() {
      return bindX;
    }

    public boolean isBindY() {
      return bindY;
    }
  }
}
