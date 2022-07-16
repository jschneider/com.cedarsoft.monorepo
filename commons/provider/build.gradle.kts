@file:Suppress("UNUSED_VARIABLE")

plugins {
  kotlinMultiPlatform
  kotlinxSerialization
}

repositories {
  mavenCentral()
}

kotlin {
  jvm()
  js {
    browser {
      testTask {
        useKarma {
          useChromeHeadlessNoSandbox()
        }
      }
    }
  }

  sourceSets {
    val commonMain by getting {
      dependencies {
        api(Kotlin.stdlib.common)

        api(project(Projects.open_commons_kotlin_lang))
        api(project(Projects.open_commons_kotlin_collections))
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
      }
    }

    js().compilations["test"].defaultSourceSet {
      dependencies {
        implementation(project(Projects.dependencies_sets_js_kotlin_test))
      }
    }
  }
}
