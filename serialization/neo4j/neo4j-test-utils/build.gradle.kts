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
  api(project(Projects.open_serialization_neo4j))
  api(project(Projects.open_serialization_serialization_test_utils))
  api(project(Projects.open_commons_test_utils))
  api(Libs.junit_jupiter_api)
  api(Libs.junit)
  api(Libs.com_google_guava_guava)
  api(Libs.neo4j_kernel)
  api(Libs.org_neo4j_community_it_test_support)
  //api("org.neo4j:neo4j-shell")
  api(Libs.mockito_core)
  api(Libs.easymockclassextension)
  api(Libs.easymock)
  api(Libs.assertj_core)
  api(Libs.fest_reflect)

  //
  // Test Deps
  //
  testImplementation(Libs.neo4j_io)
}
