@file:Suppress("UNUSED_VARIABLE")

description = """Logging"""

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
      configureJsKarma()
    }
  }

  sourceSets {
    val commonMain by getting {
      dependencies {
        api(project(Projects.open_unit_unit))
        api(project(Projects.open_commons_kotlin_lang))
      }
    }

    val commonTest by getting {
      dependencies {
        implementation(Libs.kotlin_test_common)
        implementation(Libs.kotlin_test_annotations_common)
      }
    }

    js().compilations["main"].defaultSourceSet {
      dependencies {
        api(Libs.kotlin_js)
        implementation(project(Projects.open_commons_kotlin_js))
      }
    }

    js().compilations["test"].defaultSourceSet {
      dependencies {
        implementation(project(Projects.dependencies_sets_js_kotlin_test))
        implementation(Libs.kotlin_test_js)
      }
    }

    jvm().compilations["main"].defaultSourceSet {
      dependencies {
        implementation(Libs.kotlin_reflect)
        api(project(Projects.open_annotations))

        compileOnly(project(Projects.dependencies_sets_jvm_annotations))

        api(project(Projects.dependencies_sets_jvm_kotlin))
        api(Libs.slf4j_api)
        api(Libs.logback_classic)

        //Compile only - for [ApplicationHomeLogbackConfigurer]
        compileOnly(project(Projects.open_commons_app))
        compileOnly(project(Projects.open_commons_io))
      }
    }

    jvm().compilations["test"].defaultSourceSet {
      dependencies {
        implementation(Libs.kotlin_test)
        implementation(Libs.kotlin_test_junit)

        implementation(project(Projects.dependencies_sets_jvm_test_basics))
        implementation(project(Projects.dependencies_sets_jvm_kotlin_test))
        implementation(project(Projects.open_commons_test_utils))
        implementation(project(Projects.open_commons_javafx_test_utils))
      }
    }
  }
}
