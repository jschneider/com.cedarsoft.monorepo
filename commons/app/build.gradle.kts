description = """App stuff"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlin("jvm")
}

dependencies {
  api(project(":dependencies-sets:annotations"))
  api(project(":dependencies-sets:kotlin"))
  api(project(":open:annotations:annotations"))
  api(project(":open:commons:xml-commons"))
  api(project(":open:commons:version"))

  api("com.google.guava:guava")
  api("commons-io:commons-io")
  api("commons-logging:commons-logging")
  api("log4j:log4j")

  testImplementation(project(":dependencies-sets:test-basics"))
  testImplementation(project(":open:commons:test-utils"))
  testImplementation("commons-codec:commons-codec")
}