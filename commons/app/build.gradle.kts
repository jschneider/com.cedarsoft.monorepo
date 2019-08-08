description = """App stuff"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlin("jvm")
}

dependencies {
  api(project(Projects.dependencies_sets_annotations))
  api(project(Projects.dependencies_sets_kotlin))
  api(project(Projects.open_annotations))
  api(project(Projects.open_commons_xml_commons))
  api(project(Projects.open_commons_version))

  api(Libs.guava)
  api(Libs.commons_io)
  api(Libs.commons_logging)
  api(Libs.log4j)

  testImplementation(project(Projects.dependencies_sets_test_basics))
  testImplementation(project(Projects.open_commons_test_utils))
  testImplementation(Libs.commons_codec)
}
