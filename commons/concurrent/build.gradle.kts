

description = """Concurrent stuff"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlinJvm
}

dependencies {
  api(project(Projects.dependencies_sets_kotlin_jvm))

  compileOnlyApi(project(Projects.open_annotations))
  api(project(Projects.open_unit_unit))
  api(project(Projects.open_commons_exceptions))
  api(project(Projects.open_commons_kotlin_lang))

  api(Libs.guava)
  compileOnlyApi(Libs.javax_annotation_api)

  testImplementation(project(Projects.dependencies_sets_kotlin_test))
  testImplementation(project(Projects.open_commons_test_utils))
  testImplementation(project(Projects.open_commons_commons))
  testImplementation(Libs.awaitility)
  testImplementation(Libs.logback_classic)
}
