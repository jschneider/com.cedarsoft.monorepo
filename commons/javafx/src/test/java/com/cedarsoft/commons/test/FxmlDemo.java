package com.cedarsoft.commons.test;/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FxmlDemo extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    Parent root;
    if (true) {
      root = FXMLLoader.load(getClass().getResource("Login.fxml"));
    }
    else {
      MyCustomControlController controlController = new MyCustomControlController();
      controlController.setText("DaText");
      root = controlController;
    }

    primaryStage.setTitle("Hello World");
    primaryStage.setScene(new Scene(root, 1920, 1080));
    primaryStage.show();
  }
}
