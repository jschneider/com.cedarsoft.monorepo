description = """Swing JGoodies Extensions"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlin("jvm")
}

dependencies {
  api(project(":dependencies-sets:annotations"))
  api(project(":open:annotations:annotations"))

  api("com.jgoodies:jgoodies-binding")
  api("com.jgoodies:jgoodies-validation")
  api("net.java.dev.glazedlists:glazedlists_java16")
  api("com.miglayout:miglayout-swing")

  //
  // Test Deps
  //
  testImplementation(project(":open:commons:test-utils"))
}
