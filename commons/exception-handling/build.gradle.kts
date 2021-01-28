description = """Exception Handling stuff"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlinJvm
}

dependencies {
  api(project(Projects.open_unit_unit))
  compileOnlyApi(project(Projects.open_annotations))
  api(project(Projects.open_commons_exceptions))
  api(project(Projects.open_commons_swing_common))
  api(Libs.miglayoutSwing)
  api(Libs.miglayoutJavafx)
  api(project(Projects.open_commons_version))
  api(project(Projects.open_commons_javafx))
  api(Libs.controlsfx)
  api(Libs.fontAwesomeFx)
  api(Libs.slf4jApi)
  api(Libs.guava)
  api(Libs.balloontip)
  compileOnlyApi(Libs.comIntellijAnnotations)


  testImplementation(project(Projects.open_commons_test_utils))
  testImplementation(Libs.testfxJunit5)
}
