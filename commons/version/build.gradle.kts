plugins {
  kotlinJvm
}

dependencies {
  api(project(Projects.dependencies_sets_kotlin))
  testImplementation(project(Projects.dependencies_sets_test_basics))
}
