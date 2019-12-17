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
  kotlinJvm
}

dependencies {
  api(project(Projects.dependencies_sets_annotations))
  api(project(Projects.dependencies_sets_kotlin))
  api(project(Projects.dependencies_sets_test_basics))

  api(project(Projects.open_commons_crypt))
  api(project(Projects.open_commons_xml_commons))

  api(Libs.guava)
  api(Libs.commons_codec)
  api(Libs.commons_io)

  api(Libs.staxmate)
  api(Libs.woodstox_core_asl)
  api(Libs.commons_lang3)
  api(Libs.commons_io)
  api(Libs.mockito_core)
  api(Libs.easymockclassextension)
  api(Libs.easymock)
  api(Libs.assertj_core)
  api(Libs.fest_reflect)
  api(Libs.jackson_databind)
  api(Libs.xmlunit)
  api(Libs.awaitility)

  //Test stuff
  testImplementation(project(Projects.dependencies_sets_kotlin_test))
}
