description = """Swing JGoodies Extensions"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlinJvm
}

dependencies {
  compileOnlyApi(project(Projects.dependencies_sets_annotations))
  compileOnlyApi(project(Projects.open_annotations))

  api(Libs.jgoodiesBinding)
  api(Libs.jgoodiesValidation)
  api(Libs.glazedlistsJava16)
  api(Libs.miglayoutSwing)

  //
  // Test Deps
  //
  testImplementation(project(Projects.open_commons_test_utils))
}
