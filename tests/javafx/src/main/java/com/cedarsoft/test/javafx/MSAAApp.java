package com.cedarsoft.test.javafx;

import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MSAAApp extends Application {

  @Override
  public void start(Stage stage) {
    if (!Platform.isSupported(ConditionalFeature.SCENE3D)) {
      throw new RuntimeException("*** ERROR: common conditional SCENE3D is not supported");
    }

    stage.setTitle("JavaFX MSAA demo");

    Group root = new Group();
    Scene scene = new Scene(root, 1000, 800);
    scene.setFill(Color.color(0.2, 0.2, 0.2, 1.0));

    HBox hbox = new HBox();
    hbox.setLayoutX(75);
    hbox.setLayoutY(200);

    PhongMaterial phongMaterial = new PhongMaterial(Color.color(1.0, 0.7, 0.8));
    Cylinder cylinder1 = new Cylinder(100, 200);
    cylinder1.setMaterial(phongMaterial);
    SubScene noMsaa = createSubScene("MSAA = false", cylinder1,
                                     Color.TRANSPARENT,
                                     new PerspectiveCamera(), false);
    hbox.getChildren().add(noMsaa);

    Shape3D meshView = createMesh();
    meshView.setMaterial(phongMaterial);

    //SubScene msaa = createSubScene("MSAA = true", cylinder2, Color.TRANSPARENT, new PerspectiveCamera(), true);
    SubScene msaa = createSubScene("MSAA = true", meshView, Color.TRANSPARENT, new PerspectiveCamera(), true);
    hbox.getChildren().add(msaa);


    Slider slider = new Slider(0, 360, 0);
    slider.setBlockIncrement(1);
    slider.setTranslateX(425);
    slider.setTranslateY(625);
    cylinder1.rotateProperty().bind(slider.valueProperty());
    meshView.rotateProperty().bind(slider.valueProperty());
    root.getChildren().addAll(hbox, slider);

    stage.setScene(scene);
    stage.show();
  }

  private Shape3D createMesh() {
    if (false) {
      Cylinder cylinder2 = new Cylinder(100, 200);
      return cylinder2;
    }

    TriangleMesh pyramidMesh = new TriangleMesh();
    pyramidMesh.getTexCoords().addAll(0, 0);


    float h = 150;                    // Height
    float s = 300;                    // Side
    pyramidMesh.getPoints().addAll(
      0, 0, 0,            // Point 0 - Top
      0, h, -s / 2,         // Point 1 - Front
      -s / 2, h, 0,            // Point 2 - Left
      s / 2, h, 0,            // Point 3 - Back
      0, h, s / 2           // Point 4 - Right
    );

    pyramidMesh.getFaces().addAll(
      0, 0, 2, 0, 1, 0,          // Front left face
      0, 0, 1, 0, 3, 0,          // Front right face
      0, 0, 3, 0, 4, 0,          // Back right face
      0, 0, 4, 0, 2, 0,          // Back left face
      4, 0, 1, 0, 2, 0,          // Bottom rear face
      4, 0, 3, 0, 1, 0           // Bottom front face
    );

    MeshView pyramid = new MeshView(pyramidMesh);
    pyramid.setDrawMode(DrawMode.FILL);
    //pyramid.setTranslateX(200);
    //pyramid.setTranslateY(100);
    //pyramid.setTranslateZ(200);

    return pyramid;
  }

  private static Parent setTitle(String str) {
    final VBox vbox = new VBox();
    final Text text = new Text(str);
    text.setFont(Font.font("Times New Roman", 24));
    text.setFill(Color.WHEAT);
    vbox.getChildren().add(text);
    return vbox;
  }

  private static SubScene createSubScene(String title, Node node,
                                         Paint fillPaint, Camera camera, boolean msaa) {
    Group root = new Group();

    PointLight light = new PointLight(Color.WHITE);
    light.setTranslateX(50);
    light.setTranslateY(-300);
    light.setTranslateZ(-400);
    PointLight light2 = new PointLight(Color.color(0.6, 0.3, 0.4));
    light2.setTranslateX(400);
    light2.setTranslateY(0);
    light2.setTranslateZ(-400);

    AmbientLight ambientLight = new AmbientLight(Color.color(0.2, 0.2, 0.2));
    node.setRotationAxis(new Point3D(2, 1, 0).normalize());
    node.setTranslateX(180);
    node.setTranslateY(180);
    root.getChildren().addAll(setTitle(title), ambientLight,
                              light, light2, node);

    SubScene subScene = new SubScene(root, 500, 400, true,
                                     msaa ? SceneAntialiasing.BALANCED : SceneAntialiasing.DISABLED);
    subScene.setFill(fillPaint);
    subScene.setCamera(camera);

    return subScene;
  }

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }
}
