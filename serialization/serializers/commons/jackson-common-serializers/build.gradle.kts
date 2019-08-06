description = """Serialzation - Jackson Common Serializers"""


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
  api(project(Projects.open_serialization_jackson))
  api(Libs.commons_io)
  api(project(Projects.open_commons_xml_commons))

  api(project(Projects.open_commons_app))
  api(project(Projects.open_commons_license))
  api(project(Projects.open_commons_file))
  api(project(Projects.open_commons_crypt))
  api(Libs.joda_time)

  //
  // Test Deps
  //
  testImplementation(project(Projects.open_serialization_serialization_test_utils))
  testImplementation(project(Projects.open_commons_test_utils))
}
