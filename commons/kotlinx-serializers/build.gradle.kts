@file:Suppress("UNUSED_VARIABLE")
description = """Kotlinx Serialization serializers"""


plugins {
  kotlinMultiPlatform
  kotlinxSerialization
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
        api(KotlinX.serialization.core)
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
        api(Libs.javax_annotation_api)

        api(project(Projects.dependencies_sets_jvm_annotations))
        api(project(Projects.dependencies_sets_jvm_kotlin))
        api(project(Projects.open_annotations))

        api(project(Projects.open_commons_concurrent))

        api(KotlinX.coroutines.jdk8)
        api(KotlinX.serialization.json)
      }
    }

    jvm().compilations["test"].defaultSourceSet {
      dependencies {
        implementation(project(Projects.dependencies_sets_jvm_kotlin_test))
        implementation(project(Projects.open_commons_test_utils))
        implementation(project(Projects.open_commons_commons))
        implementation(project(Projects.open_commons_kotlinx_serialization_test_utils))

        implementation(Libs.awaitility)
        implementation(Libs.logback_classic)

        implementation(Libs.kotlin_test)
        implementation(Libs.kotlin_test_junit)
      }
    }
  }
}


