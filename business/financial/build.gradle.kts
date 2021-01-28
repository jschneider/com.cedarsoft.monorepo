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
  api(Libs.jodaTime)

  //
  // Compile Deps
  //
  api(project(Projects.open_commons_commons))
  api(Libs.jodaTime)
  testImplementation(project(Projects.open_commons_test_utils))
}
