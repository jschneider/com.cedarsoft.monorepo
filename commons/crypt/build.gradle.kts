description = """Crypt related stuff"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlin("jvm")
}

dependencies {
  api(project(":dependencies-sets:annotations"))
  api(project(":dependencies-sets:kotlin"))

  api("com.google.guava:guava")
  api("commons-codec:commons-codec")
  api("commons-io:commons-io")

  testImplementation(project(":dependencies-sets:test-basics"))
}
