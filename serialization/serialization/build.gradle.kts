description = """Serialization"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlin("jvm")
}

dependencies {
  //
  // Compile Deps
  //
  api(project(":dependencies-sets:kotlin"))
  api(project(":dependencies-sets:annotations"))
  api(project(":open:commons:version"))

  //
  // Test Deps
  //
  testImplementation("com.google.guava:guava")
  testImplementation(project(":open:commons:test-utils"))
}
