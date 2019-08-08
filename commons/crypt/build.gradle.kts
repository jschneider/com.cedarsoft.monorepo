description = """Crypt related stuff"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlin("jvm")
}

dependencies {
  api(project(Projects.dependencies_sets_annotations))
  api(project(Projects.dependencies_sets_kotlin))

  api(Libs.guava)
  api(Libs.commons_codec)
  api(Libs.commons_io)

  testImplementation(project(Projects.dependencies_sets_test_basics))
}
