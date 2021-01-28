description = """JavaFX"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlinJvm
}

dependencies {
  api(project(Projects.dependencies_sets_kotlin))

  compileOnlyApi(project(Projects.open_annotations))

  api(project(Projects.open_commons_concurrent))
  api(project(Projects.open_unit_unit))

  api(Libs.guava)

  api(project(Projects.open_commons_time))
  api(project(Projects.open_unit_unit))
  api(project(Projects.open_commons_concurrent))

  testImplementation(project(Projects.dependencies_sets_kotlin_test))
  testImplementation(project(Projects.open_commons_test_utils))
  testImplementation(project(Projects.open_commons_javafx_test_utils))
  testImplementation(Libs.miglayoutJavafx)
  testImplementation(Libs.jfxtrasAll)
  testImplementation(Libs.fxGraphics2d)
  testImplementation(Libs.jfoenix)
  testImplementation(Libs.fontAwesomeFx)
  testImplementation(Libs.controlsfx)
  testImplementation(Libs.tilesFx)
  testImplementation(Libs.vworkflowsFx)
}
