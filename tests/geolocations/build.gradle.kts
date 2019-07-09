plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlin("jvm")
}

dependencies {
  api(project(":dependencies-sets:kotlin"))
  api("io.reactivex.rxjava2:rxjava:2.2.8")
  api("org.slf4j:slf4j-api:1.7.25")
  api("com.google.guava:guava:27.1-jre")
  implementation("ch.qos.logback:logback-classic:1.2.3")

  testImplementation(project(":dependencies-sets:kotlin-test"))
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.4.0")
  testImplementation("org.junit.jupiter:junit-jupiter-engine:5.4.0")
  implementation(kotlin("stdlib-jdk8"))
}
repositories {
  mavenCentral()
}
