package com.cedarsoft.commons.javafx;

import java.util.concurrent.ThreadLocalRandom;

import org.tbee.javafx.scene.layout.MigPane;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ProgressIndicatorBarDemo extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    MigPane root = new MigPane("", "[grow, fill]", "[grow, fill]");

    ProgressIndicatorBar progressIndicatorBar1 = new ProgressIndicatorBar(20, "%.1f", "mA");
    root.add(progressIndicatorBar1, "alignx center, aligny center, wrap");

    ProgressIndicatorBar progressIndicatorBar2 = new ProgressIndicatorBar(100, "%.0f", "%");
    root.add(progressIndicatorBar2, "alignx center, aligny center, wrap");

    ProgressIndicatorBar progressIndicatorBar3 = new ProgressIndicatorBar(-1, "%.0f", "%");
    root.add(progressIndicatorBar3, "alignx center, aligny center");

    Timeline timeLine = new Timeline(new KeyFrame(Duration.millis(50), event -> {
      double workDone = progressIndicatorBar1.getWorkDone();
      if (workDone < 20.0) {
        progressIndicatorBar1.workDoneProperty().setValue(workDone + 0.1);
      }
      else {
        progressIndicatorBar1.workDoneProperty().setValue(4.0);
      }

      progressIndicatorBar2.workDoneProperty().setValue(ThreadLocalRandom.current().nextInt(0, 100 + 1));
    }));

    timeLine.setCycleCount(Animation.INDEFINITE);
    timeLine.play();

    primaryStage.setScene(new Scene(root, 200, 100));
    primaryStage.show();
  }
}
