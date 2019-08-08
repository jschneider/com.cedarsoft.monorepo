description = """Kotlinx Serialization serializers"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlin("jvm")
  id("kotlinx-serialization")
}

dependencies {
  api(project(Projects.dependencies_sets_kotlin))
  api(project(Projects.open_commons_concurrent))
  api(project(Projects.open_commons_guava))

  api(Libs.guava)
  api(Libs.javax_annotation_api)
  api(Libs.kotlinx_coroutines_jdk8)
  api(AdditionalLibs.kotlinx_serialization_runtime_common)
  api(Libs.kotlinx_serialization_runtime)

  testImplementation(project(Projects.dependencies_sets_kotlin_test))
  testImplementation(project(Projects.open_commons_test_utils))
  testImplementation(project(Projects.open_commons_commons))
  testImplementation(Libs.awaitility)
  testImplementation(Libs.logback_classic)
}
