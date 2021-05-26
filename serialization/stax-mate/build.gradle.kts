description = """Serialization - Stax Mate"""


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
  api(project(Projects.open_serialization_stax))
  api(Libs.staxmate)
  api(Libs.woodstox_core_asl)
  api(Libs.commons_lang3)
  api(Libs.jettison)


  //
  // Test Deps
  //
  testImplementation(project(Projects.open_serialization_serialization_test_utils))
  testImplementation(project(Projects.open_commons_test_utils))
}
