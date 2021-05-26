description = """Exception Handling stuff"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlinJvm
}

dependencies {
  compileOnlyApi(project(Projects.open_annotations))
  compileOnlyApi(Libs.annotations)

  api(project(Projects.open_unit_unit))
  api(project(Projects.open_commons_exception_handling))


  api(project(Projects.open_commons_swing_common))

  api(Libs.miglayout_swing)
  api(Libs.miglayout_javafx)
  api(project(Projects.open_commons_version))
  api(project(Projects.open_commons_javafx))

  api(Libs.controlsfx)
  api(Libs.fontawesomefx)
  api(Libs.slf4j_api)
  api(Libs.guava)
  api(Libs.balloontip)


  testImplementation(project(Projects.open_commons_test_utils))
  testImplementation(Libs.testfx_junit5)
}
