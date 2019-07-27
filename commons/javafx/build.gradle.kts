description = """JavaFX"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlin("jvm")
}

dependencies {
  api(project(":dependencies-sets:kotlin"))

  api(project(":open:annotations:annotations"))

  api(project(":open:commons:concurrent"))
  api(project(":open:unit:unit"))

  api("com.google.guava:guava")

  api(project(":open:commons:time"))
  api(project(":open:unit:unit"))
  api(project(":open:commons:concurrent"))

  testImplementation(project(":dependencies-sets:kotlin-test"))
  testImplementation(project(":open:commons:test-utils"))
  testImplementation("com.miglayout:miglayout-javafx")
  testImplementation("org.jfxtras:jfxtras-all")
  testImplementation("org.jfree:fxgraphics2d")
  testImplementation("com.jfoenix:jfoenix")
  testImplementation("de.jensd:fontawesomefx")
  testImplementation("org.controlsfx:controlsfx")
  testImplementation("eu.hansolo:tilesfx")
  testImplementation("eu.mihosoft.vrl.workflow:vworkflows-fx")
}
