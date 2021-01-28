description = """Serialization - Sample: Evolution"""


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
  api(project(Projects.open_serialization_stax_mate))
  api(Libs.commonsIo)
  api(project(Projects.open_commons_xml_commons))
  api(Libs.xstream)


  //
  // Test Deps
  //
  testImplementation(project(Projects.open_serialization_serialization_test_utils))
  testImplementation(project(Projects.open_commons_test_utils))
}
