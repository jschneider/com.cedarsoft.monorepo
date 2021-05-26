import Libs.jackson_annotations
import Libs.jackson_datatype_guava
import Libs.jackson_datatype_jdk8
import Libs.jackson_datatype_jsr310
import Libs.jackson_module_parameter_names

description = """REST"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlinJvm
}

dependencies {
  api(project(Projects.dependencies_sets_kotlin_jvm))
  api(project(Projects.open_commons_guava))

  api(Libs.kotlin_reflect)

  api(Libs.guava)
  compileOnlyApi(jackson_annotations)
  api(jackson_datatype_jdk8)
  api(jackson_datatype_jsr310)
  api(jackson_datatype_guava)
  api(jackson_module_parameter_names)
  api(Libs.jackson_module_kotlin)

  testImplementation(project(Projects.dependencies_sets_kotlin_test))
  testImplementation(project(Projects.open_commons_test_utils))
}
