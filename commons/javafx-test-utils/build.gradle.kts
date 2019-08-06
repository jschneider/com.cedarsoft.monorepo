description = """JavaFX Test Utils"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlin("jvm")
}

dependencies {
  api(project(Projects.dependencies_sets_annotations))
  api(project(Projects.open_annotations))

  api(project(Projects.open_commons_javafx))
  api(Libs.com_google_guava_guava)
  api(project(Projects.open_unit_unit))
  api(project(Projects.open_commons_concurrent))
  api(Libs.fxyz3d)
  api(Libs.testfx_junit5)


  api(project(Projects.dependencies_sets_kotlin_test))
  api(project(Projects.open_commons_test_utils))
}
