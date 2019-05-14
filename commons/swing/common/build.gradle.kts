description = """Swing Common"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlin("jvm")
}

dependencies {
  api(project(":dependencies-sets:annotations"))

  api("org.slf4j:slf4j-api")
  api("com.miglayout:miglayout-swing")
  api("com.intellij:annotations")
  api("com.jidesoft:jide-oss")
  api("com.google.guava:guava")
  api(project(":open:annotations:annotations"))
  api(project(":open:unit:unit"))

  //
  // Test Deps
  //
  testImplementation(project(":open:commons:test-utils"))
}
