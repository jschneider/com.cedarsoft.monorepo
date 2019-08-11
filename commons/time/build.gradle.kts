description = """Time extensions"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlinJvm
}

dependencies {
  api(project(Projects.dependencies_sets_annotations))
  api(project(Projects.open_annotations))
  api(project(Projects.open_unit_unit))
  api(Libs.commons_lang3)

  testImplementation(project(Projects.dependencies_sets_kotlin_test))
  testImplementation(project(Projects.open_commons_test_utils))
}
