description = """App stuff"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlinJvm
}

dependencies {
  compileOnlyApi(project(Projects.dependencies_sets_jvm_annotations))
  api(project(Projects.dependencies_sets_jvm_kotlin))
  compileOnlyApi(project(Projects.open_annotations))
  api(project(Projects.open_commons_kotlin_lang))
  api(project(Projects.open_commons_xml_commons))
  api(project(Projects.open_commons_version))

  api(Libs.guava)
  api(Libs.commons_io)
  api(Libs.commons_logging)
  api(Libs.log4j)

  testImplementation(project(Projects.dependencies_sets_jvm_test_basics))
  testImplementation(project(Projects.open_commons_test_utils))
  testImplementation(Libs.commons_codec)
}
