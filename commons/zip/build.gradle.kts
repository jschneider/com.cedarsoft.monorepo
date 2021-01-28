plugins {
}

dependencies {
  compileOnlyApi(project(Projects.dependencies_sets_annotations))
  api(Libs.commonsIo)
  api(Libs.commonsCompress)

  testImplementation(project(Projects.dependencies_sets_test_basics))
  testImplementation(project(Projects.open_commons_test_utils))
}
