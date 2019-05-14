description = """Serialzation - Jackson Common Serializers"""


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
  api(project(":open:serialization:jackson"))
  api("commons-io:commons-io")
  api(project(":open:commons:xml-commons"))

  api(project(":open:commons:app"))
  api(project(":open:commons:license"))
  api(project(":open:commons:file"))
  api(project(":open:commons:crypt"))
  api("joda-time:joda-time")

  //
  // Test Deps
  //
  testImplementation(project(":open:serialization:serialization-test-utils"))
  testImplementation(project(":open:commons:test-utils"))
}
