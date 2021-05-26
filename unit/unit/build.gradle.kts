import Libs.commons_io

description = """Unit"""

group = "com.cedarsoft"

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
      }
    }
    //commonTest {
    //  dependencies {
    //    implementation(Libs.kotlin_test_common)
    //    implementation(Libs.kotlin_test_annotations_common)
    //  }
    //}

    named("jvmMain") {
      dependencies {
        api(project(Projects.dependencies_sets_kotlin_jvm))
        api(project(Projects.open_annotations))
      }
    }

    named("jvmTest") {
      dependencies {
        api(project(Projects.dependencies_sets_kotlin_jvm))

        implementation(Libs.kotlin_test)
        implementation(Libs.kotlin_test_junit)

        implementation(project(Projects.dependencies_sets_kotlin_test))
        implementation(project(Projects.open_commons_test_utils))

        implementation(commons_io)
        implementation(Libs.assertj_core)

      }
    }
    named("jsMain") {
      dependencies {
        api(project(Projects.dependencies_sets_kotlin_js))
      }
    }
    //named("jsTest") {
    //  dependencies {
    //    implementation(Libs.kotlin_test_js)
    //  }
    //}
  }
}
