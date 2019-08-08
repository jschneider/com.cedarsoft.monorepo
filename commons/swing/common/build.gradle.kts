description = """Swing Common"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlin("jvm")
}

dependencies {
  api(project(Projects.dependencies_sets_annotations))

  api(Libs.slf4j_api)
  api(Libs.miglayout_swing)
  api(Libs.com_intellij_annotations)
  api(Libs.jide_oss)
  api(Libs.guava)
  api(project(Projects.open_annotations))
  api(project(Projects.open_unit_unit))

  //
  // Test Deps
  //
  testImplementation(project(Projects.open_commons_test_utils))
}
