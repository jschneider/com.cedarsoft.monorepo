import Libs.commons_io

description = """Crypt related stuff"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlinJvm
}

dependencies {
  compileOnlyApi(project(Projects.dependencies_sets_jvm_annotations))
  api(project(Projects.dependencies_sets_jvm_kotlin))

  api(Libs.guava)
  api(Libs.commons_codec)
  api(commons_io)

  testImplementation(project(Projects.dependencies_sets_jvm_test_basics))
}
