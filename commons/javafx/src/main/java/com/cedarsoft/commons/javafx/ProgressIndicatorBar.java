package com.cedarsoft.commons.javafx;

import javax.annotation.Nonnull;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


/**
 * Displays a progress bar with a text indicator inside
 */
public final class ProgressIndicatorBar extends StackPane {

  private final double totalWork;
  @Nonnull
  private final DoubleProperty workDone = new SimpleDoubleProperty();
  @Nonnull
  private final String labelFormatSpecifier;
  @Nonnull
  private final String unitLabel;

  @Nonnull
  private final ProgressBar progressBar = new ProgressBar();
  @Nonnull
  private final Text text = new Text();

  private static final int DEFAULT_LABEL_PADDING = 2;

  public ProgressIndicatorBar(double maxValue, @Nonnull String labelFormatSpecifier, @Nonnull String unitLabel) {
    this.totalWork = maxValue;
    this.labelFormatSpecifier = labelFormatSpecifier;
    this.unitLabel = unitLabel;
    progressBar.setMaxWidth(Double.MAX_VALUE);
    text.setFill(Color.BLACK);
    text.setFont(Font.font(text.getFont().getFamily(), FontWeight.NORMAL, 14.0));

    syncProgress();

    workDone.addListener((observable, oldValue, newValue) -> syncProgress());

    getChildren().setAll(progressBar, text);
  }

  public double getWorkDone() {
    return workDone.get();
  }

  @Nonnull
  public DoubleProperty workDoneProperty() {
    return workDone;
  }

  // synchronizes the progress indicated with the work done.
  private void syncProgress() {
    if (totalWork < 0) {
      text.setText("");
      progressBar.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
    } else {
      text.setText(String.format(labelFormatSpecifier, workDone.get()) + " " + unitLabel);
      progressBar.setProgress(workDone.get() / totalWork);
    }

    progressBar.setMinHeight(text.getBoundsInLocal().getHeight() + DEFAULT_LABEL_PADDING * 2);
    progressBar.setMinWidth (text.getBoundsInLocal().getWidth()  + DEFAULT_LABEL_PADDING * 2);
  }
}
