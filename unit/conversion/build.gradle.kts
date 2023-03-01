import Libs.commons_io

description = """Unit Conversions"""

group = "it.neckar.open.unit"

plugins {
  // Apply the java-library plugin to add support for Java Library
  kotlinMultiPlatform
  //dokka
}

kotlin {
  jvm {
  }

  js {
    nodejs()
  }

  sourceSets {
    commonMain {
      dependencies {
        api(Kotlin.stdlib.common)
        api(project(Projects.open_unit_unit))
      }
    }

    named("jvmMain") {
      dependencies {
        api(project(Projects.dependencies_sets_jvm_kotlin))
        api(project(Projects.open_annotations))
      }
    }

    named("jvmTest") {
      dependencies {
        api(project(Projects.dependencies_sets_jvm_kotlin))

        implementation(Libs.kotlin_test)
        implementation(Libs.kotlin_test_junit)

        implementation(project(Projects.dependencies_sets_jvm_kotlin_test))
        implementation(project(Projects.open_commons_test_utils))

        implementation(commons_io)
        implementation(Libs.assertj_core)

      }
    }
    named("jsMain") {
      dependencies {
        api(project(Projects.dependencies_sets_js_kotlin))
      }
    }
    //named("jsTest") {
    //  dependencies {
    //    implementation(Libs.kotlin_test_js)
    //  }
    //}
  }
}
