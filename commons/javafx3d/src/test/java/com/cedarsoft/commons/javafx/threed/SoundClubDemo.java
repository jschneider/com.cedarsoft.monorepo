package com.cedarsoft.commons.javafx.threed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import org.fxyz3d.geometry.Point3D;
import org.fxyz3d.shapes.composites.PolyLine3D;
import org.fxyz3d.shapes.primitives.FrustumMesh;

import com.google.common.collect.ImmutableList;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.LightBase;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

/**
 * @author Christian Erbelding (<a href="mailto:ce@cedarsoft.com">ce@cedarsoft.com</a>)
 */
public class SoundClubDemo extends Application {

  private static final int GRID_CELL_WIDTH = 40;
  private static final int GRID_CELL_DEPTH = 40;
  private static final int GRID_CELL_COUNT_X = 10;
  private static final int GRID_CELL_COUNT_Z = 20;
  
  private static final Color ORANGE = Color.rgb(220, 120, 40);
  private static final Color BLUE = Color.rgb(8, 80, 121, 0.35);
  private static final Color SOUND_CLUB_LINE_COLOR = Color.rgb(8, 80, 121);
  private static final float GRID_Y_OFFSET = 100.0f;
  private static final int SOUND_CLUB_DIVISIONS = 24;
  private static final int FONT_HEIGHT = 8;

  // FIXME real values from sensor
  private static final double[] SOUND_CLUB_X_COORDINATES = {300, 350, 400, 450, 500, 550, 600, 650, 700, 750, 800, 850, 900, 950, 1000, 1050, 1100, 1150, 1200, 1250, 1300, 1350, 1400, 1450, 1500, 1550, 1600, 1650, 1700, 1750, 1800, 1850, 1900, 1950, 2000, 2050, 2100, 2150, 2200, 2250, 2300, 2350, 2400, 2450, 2500, 2550, 2600, 2650, 2700, 2750, 2800, 2850, 2900, 2950, 3000, 3050, 3100, 3150, 3200, 3250, 3300, 3350, 3400, 3450, 3500, 3550, 3600, 3650, 3700, 3750, 3800, 3850, 3900, 3950, 4000, 4000};
  private static final double[] SOUND_CLUB_Y_COORDINATES = {25.71999740600586, 11.055261611938477, 5.3968000411987305, 5.48799991607666, 5.455999851226807, 5.446400165557861, 5.408319473266602, 5.276480197906494, 5.193919658660889, 5.157759666442871, 5.12384033203125, 4.964159965515137, 4.799999713897705, 4.751999855041504, 4.727999687194824, 4.599999904632568, 4.5167999267578125, 4.53439998626709, 4.524799823760986, 4.415999889373779, 4.319999694824219, 4.241919994354248, 4.178879737854004, 4.131519794464111, 4.034560203552246, 5.801918029785156, 7.469691753387451, 9.222116470336914, 11.006268501281738, 12.539586067199707, 13.6317777633667, 15.074519157409668, 16.496891021728516, 17.311979293823242, 18.337085723876953, 19.782438278198242, 21.06688690185547, 21.330978393554688, 21.799837112426758, 21.591854095458984, 21.617267608642578, 20.956510543823242, 20.122961044311523, 19.347856521606445, 19.228593826293945, 19.036678314208984, 18.859216690063477, 17.475399017333984, 15.974605560302734, 15.373852729797363, 13.46033000946045, 13.03570556640625, 12.851369857788086, 12.047504425048828, 12.239413261413574, 11.779294967651367, 11.20086669921875, 11.363694190979004, 11.54455852508545, 11.192337989807129, 10.339958190917969, 10.367323875427246, 10.070924758911133, 8.445317268371582, 8.009987831115723, 8.003432273864746, 7.585000514984131, 8.374799728393555, 8.813088417053223, 8.810688018798828, 8.316871643066406, 9.117534637451172, 8.632166862487793, 7.412232875823975, 8.976746559143066, 0};


  public static void main(String... args) {
    Application.launch(args);
  }


  @Override
  public void start(Stage primaryStage) throws Exception {
    Button resetViewButton = new Button("Reset view");

    SubScene subScene = createSceneWithParallelCamera(resetViewButton);
    subScene.setFill(Color.WHITE);

    VBox vBox = new VBox(0);
    vBox.setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY)));
    vBox.getChildren().add(subScene);
    vBox.getChildren().add(resetViewButton);

    Scene scene = new Scene(vBox);
    primaryStage.setTitle(SoundClubDemo.class.getSimpleName());
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  @Nonnull
  private static SubScene createSceneWithParallelCamera(@Nonnull ButtonBase resetViewButton) {
    Group root = new Group();

    Collection<? extends Node> gridXZPane = createGridXZPane(GRID_Y_OFFSET);
    root.getChildren().addAll(gridXZPane);

    Collection<? extends Node> gridXZLabels = createGridXZLabels(GRID_Y_OFFSET);
    root.getChildren().addAll(gridXZLabels);
    TransformUtil.installInverseRotate(root, gridXZLabels);

    Shape3D sensorBox = createSensorBox();

    Shape3D detectionBox = createDetectionBox();

    Shape3D detectionRod = createDetectionRod();

    MeshView soundClub = createSoundClub();
    
    Shape3D innerSoundClub = createInnerSoundClub();

    Node soundClubLines = createSoundClubLines((TriangleMesh) soundClub.getMesh());
    TransformUtil.bindAll(soundClub, soundClubLines);

    Node soundClubShadow = createSoundClubShadow(GRID_Y_OFFSET);
    Node innerSoundClubShadow = createInnerSoundClubShadow(GRID_Y_OFFSET);

    root.getChildren().addAll(innerSoundClubShadow, soundClubShadow, sensorBox, detectionRod, detectionBox, innerSoundClub, soundClub, soundClubLines);
    
    Collection<? extends LightBase> lights = createLights(GRID_Y_OFFSET);
    root.getChildren().addAll(lights);
    
    for (LightBase light : lights) {
      light.getScope().setAll(sensorBox, detectionRod, soundClub, innerSoundClub, detectionBox, innerSoundClubShadow, soundClubShadow);
    }

    // correct!
    root.getTransforms().add(new Rotate(-45, Rotate.Y_AXIS));
    root.getTransforms().add(new Rotate(25, new javafx.geometry.Point3D(1.0, 0, -1.0)));
    root.getTransforms().add(new Translate(350, 300));

    SubScene subScene = new SubScene(root, 1024, 768, true, SceneAntialiasing.BALANCED);
    PerspectiveCamera camera = new PerspectiveCamera();
    camera.setFieldOfView(1.0);
    camera.setTranslateX(-512);
    camera.setTranslateY(-384);
    subScene.setCamera(camera);

    SubSceneInputEventSupport support = new SubSceneInputEventSupport(subScene);
    support.install(root);

    resetViewButton.setOnAction(event -> support.resetAll());

    return subScene;
  }

  @Nonnull
  private static Shape3D createSensorBox() {
    Box sensorBox = new Box(24, 24, 10);
    sensorBox.setMaterial(new PhongMaterial(Color.rgb(0, 180, 255)));
    sensorBox.setTranslateZ(-sensorBox.getDepth() * 0.5);
    return sensorBox;
  }

  @Nonnull
  private static Shape3D createDetectionBox() {
    Box detectionBox = new Box(80, 150, 10);
    detectionBox.setMaterial(new PhongMaterial(Color.rgb(0, 100, 180)));
    detectionBox.setTranslateX(detectionBox.getWidth() * 0.5);
    detectionBox.setTranslateY(-detectionBox.getHeight() * 0.5 + GRID_Y_OFFSET);
    detectionBox.setTranslateZ(0.5 * GRID_CELL_COUNT_Z * GRID_CELL_DEPTH);
    detectionBox.setRotationAxis(Rotate.Y_AXIS);
    detectionBox.setRotate(15);
    return detectionBox;
  }

  @Nonnull
  private static Shape3D createDetectionRod() {
    FrustumMesh frustumMesh = new FrustumMesh(5, 5, 150);
    frustumMesh.setMaterial(new PhongMaterial(ORANGE));
    frustumMesh.setTranslateX(-0.5 * GRID_CELL_WIDTH);
    frustumMesh.setTranslateY(-frustumMesh.getHeight() * 0.5 + GRID_Y_OFFSET);
    frustumMesh.setTranslateZ(0.35 * GRID_CELL_COUNT_Z * GRID_CELL_DEPTH);
    return frustumMesh;
  }

  @Nonnull
  private static MeshView createSoundClub() {
    double[] xCoordinates = Arrays.copyOf(SOUND_CLUB_X_COORDINATES, SOUND_CLUB_X_COORDINATES.length);
    double[] yCoordinates = Arrays.copyOf(SOUND_CLUB_Y_COORDINATES, SOUND_CLUB_Y_COORDINATES.length);

    // FIXME: use values from sensor
    for (int i = 0; i < xCoordinates.length; i++) {
      xCoordinates[i] *= 0.15;
      yCoordinates[i] *= 2.2;
    }

    float[] points = MeshGenerator.generatePoints(xCoordinates, yCoordinates, SOUND_CLUB_DIVISIONS);
    MeshView meshView = new MeshView(MeshGenerator.generateMeshFrom3DPoints(points, SOUND_CLUB_DIVISIONS, xCoordinates.length));
    meshView.setMaterial(new PhongMaterial(BLUE));
    meshView.setCullFace(CullFace.NONE);
    meshView.getTransforms().add(new Rotate(-90, Rotate.Y_AXIS));

    return meshView;
  }
  
  @Nonnull
  private static Shape3D createInnerSoundClub() {
    double[] xCoordinates = Arrays.copyOf(SOUND_CLUB_X_COORDINATES, SOUND_CLUB_X_COORDINATES.length);
    double[] yCoordinates = Arrays.copyOf(SOUND_CLUB_Y_COORDINATES, SOUND_CLUB_Y_COORDINATES.length);

    // FIXME: use values from sensor
    for (int i = 0; i < xCoordinates.length; i++) {
      xCoordinates[i] *= 0.15;
      yCoordinates[i] *= 1.6;
    }

    float[] points = MeshGenerator.generatePoints(xCoordinates, yCoordinates, SOUND_CLUB_DIVISIONS);
    MeshView meshView = new MeshView(MeshGenerator.generateMeshFrom3DPoints(points, SOUND_CLUB_DIVISIONS, xCoordinates.length));
    meshView.setMaterial(new PhongMaterial(ORANGE));
    meshView.setCullFace(CullFace.NONE);
    meshView.getTransforms().add(new Rotate(-90, Rotate.Y_AXIS));

    return meshView;
  }

  @Nonnull
  private static Node createSoundClubLines(@Nonnull TriangleMesh mesh) {
    return new MeshLines(mesh, SOUND_CLUB_LINE_COLOR, PolyLine3D.LineType.TRIANGLE, 2.0f, 1.01f, SOUND_CLUB_DIVISIONS);
  }

  @Nonnull
  private static Node createSoundClubShadow(final float yPos) {
    double[] xCoordinates = Arrays.copyOf(SOUND_CLUB_X_COORDINATES, SOUND_CLUB_X_COORDINATES.length);
    double[] yCoordinates = Arrays.copyOf(SOUND_CLUB_Y_COORDINATES, SOUND_CLUB_Y_COORDINATES.length);

    final int numberOfCoordinates = xCoordinates.length;
    for (int i = 0; i < numberOfCoordinates; i++) {
      xCoordinates[i] *= 0.15;
      yCoordinates[i] *= 2.2;
    }

    return createShadow(yPos - 0.1f, xCoordinates, yCoordinates, BLUE);
  }

  @Nonnull
  private static Node createInnerSoundClubShadow(final float yPos) {
    double[] xCoordinates = Arrays.copyOf(SOUND_CLUB_X_COORDINATES, SOUND_CLUB_X_COORDINATES.length);
    double[] yCoordinates = Arrays.copyOf(SOUND_CLUB_Y_COORDINATES, SOUND_CLUB_Y_COORDINATES.length);

    final int numberOfCoordinates = xCoordinates.length;
    for (int i = 0; i < numberOfCoordinates; i++) {
      xCoordinates[i] *= 0.15;
      yCoordinates[i] *= 1.6;
    }
    
    return createShadow(yPos - 0.15f, xCoordinates, yCoordinates, Color.rgb(220, 120, 40, 0.65));
  }

  @Nonnull
  private static Node createShadow(final float yPos,
                                   @Nonnull double[] xCoordinates,
                                   @Nonnull double[] yCoordinates,
                                   @Nonnull Color shadowColor) {
    final int numberOfCoordinates = xCoordinates.length;

    List<javafx.geometry.Point3D> points3D = new ArrayList<>();
    for (int i = 0; i < numberOfCoordinates; i++) {
      double x2D = xCoordinates[i];
      double y2D = yCoordinates[i];
      points3D.add(new javafx.geometry.Point3D(x2D, y2D, 0));
      points3D.add(new javafx.geometry.Point3D(x2D, -y2D, 0));
    }
    float[] points = MeshGenerator.flatten(points3D);

    // FIXME why does this not work?
    float[] textCoords = {0.5f, 0.0f, // t0
                          0.0f, 1.0f, // t1
                          1.0f, 1.0f  // t2
    };

    Collection<Integer> faces = new ArrayList<>();
    for (int i = 0, k = 0; i < numberOfCoordinates - 3; i++, k += 2) {
      addTriangle(faces, k + 2, k, k + 1);
      addTriangle(faces, k + 2, k + 1, k + 3);
    }

    TriangleMesh triangleMesh = new TriangleMesh();
    triangleMesh.getPoints().addAll(points);
    triangleMesh.getTexCoords().addAll(textCoords);
    triangleMesh.getFaces().addAll(faces.stream().mapToInt(i -> i).toArray());
    MeshView meshView = new MeshView(triangleMesh);

    meshView.setCullFace(CullFace.BACK);
    meshView.setMaterial(new PhongMaterial(shadowColor));
    meshView.getTransforms().add(new Rotate(-90, Rotate.Y_AXIS));
    meshView.getTransforms().add(new Rotate(-90, Rotate.X_AXIS));
    meshView.setTranslateY(yPos);

    return meshView;
  }
  
  /**
   * Vertices are supposed to be in counter-clockwise order.
   */
  private static void addTriangle(@Nonnull Collection<? super Integer> faces,
                                  final int v0,
                                  final int v1,
                                  final int v2) {
    // front face, vertices counter-clockwise, start vertex does not matter
    faces.add(v0);
    faces.add(0);
    faces.add(v1);
    faces.add(1);
    faces.add(v2);
    faces.add(2);

    // back face, vertices clockwise, start vertex does not matter
    faces.add(v0);
    faces.add(0);
    faces.add(v2);
    faces.add(2);
    faces.add(v1);
    faces.add(1);
  }

  @Nonnull
  private static Collection<? extends Node> createGridXZPane(final float yPos) {
    final float thickLine = 3.0f;
    final float thinLine = 1.0f;

    List<Node> gridNodes = new ArrayList<>();

    final int halfCellCountX = GRID_CELL_COUNT_X / 2;
    for (int x = -halfCellCountX; x < halfCellCountX + 1; x++) {
      Point3D startPoint = new Point3D(x * GRID_CELL_WIDTH, yPos, 0);
      Point3D endPoint = new Point3D(x * GRID_CELL_WIDTH, yPos, GRID_CELL_COUNT_Z * GRID_CELL_DEPTH);
      gridNodes.add(new PolyLine3D(ImmutableList.of(startPoint, endPoint), x % 5 == 0 ? thickLine : thinLine, Color.GRAY, PolyLine3D.LineType.TRIANGLE));
    }
    for (int z = 0; z < GRID_CELL_COUNT_Z + 1; z++) {
      Point3D startPoint = new Point3D(-halfCellCountX * GRID_CELL_WIDTH, yPos, z * GRID_CELL_DEPTH);
      Point3D endPoint = new Point3D(halfCellCountX * GRID_CELL_WIDTH, yPos, z * GRID_CELL_DEPTH);
      gridNodes.add(new PolyLine3D(ImmutableList.of(startPoint, endPoint), z % 5 == 0 ? thickLine : thinLine, Color.GRAY, PolyLine3D.LineType.TRIANGLE));
    }

    return gridNodes;
  }
  
  @Nonnull
  private static Collection<? extends Node> createGridXZLabels(final float yPos) {
    Collection<Node> labels = new ArrayList<>();

    final int halfCellCountX = GRID_CELL_COUNT_X / 2;
    
    final int maxZValue = 6400; // FIXME correct value from sensor
    final double zCellsPerLabel = 5.0 / 2.0; // a label every 2.5 cells
    final double totalDepth = GRID_CELL_COUNT_Z * GRID_CELL_DEPTH;
    final double zDistance = GRID_CELL_DEPTH * zCellsPerLabel;
    final int zValueOffset = (int) (maxZValue / GRID_CELL_COUNT_Z * zCellsPerLabel);
    int zValue = 0;
    double zPos = 0;
    while (zPos <= totalDepth + 1) { // + 1 due to rounding errors
      Node label = new Label(Integer.toString(zValue));
      labels.add(label);
      label.setTranslateX(-halfCellCountX * GRID_CELL_WIDTH - 50);
      label.setTranslateY(yPos - FONT_HEIGHT);
      label.setTranslateZ(zPos);
      zPos += zDistance;
      zValue += zValueOffset;
    }

    final int maxXValue = 2400; // FIXME correct value from sensor
    final double xCellsPerLabel = 5.0 / 3.0; // a label every 1.666 cells
    final double halfWidth = halfCellCountX * GRID_CELL_WIDTH;
    final double xDistance = GRID_CELL_WIDTH * xCellsPerLabel;
    final int xValueOffset = (int) ((maxXValue / GRID_CELL_COUNT_X) * xCellsPerLabel) * 2;
    int xValue = -maxXValue;
    double xPos = -halfWidth;
    while (xPos <= halfWidth + 1) { // + 1 due to rounding errors
      Node label = new Label(Integer.toString(Math.abs(xValue)));
      labels.add(label);
      label.setTranslateX(xPos);
      label.setTranslateY(yPos - FONT_HEIGHT);
      label.setTranslateZ(totalDepth + 50);
      xPos += xDistance;
      xValue += xValueOffset;
    }

    return labels;
  }

  @Nonnull
  private static Collection<? extends LightBase> createLights(final double yPos) {
    PointLight light1 = new PointLight(Color.color(0.5, 0.5, 0.5));
    light1.setTranslateX(-3000);
    light1.setTranslateY(-500);
    light1.setTranslateZ(GRID_CELL_COUNT_Z * GRID_CELL_DEPTH * 0.5);

    PointLight light2 = new PointLight(Color.color(0.6, 0.6, 0.6));
    light2.setTranslateX(0);
    light2.setTranslateY(-600);
    light2.setTranslateZ(-500);

    return ImmutableList.of(light1, light2, new AmbientLight(Color.color(0.4, 0.4, 0.4)));
  }

}
