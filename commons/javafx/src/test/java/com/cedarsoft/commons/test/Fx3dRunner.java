package com.cedarsoft.commons.test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class Fx3dRunner extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    StackPane root = new StackPane();

    Scene scene = new Scene(root, 1920, 1080);
    primaryStage.setScene(scene);
    //scene.getStylesheets().add(getClass().getResource("JavaFxRunner.css").toExternalForm());

    Circle circle = new Circle(100, 100, 100);

    root.getChildren().add(new Button("Hallo"));

    root.setShape(circle);
    primaryStage.setScene(scene);
    primaryStage.initStyle(StageStyle.TRANSPARENT);
    scene.setFill(Color.TRANSPARENT);
    primaryStage.show();
  }
}