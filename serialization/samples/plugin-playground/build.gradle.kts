description = "Serialization Plugin Playground"

repositories {
  mavenCentral()
}

plugins {
  kotlin("jvm") version "1.4.32"
}

dependencies {
  api("com.google.code.findbugs:jsr305:3.0.2")
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.1")
  testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.23.1")

}
