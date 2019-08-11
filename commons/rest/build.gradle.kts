description = """REST"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlinJvm
}

dependencies {
  api(project(Projects.dependencies_sets_kotlin))
  api(project(Projects.open_commons_guava))

  api(Libs.guava)
  api(Libs.jackson_annotations)
  api(Libs.jackson_datatype_jdk8)
  api(Libs.jackson_datatype_jsr310)
  api(Libs.jackson_datatype_guava)
  api(Libs.jackson_module_parameter_names)
  api(Libs.jackson_module_kotlin)

  testImplementation(project(Projects.dependencies_sets_kotlin_test))
  testImplementation(project(Projects.open_commons_test_utils))
}
