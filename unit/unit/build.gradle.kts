description = """Unit"""

group = "com.cedarsoft"

plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlin("jvm")
}

dependencies {
  api(project(Projects.dependencies_sets_annotations))
  api(project(Projects.dependencies_sets_kotlin))
  api(project(Projects.open_annotations))

  testImplementation(project(Projects.dependencies_sets_kotlin_test))
  testImplementation(project(Projects.open_commons_test_utils))
}
