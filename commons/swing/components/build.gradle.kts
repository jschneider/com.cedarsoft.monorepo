description = """Swing Components"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlin("jvm")
}

dependencies {
  api(project(Projects.dependencies_sets_annotations))

  //
  // Compile Deps
  //
  api(Libs.guava)
  api(project(Projects.open_unit_unit))

  //
  // Test Deps
  //
  testImplementation(project(Projects.open_commons_test_utils))
}
