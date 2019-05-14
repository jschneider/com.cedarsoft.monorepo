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
  api(project(":open:serialization:stax"))
  api("org.codehaus.staxmate:staxmate")
  api("org.codehaus.woodstox:woodstox-core-asl")
  api("org.apache.commons:commons-lang3")
  api("org.codehaus.jettison:jettison")


  //
  // Test Deps
  //
  testImplementation(project(":open:serialization:serialization-test-utils"))
  testImplementation(project(":open:commons:test-utils"))
}
