description = """Serialization - Sample: Demo 1"""


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
  api(project(":open:serialization:stax-mate"))
  api("commons-io:commons-io")
  api(project(":open:commons:xml-commons"))
  api("com.thoughtworks.xstream:xstream")

  //
  // Test Deps
  //
  testImplementation(project(":open:serialization:serialization-test-utils"))
  testImplementation(project(":open:commons:test-utils"))
}
