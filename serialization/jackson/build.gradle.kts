import Libs.jackson_core
import Libs.jackson_databind

description = """Serialization - Jackson"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlinJvm
}

dependencies {
  //
  // Compile Deps
  //
  api(project(Projects.open_serialization_serialization))
  api(jackson_core)

  //
  // Test Deps
  //
  testImplementation(project(Projects.open_serialization_serialization_test_utils))
  testImplementation(project(Projects.open_commons_test_utils))
  testImplementation(jackson_databind)
  testImplementation(Libs.guice)
}
