description = """Serialization - Stax Mate"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlinJvm
}

dependencies {
  //
  // Compile Deps
  //
  api(project(Projects.open_serialization_serialization))
  api(project(Projects.open_serialization_stax_mate))
  api(Libs.guava)
  api(Libs.neo4j)
  api(Libs.perf4j)
  api(Libs.speed4j)
  api(project(Projects.open_commons_xml_commons))


  //
  // Test Deps
  //
  testImplementation(project(Projects.open_serialization_serialization_test_utils))
  testImplementation(project(Projects.open_commons_test_utils))

  testImplementation(Libs.neo4jKernel)
  testImplementation(Libs.neo4jHarness)
  testImplementation(Libs.orgNeo4jCommunityItTestSupport)
}
