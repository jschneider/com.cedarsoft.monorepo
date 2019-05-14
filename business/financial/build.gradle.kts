description = """Business - Financial"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlin("jvm")
}

dependencies {
  //
  // Compile Deps
  //
  api("joda-time:joda-time")

  //
  // Compile Deps
  //
  api(project(":open:commons:commons"))
  api("joda-time:joda-time")
  testImplementation(project(":open:commons:test-utils"))
}
