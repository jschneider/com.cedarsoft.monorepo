description = """Serialization - Stax Mate"""


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
  api("com.google.guava:guava")
  api("org.neo4j:neo4j")
  api("org.perf4j:perf4j")
  api("com.ecyrd.speed4j:speed4j")
  api(project(":open:commons:xml-commons"))


  //
  // Test Deps
  //
  testImplementation(project(":open:serialization:serialization-test-utils"))
  testImplementation(project(":open:commons:test-utils"))
  testImplementation("org.neo4j:neo4j-kernel")
  testImplementation("org.neo4j.test:neo4j-harness")
  testImplementation("org.neo4j.community:it-test-support")
  testImplementation("org.neo4j:neo4j-shell")
  testImplementation("org.neo4j:neo4j-io")
}
