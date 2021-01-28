description = """REST"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlinJvm
}

dependencies {
  api(project(Projects.dependencies_sets_kotlin))
  api(project(Projects.open_commons_guava))

  api(Libs.kotlinReflect)

  api(Libs.guava)
  compileOnlyApi(Libs.jacksonAnnotations)
  api(Libs.jacksonDatatypeJdk8)
  api(Libs.jacksonDatatypeJsr310)
  api(Libs.jacksonDatatypeGuava)
  api(Libs.jacksonModuleParameterNames)
  api(Libs.jacksonModuleKotlin)

  testImplementation(project(Projects.dependencies_sets_kotlin_test))
  testImplementation(project(Projects.open_commons_test_utils))
}
