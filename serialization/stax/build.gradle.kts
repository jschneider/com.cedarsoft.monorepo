description = """Serialization - Stax"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlin("jvm")
}

dependencies {
  //
  // Compile Deps
  //
  api(project(":open:serialization:serialization"))
  api("org.codehaus.jettison:jettison")


  //
  // Test Deps
  //
  testImplementation(project(":open:serialization:serialization-test-utils"))
  testImplementation(project(":open:commons:test-utils"))
}
