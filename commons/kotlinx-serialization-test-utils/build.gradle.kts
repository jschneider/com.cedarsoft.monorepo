description = """Kotlinx Serialization serializers"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlinJvm
  kotlinxSerialization
}

dependencies {
  api(project(Projects.dependencies_sets_kotlin))
  api(project(Projects.open_commons_concurrent))
  api(project(Projects.open_commons_guava))

  compileOnlyApi(project(Projects.dependencies_sets_annotations))
  api(project(Projects.dependencies_sets_kotlin))
  api(project(Projects.dependencies_sets_test_basics))

  api(Libs.guava)
  compileOnlyApi(Libs.javax_annotation_api)
  api(Libs.kotlinx_coroutines_jdk8)
  api(Libs.kotlinx_serialization_json)
  api(Libs.kotlinx_serialization_protobuf)
  api(Libs.kotlinx_serialization_cbor)

  api(project(Projects.dependencies_sets_kotlin_test))
  api(project(Projects.open_commons_test_utils))
  api(project(Projects.open_commons_commons))
}
