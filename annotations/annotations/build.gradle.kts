/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java Library project to get you started.
 * For more details take a look at the Java Libraries chapter in the Gradle
 * User Manual available at https://docs.gradle.org/5.2.1/userguide/java_library_plugin.html
 */

group = "com.cedarsoft"

plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
}

dependencies {
  compileOnlyApi(project(Projects.dependencies_sets_annotations))
  testImplementation(project(Projects.open_commons_test_utils))
}
