package com.cedarsoft.commons.javafx;

import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.cedarsoft.annotations.UiThread;
import com.cedarsoft.unit.other.px;

import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 * Displays a pane with two buttons, that can be dragged and
 * are bound to IntegerProperties
 */
public class DragButtonsPane extends Pane {
  @Nonnull
  private final DragButton button1;
  @Nonnull
  private final DragButton button2;
  @Nonnull
  private final Supplier<Integer> maxXSupplier;
  @Nonnull
  private final IntegerProperty button1Property;
  @Nonnull
  private final IntegerProperty button2Property;

  private final boolean useLimits;
  @Nullable
  private final IntegerProperty minLimitProperty;
  @Nullable
  private final IntegerProperty maxLimitProperty;

  public DragButtonsPane(@Nonnull Supplier<Integer> maxXSupplier,
                         @Nonnull IntegerProperty button1Property,
                         @Nonnull IntegerProperty button2Property,
                         @Nonnull ObservableValue<Boolean> button1ActivatedBinding,
                         @Nonnull ObservableValue<Boolean> button2ActivatedBinding,
                         @Nonnull String buttonLabel1,
                         @Nonnull String buttonLabel2,
                         boolean useLimits,
                         @Nullable IntegerProperty minLimitProperty,
                         @Nullable IntegerProperty maxLimitProperty) {

    this.maxXSupplier = maxXSupplier;
    this.button1Property = button1Property;
    this.button2Property = button2Property;
    this.useLimits = useLimits;
    this.minLimitProperty = minLimitProperty;
    this.maxLimitProperty = maxLimitProperty;

    button1Property.addListener((observable, oldValue, newValue) -> {
      layoutButtons();
    });

    button2Property.addListener((observable, oldValue, newValue) -> {
      layoutButtons();
    });

    button1 = new DragButton(buttonLabel1);
    button1.setManaged(false);
    button1.setOnMouseDragged(this::onButton1Dragged);
    button1.setOnMouseReleased(event -> onButtonDragFinished());

    button2 = new DragButton(buttonLabel2);
    button2.setManaged(false);
    button2.setOnMouseDragged(this::onButton2Dragged);
    button2.setOnMouseReleased(event -> onButtonDragFinished());

    getChildren().addAll(button1, button2);

    button1.visibleProperty().bind(button1ActivatedBinding);
    button2.visibleProperty().bind(button2ActivatedBinding);

    widthProperty().addListener((observable, oldValue, newValue) -> layoutButtons());
    heightProperty().addListener((observable, oldValue, newValue) -> layoutButtons());
  }

  @UiThread
  private void layoutButtons() {
    final double width = getWidth();
    final double height = getHeight();
    final double buttonWidth = Math.max(button1.prefWidth(height), button2.prefWidth(height));

    @px double buttonY = 5;

    double model2xFactor = width / maxXSupplier.get();

    @px double button1X = (button1Property.get() * model2xFactor) - (buttonWidth * 0.5);
    button1.resizeRelocate(button1X, buttonY, buttonWidth, height - buttonY);
    buttonY += 20;

    @px double button2X = (button2Property.get() * model2xFactor) - (buttonWidth * 0.5);
    button2.resizeRelocate(button2X, buttonY, buttonWidth, height - buttonY);
  }

  @UiThread
  private void onButtonDragFinished() {
    if (button1.isVisible()) {
      final int modelValue = computeModelValue(button1);
      button1Property.set(modelValue);
    }
    if (button2.isVisible()) {
      final int modelValue = computeModelValue(button2);
      button2Property.set(modelValue);
    }
  }

  @UiThread
  private int computeModelValue(@Nonnull DragButton dragButton) {
    final double x2ModelFactor = maxXSupplier.get() / getWidth();
    int modelValue = (int) Math.round((dragButton.getLayoutX() + dragButton.getWidth() * 0.5) * x2ModelFactor);

    if (useLimits && minLimitProperty != null && maxLimitProperty != null) {
      if (modelValue < minLimitProperty.get()) {
        modelValue = minLimitProperty.get();
      }
      if (modelValue > maxLimitProperty.get()) {
        modelValue = maxLimitProperty.get();
      }
    }

    return modelValue;
  }

  @UiThread
  private void onButton1Dragged(@Nonnull MouseEvent event) {
    button1.setLayoutX(computeLayoutX(button1, event));
  }

  @UiThread
  private void onButton2Dragged(@Nonnull MouseEvent event) {
    button2.setLayoutX(computeLayoutX(button2, event));
  }

  @UiThread
  private double computeLayoutX(@Nonnull DragButton forButton, @Nonnull MouseEvent event) {
    double eventX = forButton.localToParent(event.getX(), 0).getX();
    double minX = -forButton.getWidth() / 2;
    if (eventX < minX) {
      eventX = minX;
    }
    double maxX = minX + getWidth();
    if (eventX > maxX) {
      eventX = maxX;
    }
    return eventX;
  }
}
