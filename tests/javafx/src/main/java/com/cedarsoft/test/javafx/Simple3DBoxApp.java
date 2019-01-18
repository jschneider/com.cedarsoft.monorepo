package com.cedarsoft.test.javafx;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class Simple3DBoxApp extends Application {

  public Parent createContent() throws Exception {

    // Box
    Box testBox = new Box(5, 5, 5);
    //testBox.setMaterial(new PhongMaterial(Color.web("#12345650")));
    testBox.setMaterial(new PhongMaterial(Color.web("#123456")));
    testBox.setDrawMode(DrawMode.FILL);

    Box testBox2 = new Box(2, 2, 2);
    testBox2.setMaterial(createMaterial());
    testBox2.setDrawMode(DrawMode.FILL);

    // Create and position camera
    PerspectiveCamera camera = new PerspectiveCamera(true);
    camera.getTransforms().addAll(
      new Rotate(-20, Rotate.Y_AXIS),
      new Rotate(-20, Rotate.X_AXIS),
      new Translate(0, 0, -15));

    // Build the Scene Graph
    Group root = new Group();
    root.getChildren().add(camera);
    root.getChildren().add(testBox);
    root.getChildren().add(testBox2);

    // Use a SubScene
    SubScene subScene = new SubScene(root, 300, 300);
    subScene.setFill(Color.ALICEBLUE);
    subScene.setCamera(camera);
    Group group = new Group();
    group.getChildren().add(subScene);
    return group;
  }


  private Material createMaterial() {
    //return new PhongMaterial(Color.RED);

    Material mat = new PhongMaterial();
    Image diffuseMap = new Image("diffuseMap.png");
    Image normalMap = new Image("normalMap.png");

    //// Set material properties
    //mat.setDiffuseMap(diffuseMap);
    //mat.setBumpMap(normalMap);
    //mat.setSpecularColor(Color.WHITE);


    return mat;
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setResizable(false);
    Scene scene = new Scene(createContent());
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  /**
   * Java main for when running without JavaFX launcher
   */
  public static void main(String[] args) {
    launch(args);
  }
}
