description = """Swing Common"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlinJvm
}

dependencies {
  compileOnlyApi(project(Projects.dependencies_sets_annotations))

  api(Libs.slf4jApi)
  api(Libs.miglayoutSwing)
  compileOnlyApi(Libs.comIntellijAnnotations)
  api(Libs.jideOss)
  api(Libs.guava)
  compileOnlyApi(project(Projects.open_annotations))
  api(project(Projects.open_unit_unit))

  //
  // Test Deps
  //
  testImplementation(project(Projects.open_commons_test_utils))
}
