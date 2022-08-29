package com.cedarsoft.commons.javafx.threed

import com.cedarsoft.commons.javafx.Components.label
import com.cedarsoft.commons.javafx.Components.textFieldDoubleDelayed
import com.cedarsoft.commons.javafx.threed.MeshGenerator.generateMeshFrom3DPoints
import com.cedarsoft.commons.javafx.threed.MeshGenerator.generatePoints
import com.cedarsoft.unit.other.px
import com.google.common.collect.ImmutableList
import javafx.application.Application
import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.scene.AmbientLight
import javafx.scene.Camera
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.PerspectiveCamera
import javafx.scene.PointLight
import javafx.scene.Scene
import javafx.scene.SceneAntialiasing
import javafx.scene.SubScene
import javafx.scene.control.Separator
import javafx.scene.control.Slider
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.paint.PhongMaterial
import javafx.scene.shape.CullFace
import javafx.scene.shape.Cylinder
import javafx.scene.shape.DrawMode
import javafx.scene.shape.MeshView
import javafx.scene.shape.TriangleMesh
import javafx.scene.transform.Rotate
import javafx.stage.Stage
import javax.annotation.Nonnull

/**
 * GlobalTilesCache  */
class MeshGeneratorDemo : Application() {

  override fun start(primaryStage: Stage) {
    val meshView = createMeshView(true)
    //MeshView meshViewLineMode = createMeshView(false);

    //meshViewLineMode.setDrawMode(DrawMode.LINE);
    //bindAll(meshView, meshViewLineMode);

    //Collection<? extends PolyLine3D> polyLines = createPolyLines((TriangleMesh) meshView.getMesh(), DIVISIONS);
    //polyLines.forEach(o -> bindAll(meshView, o));
    val nodes: MutableList<Node> = ArrayList()
    //nodes.addAll(polyLines);
    nodes.add(meshView)
    val nodesGroup = Group()
    nodesGroup.children.addAll(nodes)
    val subSceneWithMeshView = createSubScene(ImmutableList.of(nodesGroup))
    //SubScene subSceneWithMeshView = createSubScene(polyLine3D, meshView, meshViewLineMode);
    val cameraControlPane = createCameraControlPane(subSceneWithMeshView.camera)
    val meshViewControlPane = createMeshViewControlPane(nodesGroup)
    val root = VBox(10.0, subSceneWithMeshView, cameraControlPane, meshViewControlPane)
    root.padding = Insets(10.0)
    val scene = Scene(root, SCENE_INITIAL_WIDTH.toDouble(), SCENE_INITIAL_HEIGHT.toDouble(), true)
    primaryStage.scene = scene
    primaryStage.show()
  }

  private fun bindAll(source: Node, target: Node) {
    target.translateXProperty().bind(source.translateXProperty())
    target.translateYProperty().bind(source.translateYProperty())
    target.translateZProperty().bind(source.translateZProperty())
    target.scaleXProperty().bind(source.scaleXProperty())
    target.scaleYProperty().bind(source.scaleYProperty())
    target.scaleZProperty().bind(source.scaleZProperty())
    target.rotateProperty().bind(source.rotateProperty())
    target.rotationAxisProperty().bind(source.rotationAxisProperty())
  }

  private fun createPolyLines(mesh: TriangleMesh, divisionCount: Int): Node {
    return MeshLines(mesh, LINE_COLOR, divisionCount)
  }

  companion object {
    private const val SCENE_INITIAL_WIDTH: @px Int = 1680
    private const val SCENE_INITIAL_HEIGHT: @px Int = 1080
    private const val SUB_SCENE_HEIGHT: @px Int = 800
    private const val DIVISIONS = 24
    val LINE_COLOR = Color.rgb(8, 80, 121)
    val BG_COLOR = Color.rgb(8, 80, 121, 0.35)

    @JvmStatic
    fun main(args: Array<String>) {
      launch(*args)
    }

    private fun createCameraControlPane(@Nonnull camera: Camera): Pane {
      val controlPane = VBox(10.0)
      controlPane.children.add(Separator(Orientation.HORIZONTAL))
      val minValue = -2500
      val maxValue = +2500
      controlPane.children.add(createControlPane(camera.translateXProperty(), minValue.toDouble(), maxValue.toDouble(), "camera translate-x"))
      controlPane.children.add(createControlPane(camera.translateYProperty(), minValue.toDouble(), maxValue.toDouble(), "camera translate-y"))
      controlPane.children.add(createControlPane(camera.translateZProperty(), minValue.toDouble(), maxValue.toDouble(), "camera translate-z"))
      camera.rotationAxis = Rotate.Y_AXIS
      camera.rotate = -41.0
      controlPane.children.add(createControlPane(camera.rotateProperty(), -720.0, 720.0, "camera rotate y-axis angle"))
      return controlPane
    }

    private fun createMeshViewControlPane(@Nonnull node: Node): Pane {
      val controlPane = VBox(10.0)
      controlPane.children.add(Separator(Orientation.HORIZONTAL))
      val meshViewScaleProperty = SimpleDoubleProperty()
      meshViewScaleProperty.set(1.0)
      node.scaleXProperty().bind(meshViewScaleProperty)
      node.scaleYProperty().bind(meshViewScaleProperty)
      node.scaleZProperty().bind(meshViewScaleProperty)
      val minValue = 1
      val maxValue = 50
      controlPane.children.add(createControlPane(meshViewScaleProperty, minValue.toDouble(), maxValue.toDouble(), "mesh-view scale"))
      controlPane.children.add(Separator(Orientation.HORIZONTAL))
      node.rotationAxis = Rotate.X_AXIS
      controlPane.children.add(createControlPane(node.rotateProperty(), -720.0, 720.0, "mesh-view rotate x-axis angle"))
      return controlPane
    }

    private fun createControlPane(@Nonnull property: DoubleProperty, min: Double, max: Double, @Nonnull label: String): Pane {
      val controlPane = HBox(10.0)
      controlPane.children.add(label(label))
      val slider = Slider(min, max, property.value)
      slider.prefWidth = 400.0
      slider.valueProperty().bindBidirectional(property)
      controlPane.children.add(slider)
      controlPane.children.add(textFieldDoubleDelayed(property))
      return controlPane
    }

    private fun createSubScene(nodes: List<Node>): SubScene {
      val group = Group()

      /*
    group.getChildren().add(createXAxis());
    group.getChildren().add(createYAxis());
    group.getChildren().add(createZAxis());
    */
      val camera: Camera = PerspectiveCamera()
      camera.translateX = 1150.0
      camera.translateY = -460.0
      camera.translateZ = -740.0
      val light1 = PointLight(Color.WHITE)
      light1.translateX = 1200.0
      light1.translateY = -600.0
      light1.translateZ = -200.0
      group.children.add(light1)
      val light2 = PointLight(Color.DARKGRAY)
      light2.translateX = 1200.0
      light2.translateY = 600.0
      light2.translateZ = -400.0
      group.children.add(light2)
      group.children.addAll(nodes)
      val ambientLight = AmbientLight(Color.color(0.3, 0.3, 0.5))
      group.children.add(ambientLight)
      val subScene = SubScene(group, SCENE_INITIAL_WIDTH.toDouble(), SUB_SCENE_HEIGHT.toDouble(), true, SceneAntialiasing.BALANCED)
      subScene.camera = camera
      return subScene
    }

    private fun createMeshView(isFirst: Boolean): MeshView {
      //double[] xCoordinates = {0, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120};
      //double[] yCoordinates = {0, 10, 20, 30, 40, 40, 40, 35, 30, 25, 20, 0};
      //double[] xCoordinates = {0, 20, 30, 40};
      //double[] yCoordinates = {0, 10, 20, 15};
      val xCoordinates = doubleArrayOf(
        300.0,
        350.0,
        400.0,
        450.0,
        500.0,
        550.0,
        600.0,
        650.0,
        700.0,
        750.0,
        800.0,
        850.0,
        900.0,
        950.0,
        1000.0,
        1050.0,
        1100.0,
        1150.0,
        1200.0,
        1250.0,
        1300.0,
        1350.0,
        1400.0,
        1450.0,
        1500.0,
        1550.0,
        1600.0,
        1650.0,
        1700.0,
        1750.0,
        1800.0,
        1850.0,
        1900.0,
        1950.0,
        2000.0,
        2050.0,
        2100.0,
        2150.0,
        2200.0,
        2250.0,
        2300.0,
        2350.0,
        2400.0,
        2450.0,
        2500.0,
        2550.0,
        2600.0,
        2650.0,
        2700.0,
        2750.0,
        2800.0,
        2850.0,
        2900.0,
        2950.0,
        3000.0,
        3050.0,
        3100.0,
        3150.0,
        3200.0,
        3250.0,
        3300.0,
        3350.0,
        3400.0,
        3450.0,
        3500.0,
        3550.0,
        3600.0,
        3650.0,
        3700.0,
        3750.0,
        3800.0,
        3850.0,
        3900.0,
        3950.0,
        4000.0,
        4000.0
      )
      val yCoordinates = doubleArrayOf(
        25.71999740600586,
        11.055261611938477,
        5.3968000411987305,
        5.48799991607666,
        5.455999851226807,
        5.446400165557861,
        5.408319473266602,
        5.276480197906494,
        5.193919658660889,
        5.157759666442871,
        5.12384033203125,
        4.964159965515137,
        4.799999713897705,
        4.751999855041504,
        4.727999687194824,
        4.599999904632568,
        4.5167999267578125,
        4.53439998626709,
        4.524799823760986,
        4.415999889373779,
        4.319999694824219,
        4.241919994354248,
        4.178879737854004,
        4.131519794464111,
        4.034560203552246,
        5.801918029785156,
        7.469691753387451,
        9.222116470336914,
        11.006268501281738,
        12.539586067199707,
        13.6317777633667,
        15.074519157409668,
        16.496891021728516,
        17.311979293823242,
        18.337085723876953,
        19.782438278198242,
        21.06688690185547,
        21.330978393554688,
        21.799837112426758,
        21.591854095458984,
        21.617267608642578,
        20.956510543823242,
        20.122961044311523,
        19.347856521606445,
        19.228593826293945,
        19.036678314208984,
        18.859216690063477,
        17.475399017333984,
        15.974605560302734,
        15.373852729797363,
        13.46033000946045,
        13.03570556640625,
        12.851369857788086,
        12.047504425048828,
        12.239413261413574,
        11.779294967651367,
        11.20086669921875,
        11.363694190979004,
        11.54455852508545,
        11.192337989807129,
        10.339958190917969,
        10.367323875427246,
        10.070924758911133,
        8.445317268371582,
        8.009987831115723,
        8.003432273864746,
        7.585000514984131,
        8.374799728393555,
        8.813088417053223,
        8.810688018798828,
        8.316871643066406,
        9.117534637451172,
        8.632166862487793,
        7.412232875823975,
        8.976746559143066,
        0.0
      )
      val scaleY = if (isFirst) 7.5 else 7.6
      for (i in yCoordinates.indices) {
        xCoordinates[i] *= 0.5
        yCoordinates[i] *= scaleY
      }
      val points = generatePoints(xCoordinates, yCoordinates, DIVISIONS)
      val meshView = MeshView(generateMeshFrom3DPoints(points, DIVISIONS, xCoordinates.size))
      val material = PhongMaterial()
      if (isFirst) {
        material.diffuseColor = BG_COLOR
      } else {
        material.diffuseColor = LINE_COLOR
        meshView.drawMode = DrawMode.LINE
      }
      //material.setDiffuseMap(new Image("/com/cedarsoft/commons/javafx/border.png"));
      meshView.material = material
      meshView.cullFace = CullFace.NONE
      return meshView
    }

    private fun createXAxis(): Cylinder {
      val axisX = Cylinder(2.0, 10000.0)
      axisX.rotationAxis = Rotate.Z_AXIS
      axisX.rotate = 90.0
      axisX.translateX = 4950.0
      axisX.translateY = 0.0
      axisX.translateZ = 0.0
      axisX.material = PhongMaterial(Color.RED)
      return axisX
    }

    private fun createYAxis(): Cylinder {
      val axisY = Cylinder(2.0, 10000.0)
      axisY.translateX = 0.0
      axisY.translateY = -4950.0
      axisY.translateZ = 0.0
      axisY.material = PhongMaterial(Color.GREEN)
      return axisY
    }

    private fun createZAxis(): Cylinder {
      val axisZ = Cylinder(2.0, 10000.0)
      axisZ.rotationAxis = Rotate.X_AXIS
      axisZ.rotate = 90.0
      axisZ.translateX = 0.0
      axisZ.translateY = 0.0
      axisZ.translateZ = -4950.0
      axisZ.material = PhongMaterial(Color.BLUE)
      return axisZ
    }
  }
}
