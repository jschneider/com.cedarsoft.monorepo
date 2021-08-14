@file:Suppress("UNUSED_VARIABLE")
description = """Concurrent stuff - with coroutines"""

plugins {
  kotlinMultiPlatform
}

repositories {
  mavenCentral()
}

kotlin {
  jvm()
  js {
    nodejs() {
    }
  }

  sourceSets {
    val commonMain by getting {
      dependencies {
        api(Kotlin.stdlib.common)
        api(KotlinX.coroutines.core)
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
        api(project(Projects.open_annotations))

        api(project(Projects.dependencies_sets_kotlin_jvm))
        api(project(Projects.open_annotations))
        api(project(Projects.open_unit_unit))
        api(project(Projects.open_commons_concurrent))

        api(KotlinX.coroutines.jdk8)
      }
    }

    jvm().compilations["test"].defaultSourceSet {
      dependencies {
        implementation(project(Projects.open_commons_commons))
        implementation(Libs.awaitility)
        implementation(Libs.logback_classic)

        implementation(Libs.kotlin_test)
        implementation(Libs.kotlin_test_junit)

        implementation(project(Projects.dependencies_sets_kotlin_test))
        implementation(project(Projects.open_commons_test_utils))
        implementation(project(Projects.open_commons_javafx_test_utils))

        implementation(Libs.commons_io)
        implementation(Libs.assertj_core)

      }
    }
  }
}
