@file:Suppress("UNUSED_VARIABLE")
description = """Kotlinx Collections - Multiplatform"""

plugins {
  kotlinMultiPlatform
}

repositories {
  mavenCentral()
}

kotlin {
  jvm()
  js {
    browser {
    }
  }

  sourceSets {
    val commonMain by getting {
      dependencies {
        api(Kotlin.stdlib.common)
        api(project(Projects.open_commons_kotlin_lang))
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
        api(project(Projects.dependencies_sets_jvm_annotations))
        api(project(Projects.dependencies_sets_jvm_kotlin))
        api(project(Projects.open_annotations))
      }
    }

    jvm().compilations["test"].defaultSourceSet {
      dependencies {
        implementation(Libs.kotlin_test)
        implementation(Libs.kotlin_test_junit)

        implementation(project(Projects.dependencies_sets_jvm_kotlin_test))
        implementation(project(Projects.open_commons_test_utils))
        implementation(project(Projects.open_commons_javafx_test_utils))
      }
    }
  }
}

detekt {
  //Contains auto imported code, ignore detekt
  source = files()
}
