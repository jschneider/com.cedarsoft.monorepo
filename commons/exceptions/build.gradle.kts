description = """Exceptions stuff"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlin("jvm")
}

dependencies {
  api(project(Projects.dependencies_sets_kotlin))

  testImplementation(project(Projects.dependencies_sets_test_basics))
  testImplementation(project(Projects.open_commons_test_utils))
  testImplementation(Libs.commons_codec)
}
