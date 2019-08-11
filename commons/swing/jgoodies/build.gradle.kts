description = """Swing JGoodies Extensions"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlinJvm
}

dependencies {
  api(project(Projects.dependencies_sets_annotations))
  api(project(Projects.open_annotations))

  api(Libs.jgoodies_binding)
  api(Libs.jgoodies_validation)
  api(Libs.glazedlists_java16)
  api(Libs.miglayout_swing)

  //
  // Test Deps
  //
  testImplementation(project(Projects.open_commons_test_utils))
}
