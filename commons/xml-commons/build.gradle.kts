/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java Library project to get you started.
 * For more details take a look at the Java Libraries chapter in the Gradle
 * User Manual available at https://docs.gradle.org/5.2.1/userguide/java_library_plugin.html
 */

plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
}

dependencies {
  api(project(":dependencies-sets:annotations"))

  api("commons-io:commons-io")
  api("commons-codec:commons-codec")
  api("com.google.guava:guava")

  //Test stuff
  testImplementation(project(":dependencies-sets:kotlin-test"))
}
