description = """JavaFX Test Utils"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlin("jvm")
}

dependencies {
  api(project(":dependencies-sets:annotations"))
  api(project(":open:annotations:annotations"))

  api(project(":open:commons:javafx"))
  api("com.google.guava:guava")
  api(project(":open:unit:unit"))
  api(project(":open:commons:concurrent"))
  api("org.fxyz3d:fxyz3d:0.3.0")
  api("org.testfx:testfx-junit5")


  api(project(":dependencies-sets:kotlin-test"))
  api(project(":open:commons:test-utils"))
}
