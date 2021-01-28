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
  api(project(Projects.open_serialization_neo4j))
  api(project(Projects.open_serialization_serialization_test_utils))
  api(project(Projects.open_commons_test_utils))
  api(Libs.junitJupiterApi)
  api(Libs.junit)
  api(Libs.guava)
  api(Libs.neo4jKernel)
  api(Libs.orgNeo4jCommunityItTestSupport)
  //api("org.neo4j:neo4j-shell")
  api(Libs.mockitoCore)
  api(Libs.easyMockClassExtension)
  api(Libs.easymock)
  api(Libs.assertjCore)
  api(Libs.festReflect)

  //
  // Test Deps
  //
  testImplementation(Libs.neo4jIo)
}
