description = """TornadoFX Extensions"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlin("jvm")
}

dependencies {
  api(project(":dependencies-sets:kotlin"))
  api(project(":open:annotations:annotations"))

  api(project(":open:commons:javafxkt"))
  api("com.google.guava:guava")
  api("com.miglayout:miglayout-javafx")
  api("no.tornado:tornadofx")

  api(project(":open:commons:time"))

  testImplementation(project(":dependencies-sets:kotlin-test"))
  testImplementation(project(":open:commons:test-utils"))
  testImplementation(project(":open:commons:javafx-test-utils"))
  testImplementation("org.controlsfx:controlsfx")
}
