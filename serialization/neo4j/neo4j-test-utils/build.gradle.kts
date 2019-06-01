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
  api(project(":open:serialization:neo4j:neo4j"))
  api(project(":open:serialization:serialization-test-utils"))
  api(project(":open:commons:test-utils"))
  api("org.junit.jupiter:junit-jupiter-api")
  api("junit:junit")
  api("com.google.guava:guava")
  api("org.neo4j:neo4j-kernel")
  api("org.neo4j.community:it-test-support")
  //api("org.neo4j:neo4j-shell")
  api("org.mockito:mockito-core")
  api("org.easymock:easymockclassextension")
  api("org.easymock:easymock")
  api("org.assertj:assertj-core")
  api("org.easytesting:fest-reflect")

  //
  // Test Deps
  //
  testImplementation("org.neo4j:neo4j-io")
}
