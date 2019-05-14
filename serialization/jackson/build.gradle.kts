description = """Serialization - Jackson"""


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
  api("com.fasterxml.jackson.core:jackson-core")

  //
  // Test Deps
  //
  testImplementation(project(":open:serialization:serialization-test-utils"))
  testImplementation(project(":open:commons:test-utils"))
  testImplementation("com.fasterxml.jackson.core:jackson-databind")
  testImplementation("com.google.inject:guice")
}
