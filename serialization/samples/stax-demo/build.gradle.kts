description = """Serialization - Sample: Stax"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlin("jvm")
}

dependencies {
  //
  // Compile Deps
  //
  api(project(Projects.open_serialization_serialization))
  api(project(Projects.open_serialization_stax_mate))
  api(Libs.commons_io)
  api(project(Projects.open_commons_xml_commons))
  api(Libs.xstream)

  //
  // Test Deps
  //
  testImplementation(project(Projects.open_serialization_serialization_test_utils))
  testImplementation(project(Projects.open_commons_test_utils))
}
