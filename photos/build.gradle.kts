description = """Photos"""


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
  api("com.google.inject:guice")
  api(project(":open:commons:io"))
  api(project(":open:commons:image"))
  api(project(":open:commons:file"))
  api(project(":open:commons:app"))
  api(project(":open:commons:execution"))
  api(project(":open:commons:crypt"))
  api(project(":open:commons:exceptions"))
  api("org.im4java:im4java")
  api("com.google.code.findbugs:jsr305")
  api("javax.inject:javax.inject")
  api(project(":open:annotations:annotations"))


  //
  // Test Deps
  //
  testImplementation("org.apache.commons:commons-lang3")
  testImplementation("org.easymock:easymock")
  testImplementation("org.mockito:mockito-core")
  testImplementation(project(":open:commons:test-utils"))
}
