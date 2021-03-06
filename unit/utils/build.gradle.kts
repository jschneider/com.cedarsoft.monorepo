description = """Unit Utils"""

group = "com.cedarsoft.unit"

plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlinJvm
}

dependencies {
  compileOnlyApi(project(Projects.dependencies_sets_annotations))
  api(project(Projects.dependencies_sets_kotlin_jvm))
  compileOnlyApi(project(Projects.open_annotations))
  api(project(Projects.open_unit_unit))

  testImplementation(project(Projects.dependencies_sets_kotlin_test))
  testImplementation(project(Projects.open_commons_test_utils))
}
