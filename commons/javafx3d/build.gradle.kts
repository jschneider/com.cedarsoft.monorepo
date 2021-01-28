description = """JavaFX 3D"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlinJvm
}

dependencies {
  compileOnlyApi(project(Projects.dependencies_sets_annotations))
  compileOnlyApi(project(Projects.open_annotations))

  api(project(Projects.open_commons_javafx))
  api(Libs.guava)
  api(project(Projects.open_unit_unit))
  api(project(Projects.open_commons_concurrent))
  api(Libs.fxyz3d)


  testImplementation(project(Projects.dependencies_sets_kotlin_test))
  testImplementation(project(Projects.open_commons_test_utils))
  testImplementation(Libs.miglayoutJavafx)
  testImplementation(Libs.jfxtrasAll)
  testImplementation(Libs.fxGraphics2d)
  testImplementation(Libs.jfoenix)
  testImplementation(Libs.fontAwesomeFx)
  testImplementation(Libs.controlsfx)
  testImplementation(Libs.tilesFx)
  testImplementation(Libs.vworkflowsFx)
}
