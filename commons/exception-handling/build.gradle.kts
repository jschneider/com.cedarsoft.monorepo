description = """Exception Handling stuff"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlin("jvm")
}

dependencies {
  api(project(Projects.open_unit_unit))
  api(project(Projects.open_annotations))
  api(project(Projects.open_commons_exceptions))
  api(project(Projects.open_commons_swing_common))
  api(Libs.miglayout_swing)
  api(Libs.miglayout_javafx)
  api(project(Projects.open_commons_version))
  api(project(Projects.open_commons_javafx))
  api(Libs.controlsfx)
  api(Libs.fontawesomefx)
  api(Libs.org_slf4j_slf4j_api)
  api(Libs.com_google_guava_guava)
  api(Libs.balloontip)
  api(Libs.com_intellij_annotations)


  testImplementation(project(Projects.open_commons_test_utils))
  testImplementation(Libs.testfx_junit5)
}
