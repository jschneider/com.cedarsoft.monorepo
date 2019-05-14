description = """Concurrent stuff"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlin("jvm")
}

dependencies {
  api(project(":dependencies-sets:annotations"))
  api(project(":open:annotations:annotations"))
  api(project(":open:unit:unit"))
  api(project(":open:commons:exceptions"))

  api("com.google.guava:guava")

  api("javax.annotation:javax.annotation-api")

  testImplementation(project(":dependencies-sets:kotlin-test"))
  testImplementation(project(":open:commons:test-utils"))
  testImplementation(project(":open:commons:commons"))
  testImplementation("org.awaitility:awaitility")
}
