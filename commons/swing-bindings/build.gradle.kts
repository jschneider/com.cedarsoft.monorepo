import Libs.commons_io

description = """Swing bindings"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlinJvm
}

dependencies {
  compileOnlyApi(project(Projects.dependencies_sets_annotations))
  api(project(Projects.dependencies_sets_kotlin_jvm))
  compileOnlyApi(project(Projects.open_annotations))
  api(project(Projects.open_commons_xml_commons))
  api(project(Projects.open_commons_version))

  api(Libs.guava)
  api(commons_io)
  api(Libs.commons_logging)
  api(Libs.log4j)

  testImplementation(Libs.miglayout_swing)
  testImplementation(project(Projects.open_commons_test_utils))
  testImplementation(Libs.commons_codec)
}
