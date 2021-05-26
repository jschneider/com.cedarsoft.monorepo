description = """Business - Financial"""

plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlinJvm
}

dependencies {
  //
  // Compile Deps
  //
  api(Libs.joda_time)

  //
  // Compile Deps
  //
  api(project(Projects.open_commons_commons))
  testImplementation(project(Projects.open_commons_test_utils))
}
