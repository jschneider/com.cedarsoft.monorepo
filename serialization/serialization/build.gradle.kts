description = """Serialization"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlinJvm
}

dependencies {
  //
  // Compile Deps
  //
  api(project(Projects.dependencies_sets_jvm_kotlin))
  compileOnlyApi(project(Projects.dependencies_sets_jvm_annotations))
  api(project(Projects.open_commons_version))

  //
  // Test Deps
  //
  testImplementation(Libs.guava)
  testImplementation(project(Projects.open_commons_test_utils))
}
