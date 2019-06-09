description = """Ktor Server Commons"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlin("jvm")
  id("kotlinx-serialization")
}

dependencies {
  api(project(":dependencies-sets:ktor"))
  api(project(":open:commons:concurrent"))
  api(project(":open:commons:guava"))
  api(project(":open:commons:kotlinx-serializers"))


  api("com.google.guava:guava")
  api("javax.annotation:javax.annotation-api")
  api("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8")
  api("org.jetbrains.kotlinx:kotlinx-serialization-runtime")

  testImplementation(project(":dependencies-sets:kotlin-test"))
  testImplementation(project(":open:commons:test-utils"))
  testImplementation(project(":open:commons:commons"))
  testImplementation("org.awaitility:awaitility")
  testImplementation("ch.qos.logback:logback-classic")
  testImplementation("io.ktor:ktor-server-test-host")
}
