import Libs.commons_io

description = """Serialzation - StaxMate Common Serializers"""


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
  api(commons_io)
  api(project(Projects.open_commons_xml_commons))


  api(project(Projects.open_commons_app))
  api(project(Projects.open_commons_license))
  api(project(Projects.open_commons_file))
  api(project(Projects.open_commons_crypt))

  //
  // Test Deps
  //
  testImplementation(project(Projects.open_serialization_serialization_test_utils))
  testImplementation(project(Projects.open_commons_test_utils))
}
