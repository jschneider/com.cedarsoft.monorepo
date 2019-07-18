/*
 * This file was generated by the Gradle 'init' task.
 *
 * The settings file is used to specify which projects to include in your build.
 *
 * Detailed information about configuring a multi-project build in Gradle can be found
 * in the user manual at https://docs.gradle.org/5.2.1/userguide/multi_project_builds.html
 */

rootProject.name = "com.cedarsoft.tests.gradle-deployment"

enableFeaturePreview("GRADLE_METADATA")


pluginManagement {
  repositories {
    jcenter()
    mavenCentral()
    google()
    gradlePluginPortal()
    maven {
      name = "Kotlin EAP (for kotlin-frontend-plugin)"
      url = uri("https://dl.bintray.com/kotlin/kotlin-eap")
    }
  }
  resolutionStrategy {
    eachPlugin {
      when (requested.id.id) {
        "org.jetbrains.kotlin.frontend" -> useModule("org.jetbrains.kotlin:kotlin-frontend-plugin:${requested.version}")
        "kotlinx-serialization"         -> useModule("org.jetbrains.kotlin:kotlin-serialization:${requested.version}")
      }
    }
  }
}

