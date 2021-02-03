description = """File Container related stuff"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlinJvm
}

dependencies {
  compileOnlyApi(project(Projects.dependencies_sets_annotations))
  api(project(Projects.dependencies_sets_kotlin))
  api(project(Projects.open_commons_version))
  api(project(Projects.open_commons_io))

  api(Libs.guava)
  api(Libs.commonsCodec)
  api(Libs.commonsCompress)
  api(Libs.commonsIo)

  testImplementation(project(Projects.dependencies_sets_test_basics))
}