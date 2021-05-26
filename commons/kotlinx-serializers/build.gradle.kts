

description = """Kotlinx Serialization serializers"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlinJvm
  kotlinxSerialization
}

dependencies {
  api(project(Projects.dependencies_sets_kotlin_jvm))
  api(project(Projects.open_commons_concurrent))
  api(project(Projects.open_commons_guava))

  api(Libs.guava)
  compileOnlyApi(Libs.javax_annotation_api)
  api(KotlinX.coroutines.jdk8)
  api(KotlinX.serialization.json)
  api(KotlinX.serialization.protobuf)

  testImplementation(project(Projects.dependencies_sets_kotlin_test))
  testImplementation(project(Projects.open_commons_test_utils))
  testImplementation(project(Projects.open_commons_commons))
  testImplementation(Libs.awaitility)
  testImplementation(Libs.logback_classic)
}
