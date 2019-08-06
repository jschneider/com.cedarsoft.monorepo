description = """JavaFX"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlin("jvm")
}

dependencies {
  api(project(Projects.dependencies_sets_kotlin))

  api(project(Projects.open_annotations))

  api(project(Projects.open_commons_concurrent))
  api(project(Projects.open_unit_unit))

  api(Libs.com_google_guava_guava)

  api(project(Projects.open_commons_time))
  api(project(Projects.open_unit_unit))
  api(project(Projects.open_commons_concurrent))

  testImplementation(project(Projects.dependencies_sets_kotlin_test))
  testImplementation(project(Projects.open_commons_test_utils))
  testImplementation(Libs.miglayout_javafx)
  testImplementation(Libs.jfxtras_all)
  testImplementation(Libs.fxgraphics2d)
  testImplementation(Libs.jfoenix)
  testImplementation(Libs.fontawesomefx)
  testImplementation(Libs.controlsfx)
  testImplementation(Libs.tilesfx)
  testImplementation(Libs.vworkflows_fx)
}
