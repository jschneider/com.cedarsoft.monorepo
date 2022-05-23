import Libs.commons_io

plugins {
}

dependencies {
  compileOnlyApi(project(Projects.dependencies_sets_jvm_annotations))
  api(commons_io)
  api(Libs.commons_compress)

  testImplementation(project(Projects.dependencies_sets_jvm_test_basics))
  testImplementation(project(Projects.open_commons_test_utils))
}
