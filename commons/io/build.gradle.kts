import Libs.commons_io

description = """IO"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlinJvm
}

dependencies {
  api(project(Projects.dependencies_sets_jvm_kotlin))
  api(commons_io)

  testImplementation(project(Projects.dependencies_sets_jvm_kotlin_test))
  testImplementation(project(Projects.open_commons_test_utils))
}
