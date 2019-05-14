description = """Exceptions stuff"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlin("jvm")
}

dependencies {
  api(project(":dependencies-sets:annotations"))

  testImplementation(project(":dependencies-sets:test-basics"))
  testImplementation(project(":open:commons:test-utils"))
  testImplementation("commons-codec:commons-codec")
}
