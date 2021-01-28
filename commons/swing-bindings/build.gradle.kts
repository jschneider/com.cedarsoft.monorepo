description = """Swing bindings"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlinJvm
}

dependencies {
  compileOnlyApi(project(Projects.dependencies_sets_annotations))
  api(project(Projects.dependencies_sets_kotlin))
  compileOnlyApi(project(Projects.open_annotations))
  api(project(Projects.open_commons_xml_commons))
  api(project(Projects.open_commons_version))

  api(Libs.guava)
  api(Libs.commonsIo)
  api(Libs.commonsLogging)
  api(Libs.log4j)

  testImplementation(Libs.miglayoutSwing)
  testImplementation(project(Projects.open_commons_test_utils))
  testImplementation(Libs.commonsCodec)
}
