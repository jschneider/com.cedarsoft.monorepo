package com.cedarsoft.commons.javafx;

import javax.annotation.Nonnull;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class DragButton extends Pane {
  @Nonnull
  private final Rectangle rectangle;
  @Nonnull
  private final Label label;
  @Nonnull
  private final Line line;

  public DragButton(@Nonnull String text) {
    setCursor(Cursor.W_RESIZE);

    rectangle = new Rectangle();
    rectangle.setFill(Color.GRAY);
    rectangle.setArcHeight(5.0);
    rectangle.setArcWidth(5.0);

    line = new Line();

    label = Components.label(text);
    label.setTextFill(Color.WHITE);

    getChildren().addAll(rectangle, line, label);
  }

  @SuppressWarnings("MethodDoesntCallSuperMethod")
  @Override
  protected void layoutChildren() {
    double width = getWidth();
    double height = getHeight();

    double labelPrefWidth = label.prefWidth(height);
    double labelPrefHeight = label.prefHeight(width);

    rectangle.setX(0);
    rectangle.setY(0);
    rectangle.setWidth(labelPrefWidth + 10);
    rectangle.setHeight(labelPrefHeight + 6);

    label.resizeRelocate(5, 3, labelPrefWidth, labelPrefHeight);

    line.setStartX(width / 2.0);
    line.setEndX(line.getStartX());
    line.setStartY(rectangle.getHeight());
    line.setEndY(height);
  }

  @Override
  protected double computeMinWidth(double height) {
    return computePrefWidth(height);
  }

  @Override
  protected double computeMinHeight(double width) {
    return computePrefHeight(width);
  }

  @Override
  protected double computeMaxWidth(double height) {
    return computePrefWidth(height);
  }

  @Override
  protected double computeMaxHeight(double width) {
    return computePrefHeight(width);
  }

  @SuppressWarnings("MethodDoesntCallSuperMethod")
  @Override
  protected double computePrefWidth(double height) {
    return label.prefWidth(height) + 10;
  }

  @SuppressWarnings("MethodDoesntCallSuperMethod")
  @Override
  protected double computePrefHeight(double width) {
    return label.prefHeight(width) + 6;
  }
}
