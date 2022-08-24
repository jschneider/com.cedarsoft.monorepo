description = """Photos"""

group = "com.cedarsoft.photos"

plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlinJvm
}

dependencies {
  //
  // Compile Deps
  //
  api(project(Projects.dependencies_sets_jvm_kotlin))
  api(Libs.guice)
  api(project(Projects.open_commons_io))
  api(project(Projects.open_commons_image))
  api(project(Projects.open_commons_file))
  api(project(Projects.open_commons_app))
  api(project(Projects.open_commons_execution))
  api(project(Projects.open_commons_crypt))
  api(project(Projects.open_commons_exceptions))
  api(Libs.im4java)
  api(Libs.jsr305)
  api(Libs.javax_inject)
  compileOnlyApi(project(Projects.open_annotations))


  //
  // Test Deps
  //
  testImplementation(Libs.commons_lang3)
  testImplementation(project(Projects.open_commons_test_utils))
}
