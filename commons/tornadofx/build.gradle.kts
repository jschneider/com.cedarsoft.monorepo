description = """TornadoFX Extensions"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlin("jvm")
}

dependencies {
  api(project(Projects.dependencies_sets_kotlin))
  api(project(Projects.open_annotations))

  api(project(Projects.open_commons_javafx))
  api(Libs.com_google_guava_guava)
  api(Libs.miglayout_javafx)
  api(Libs.tornadofx)
  api(Libs.org_slf4j_slf4j_api)

  api(project(Projects.open_commons_time))

  testImplementation(project(Projects.dependencies_sets_kotlin_test))
  testImplementation(project(Projects.open_commons_test_utils))
  testImplementation(project(Projects.open_commons_javafx_test_utils))
  testImplementation(Libs.controlsfx)
}
