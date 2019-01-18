package com.cedarsoft.test.javafx;

import javafx.application.Application;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Demo3D extends Application {
  @Override
  public void start(Stage primaryStage) throws Exception {

    BorderPane root = new BorderPane();

    root.setTop(new Label("3D Test"));
    root.setCenter(new MyComp());

    Scene scene = new Scene(root, 800, 600);

    PerspectiveCamera camera = new PerspectiveCamera(true);
    scene.setCamera(camera);


    Group cameraGroup = new Group();
    cameraGroup.getChildren().add(camera);
    root.getChildren().add(cameraGroup);

    cameraGroup.setTranslateZ(-500);
    cameraGroup.setTranslateX(300);
    cameraGroup.setTranslateY(300);
    camera.setRotate(0);

    camera.setFieldOfView(150);
    camera.setFarClip(1000);

    PointLight light = new PointLight(Color.WHITE);
    light.setTranslateX(50);
    light.setTranslateY(-300);
    light.setTranslateZ(-400);
    PointLight light2 = new PointLight(Color.color(0.6, 0.3, 0.4));
    light2.setTranslateX(400);
    light2.setTranslateY(0);
    light2.setTranslateZ(-400);

    AmbientLight ambientLight = new AmbientLight(Color.color(0.2, 0.2, 0.2));
    root.getChildren().addAll( ambientLight, light, light2);

    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
