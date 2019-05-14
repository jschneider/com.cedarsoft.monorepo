description = """Unit Utils"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlin("jvm")
}

dependencies {
  api(project(":dependencies-sets:annotations"))
  api(project(":dependencies-sets:kotlin"))
  api(project(":open:annotations:annotations"))
  api(project(":open:unit:unit"))

  testImplementation(project(":dependencies-sets:kotlin-test"))
  testImplementation(project(":open:commons:test-utils"))
}
