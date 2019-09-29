

description = """Concurrent stuff"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlinJvm
}

dependencies {
  api(project(Projects.dependencies_sets_kotlin))

  api(project(Projects.open_annotations))
  api(project(Projects.open_unit_unit))
  api(project(Projects.open_commons_exceptions))

  api(Libs.guava)
  api(Libs.javax_annotation_api)

  api(Libs.kotlinx_coroutines_jdk8)

  testImplementation(project(Projects.dependencies_sets_kotlin_test))
  testImplementation(project(Projects.open_commons_test_utils))
  testImplementation(project(Projects.open_commons_commons))
  testImplementation(Libs.awaitility)
  testImplementation(Libs.logback_classic)
}
