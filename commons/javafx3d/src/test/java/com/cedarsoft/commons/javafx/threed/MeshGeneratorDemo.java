package com.cedarsoft.commons.javafx.threed;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.cedarsoft.commons.javafx.Components;
import com.cedarsoft.unit.other.px;
import com.google.common.collect.ImmutableList;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.AmbientLight;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

/**
GlobalTilesCache */
public class MeshGeneratorDemo extends Application {

  @px
  private static final int SCENE_INITIAL_WIDTH = 1680;
  @px
  private static final int SCENE_INITIAL_HEIGHT = 1080;
  @px
  private static final int SUB_SCENE_HEIGHT = 800;

  private static final int DIVISIONS = 24;

  public static final Color LINE_COLOR = Color.rgb(8, 80, 121);
  public static final Color BG_COLOR = Color.rgb(8, 80, 121, 0.35);

  public static void main(String... args) {
    Application.launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    MeshView meshView = createMeshView(true);
    //MeshView meshViewLineMode = createMeshView(false);

    //meshViewLineMode.setDrawMode(DrawMode.LINE);
    //bindAll(meshView, meshViewLineMode);

    //Collection<? extends PolyLine3D> polyLines = createPolyLines((TriangleMesh) meshView.getMesh(), DIVISIONS);
    //polyLines.forEach(o -> bindAll(meshView, o));

    List<Node> nodes = new ArrayList<>();
    //nodes.addAll(polyLines);
    nodes.add(meshView);

    Group nodesGroup = new Group();
    nodesGroup.getChildren().addAll(nodes);

    SubScene subSceneWithMeshView = createSubScene(ImmutableList.of(nodesGroup));
    //SubScene subSceneWithMeshView = createSubScene(polyLine3D, meshView, meshViewLineMode);

    Pane cameraControlPane = createCameraControlPane(subSceneWithMeshView.getCamera());
    Pane meshViewControlPane = createMeshViewControlPane(nodesGroup);

    VBox root = new VBox(10, subSceneWithMeshView, cameraControlPane, meshViewControlPane);
    root.setPadding(new Insets(10));
    Scene scene = new Scene(root, SCENE_INITIAL_WIDTH, SCENE_INITIAL_HEIGHT, true);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  private void bindAll(@Nonnull Node source, Node target) {
    target.translateXProperty().bind(source.translateXProperty());
    target.translateYProperty().bind(source.translateYProperty());
    target.translateZProperty().bind(source.translateZProperty());

    target.scaleXProperty().bind(source.scaleXProperty());
    target.scaleYProperty().bind(source.scaleYProperty());
    target.scaleZProperty().bind(source.scaleZProperty());

    target.rotateProperty().bind(source.rotateProperty());
    target.rotationAxisProperty().bind(source.rotationAxisProperty());
  }

  @Nonnull
  private Node createPolyLines(@Nonnull TriangleMesh mesh, int divisionCount) {
    return new MeshLines(mesh, LINE_COLOR, divisionCount);
  }

  @Nonnull
  private static Pane createCameraControlPane(@Nonnull Camera camera) {
    VBox controlPane = new VBox(10);
    controlPane.getChildren().add(new Separator(Orientation.HORIZONTAL));

    final int minValue = -2500;
    final int maxValue = +2500;
    controlPane.getChildren().add(createControlPane(camera.translateXProperty(), minValue, maxValue, "camera translate-x"));
    controlPane.getChildren().add(createControlPane(camera.translateYProperty(), minValue, maxValue, "camera translate-y"));
    controlPane.getChildren().add(createControlPane(camera.translateZProperty(), minValue, maxValue, "camera translate-z"));

    camera.setRotationAxis(Rotate.Y_AXIS);
    camera.setRotate(-41);
    controlPane.getChildren().add(createControlPane(camera.rotateProperty(), -720, 720, "camera rotate y-axis angle"));

    return controlPane;
  }

  @Nonnull
  private static Pane createMeshViewControlPane(@Nonnull Node node) {
    VBox controlPane = new VBox(10);
    controlPane.getChildren().add(new Separator(Orientation.HORIZONTAL));

    SimpleDoubleProperty meshViewScaleProperty = new SimpleDoubleProperty();
    meshViewScaleProperty.set(1.0);
    node.scaleXProperty().bind(meshViewScaleProperty);
    node.scaleYProperty().bind(meshViewScaleProperty);
    node.scaleZProperty().bind(meshViewScaleProperty);

    final int minValue = 1;
    final int maxValue = 50;
    controlPane.getChildren().add(createControlPane(meshViewScaleProperty, minValue, maxValue, "mesh-view scale"));

    controlPane.getChildren().add(new Separator(Orientation.HORIZONTAL));
    node.setRotationAxis(Rotate.X_AXIS);
    controlPane.getChildren().add(createControlPane(node.rotateProperty(), -720, 720, "mesh-view rotate x-axis angle"));

    return controlPane;
  }

  @Nonnull
  private static Pane createControlPane(@Nonnull DoubleProperty property, final double min, final double max, @Nonnull String label) {
    HBox controlPane = new HBox(10);
    controlPane.getChildren().add(Components.label(label));
    Slider slider = new Slider(min, max, property.getValue());
    slider.setPrefWidth(400.0);
    slider.valueProperty().bindBidirectional(property);
    controlPane.getChildren().add(slider);
    controlPane.getChildren().add(Components.textFieldDoubleDelayed(property));
    return controlPane;
  }

  @Nonnull
  private static SubScene createSubScene(@Nonnull List<? extends Node> nodes) {
    Group group = new Group();

    /*
    group.getChildren().add(createXAxis());
    group.getChildren().add(createYAxis());
    group.getChildren().add(createZAxis());
    */

    Camera camera = new PerspectiveCamera();
    camera.setTranslateX(1150);
    camera.setTranslateY(-460);
    camera.setTranslateZ(-740);

    PointLight light1 = new PointLight(Color.WHITE);
    light1.setTranslateX(1200);
    light1.setTranslateY(-600);
    light1.setTranslateZ(-200);
    group.getChildren().add(light1);

    PointLight light2 = new PointLight(Color.DARKGRAY);
    light2.setTranslateX(1200);
    light2.setTranslateY(600);
    light2.setTranslateZ(-400);
    group.getChildren().add(light2);

    group.getChildren().addAll(nodes);

    AmbientLight ambientLight = new AmbientLight(Color.color(0.3, 0.3, 0.5));
    group.getChildren().add(ambientLight);

    SubScene subScene = new SubScene(group, SCENE_INITIAL_WIDTH, SUB_SCENE_HEIGHT, true, SceneAntialiasing.BALANCED);
    subScene.setCamera(camera);

    return subScene;
  }

  @Nonnull
  private static MeshView createMeshView(boolean isFirst) {
    //double[] xCoordinates = {0, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120};
    //double[] yCoordinates = {0, 10, 20, 30, 40, 40, 40, 35, 30, 25, 20, 0};
    //double[] xCoordinates = {0, 20, 30, 40};
    //double[] yCoordinates = {0, 10, 20, 15};
    double[] xCoordinates = {300, 350, 400, 450, 500, 550, 600, 650, 700, 750, 800, 850, 900, 950, 1000, 1050, 1100, 1150, 1200, 1250, 1300, 1350, 1400, 1450, 1500, 1550, 1600, 1650, 1700, 1750, 1800, 1850, 1900, 1950, 2000, 2050, 2100, 2150, 2200, 2250, 2300, 2350, 2400, 2450, 2500, 2550, 2600, 2650, 2700, 2750, 2800, 2850, 2900, 2950, 3000, 3050, 3100, 3150, 3200, 3250, 3300, 3350, 3400, 3450, 3500, 3550, 3600, 3650, 3700, 3750, 3800, 3850, 3900, 3950, 4000, 4000};
    double[] yCoordinates = {25.71999740600586, 11.055261611938477, 5.3968000411987305, 5.48799991607666, 5.455999851226807, 5.446400165557861, 5.408319473266602, 5.276480197906494, 5.193919658660889, 5.157759666442871, 5.12384033203125, 4.964159965515137, 4.799999713897705, 4.751999855041504, 4.727999687194824, 4.599999904632568, 4.5167999267578125, 4.53439998626709, 4.524799823760986, 4.415999889373779, 4.319999694824219, 4.241919994354248, 4.178879737854004, 4.131519794464111, 4.034560203552246, 5.801918029785156, 7.469691753387451, 9.222116470336914, 11.006268501281738, 12.539586067199707, 13.6317777633667, 15.074519157409668, 16.496891021728516, 17.311979293823242, 18.337085723876953, 19.782438278198242, 21.06688690185547, 21.330978393554688, 21.799837112426758, 21.591854095458984, 21.617267608642578, 20.956510543823242, 20.122961044311523, 19.347856521606445, 19.228593826293945, 19.036678314208984, 18.859216690063477, 17.475399017333984, 15.974605560302734, 15.373852729797363, 13.46033000946045, 13.03570556640625, 12.851369857788086, 12.047504425048828, 12.239413261413574, 11.779294967651367, 11.20086669921875, 11.363694190979004, 11.54455852508545, 11.192337989807129, 10.339958190917969, 10.367323875427246, 10.070924758911133, 8.445317268371582, 8.009987831115723, 8.003432273864746, 7.585000514984131, 8.374799728393555, 8.813088417053223, 8.810688018798828, 8.316871643066406, 9.117534637451172, 8.632166862487793, 7.412232875823975, 8.976746559143066, 0};

    double scaleY = isFirst ? 7.5 : 7.6;
    for (int i = 0; i < yCoordinates.length; i++) {
      xCoordinates[i] *= 0.5;
      yCoordinates[i] *= scaleY;
    }

    float[] points = MeshGenerator.generatePoints(xCoordinates, yCoordinates, DIVISIONS);
    MeshView meshView = new MeshView(MeshGenerator.generateMeshFrom3DPoints(points, DIVISIONS, xCoordinates.length));

    PhongMaterial material = new PhongMaterial();
    if (isFirst) {
      material.setDiffuseColor(BG_COLOR);
    }
    else {
      material.setDiffuseColor(LINE_COLOR);
      meshView.setDrawMode(DrawMode.LINE);
    }
    //material.setDiffuseMap(new Image("/com/cedarsoft/commons/javafx/border.png"));
    meshView.setMaterial(material);
    meshView.setCullFace(CullFace.NONE);

    return meshView;
  }

  @Nonnull
  private static Cylinder createXAxis() {
    Cylinder axisX = new Cylinder(2, 10000);
    axisX.setRotationAxis(Rotate.Z_AXIS);
    axisX.setRotate(90.0);
    axisX.setTranslateX(4950);
    axisX.setTranslateY(0);
    axisX.setTranslateZ(0);
    axisX.setMaterial(new PhongMaterial(Color.RED));
    return axisX;
  }

  @Nonnull
  private static Cylinder createYAxis() {
    Cylinder axisY = new Cylinder(2, 10000);
    axisY.setTranslateX(0);
    axisY.setTranslateY(-4950);
    axisY.setTranslateZ(0);
    axisY.setMaterial(new PhongMaterial(Color.GREEN));
    return axisY;
  }

  @Nonnull
  private static Cylinder createZAxis() {
    Cylinder axisZ = new Cylinder(2, 10000);
    axisZ.setRotationAxis(Rotate.X_AXIS);
    axisZ.setRotate(90.0);
    axisZ.setTranslateX(0);
    axisZ.setTranslateY(0);
    axisZ.setTranslateZ(-4950);
    axisZ.setMaterial(new PhongMaterial(Color.BLUE));
    return axisZ;
  }


}
