description = """Swing Components"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlin("jvm")
}

dependencies {
  api(project(":dependencies-sets:annotations"))

  //
  // Compile Deps
  //
  api("com.google.guava:guava")
  api(project(":open:unit:unit"))

  //
  // Test Deps
  //
  testImplementation(project(":open:commons:test-utils"))
}
