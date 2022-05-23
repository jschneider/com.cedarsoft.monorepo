description = """Exceptions stuff"""


plugins {
  kotlinMultiPlatform
  kotlinxSerialization
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
        api(project(Projects.open_commons_i18n))
        compileOnly(project(Projects.open_unit_unit))
        implementation(KotlinX.serialization.core)
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
        implementation(project(Projects.open_commons_kotlinx_serialization_test_utils))

        implementation(Libs.commons_io)
        implementation(Libs.assertj_core)
        implementation(Libs.commons_codec)
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
