plugins {
  `java-library`
  kotlinJvm
}

dependencies {
  compileOnlyApi(project(Projects.dependencies_sets_jvm_annotations))
  api(project(Projects.dependencies_sets_jvm_kotlin))
  api(Libs.slf4j_api)
  api(Libs.logback_classic)

  //Test stuff
  testImplementation(project(Projects.dependencies_sets_jvm_test_basics))
  testImplementation(project(Projects.dependencies_sets_jvm_kotlin_test))
}
