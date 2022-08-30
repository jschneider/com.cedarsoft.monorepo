description = """Mail"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlinJvm
}

dependencies {
  compileOnlyApi(project(Projects.dependencies_sets_jvm_annotations))
  api(Libs.mail)
  implementation(Libs.slf4j_api)

  testImplementation(project(Projects.dependencies_sets_jvm_kotlin_test))
  testImplementation(project(Projects.open_commons_test_utils))
  testImplementation(Libs.logback_classic)
}
