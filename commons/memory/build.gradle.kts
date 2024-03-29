plugins {
  `java-library`
  kotlinJvm
}

dependencies {
  compileOnlyApi(project(Projects.dependencies_sets_jvm_annotations))
  api(project(Projects.dependencies_sets_jvm_kotlin))

  api(Libs.guava)

  //Test stuff
  testImplementation(project(Projects.dependencies_sets_jvm_test_basics))
  testImplementation(project(Projects.dependencies_sets_jvm_kotlin_test))
}
