description = """TornadoFX Extensions"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlinJvm
}

dependencies {
  api(project(Projects.dependencies_sets_kotlin_jvm))
  //No dependency to dependencies_sets_tornadofx --> to avoid circular deps

  api(Libs.kotlin_reflect)

  compileOnlyApi(project(Projects.open_annotations))

  api(project(Projects.open_commons_javafx))
  api(Libs.guava)
  api(Libs.miglayout_javafx)
  api(Libs.tornadofx)
  api(Libs.slf4j_api)

  api(project(Projects.open_commons_time))

  testImplementation(project(Projects.dependencies_sets_kotlin_test))
  testImplementation(project(Projects.open_commons_test_utils))
  testImplementation(project(Projects.open_commons_javafx_test_utils))
  testImplementation(Libs.controlsfx)
}
