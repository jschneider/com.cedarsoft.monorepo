description = """Commons"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlinJvm
}

dependencies {
  compileOnlyApi(project(Projects.dependencies_sets_jvm_annotations))
  compileOnlyApi(project(Projects.open_annotations))
  api(project(Projects.open_unit_unit))
  api(project(Projects.open_commons_exceptions))
  api(project(Projects.open_commons_kotlin_lang))

  api(Libs.guava)


  testImplementation(project(Projects.dependencies_sets_jvm_kotlin_test))
  testImplementation(project(Projects.open_commons_test_utils))
}
