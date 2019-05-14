description = """Exception Handling stuff"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlin("jvm")
}

dependencies {
  api(project(":open:unit:unit"))
  api(project(":open:annotations:annotations"))
  api(project(":open:commons:exceptions"))
  api(project(":open:commons:swing:common"))
  api("com.miglayout:miglayout-swing")
  api("com.miglayout:miglayout-javafx")
  api(project(":open:commons:version"))
  api(project(":open:commons:javafx"))
  api("org.controlsfx:controlsfx")
  api("de.jensd:fontawesomefx")
  api("org.slf4j:slf4j-api")
  api("com.google.guava:guava")
  api("net.java.balloontip:balloontip")
  api("com.intellij:annotations")


  testImplementation(project(":open:commons:test-utils"))
  testImplementation("org.testfx:testfx-junit5")
}
