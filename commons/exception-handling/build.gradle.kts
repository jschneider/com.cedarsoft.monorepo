description = """Exception Handling stuff"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlinJvm
}

dependencies {
  compileOnlyApi(project(Projects.dependencies_sets_jvm_annotations))
  compileOnlyApi(project(Projects.open_annotations))

  api(project(Projects.open_commons_exceptions))

  api(Libs.slf4j_api)
  api(Libs.guava)

  testImplementation(project(Projects.open_commons_test_utils))
}
