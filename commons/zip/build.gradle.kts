plugins {
}

dependencies {
  compileOnlyApi(project(Projects.dependencies_sets_annotations))
  api(Libs.commons_io)
  api(Libs.commons_compress)

  testImplementation(project(Projects.dependencies_sets_test_basics))
  testImplementation(project(Projects.open_commons_test_utils))
}
