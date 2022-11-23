description = """Process execution stuff"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlinJvm
}

configureToolchainJava17LTS()

dependencies {
  api(project(Projects.dependencies_sets_jvm_kotlin))
  api(project(Projects.open_commons_kotlin_lang))

  testImplementation(project(Projects.dependencies_sets_jvm_kotlin_test))
  testImplementation(project(Projects.open_commons_test_utils))
  testImplementation(Libs.awaitility)
}
