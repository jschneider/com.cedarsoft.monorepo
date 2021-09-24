@file:Suppress("UNUSED_VARIABLE")

import Libs.commons_io

description = """MPP I18n"""

plugins {
  kotlinMultiPlatform
  kotlinxSerialization
}

repositories {
  mavenCentral()
}

kotlin {
  jvm()
  js() {
    nodejs()
  }

  sourceSets {
    val commonMain by getting {
      dependencies {
        api(Kotlin.stdlib.common)
        implementation(KotlinX.serialization.core)
        implementation(project(Projects.open_commons_kotlin_collections))
      }
    }

    val commonTest by getting {
      dependencies {
        implementation(Libs.kotlin_test_common)
        implementation(Libs.kotlin_test_annotations_common)
      }
    }

    jvm().compilations["main"].defaultSourceSet {
      dependencies {
        api(Kotlin.stdlib.jdk8)
        //implementation(Libs.kotlinx_coroutines_core)

        api(project(Projects.dependencies_sets_annotations))
        api(project(Projects.dependencies_sets_kotlin_jvm))
        api(project(Projects.open_annotations))
        api(project(Projects.open_commons_time))
      }
    }

    jvm().compilations["test"].defaultSourceSet {
      dependencies {
        implementation(Libs.kotlin_test)
        implementation(Libs.kotlin_test_junit)

        implementation(project(Projects.dependencies_sets_kotlin_test))
        implementation(project(Projects.open_commons_kotlinx_serialization_test_utils))

        implementation(commons_io)
        implementation(Libs.mockito_core)
        implementation(Libs.assertj_core)
        implementation(Libs.commons_math3)
        implementation(Libs.mockito_kotlin)
        implementation(Libs.logback_classic)
        implementation(Libs.awaitility)
      }
    }

    js().compilations["main"].defaultSourceSet {
      dependencies {
        api(project(Projects.dependencies_sets_kotlin_js))
      }
    }
    js().compilations["test"].defaultSourceSet {
      dependencies {
        implementation(Libs.kotlin_test_js)
      }
    }
  }
}
