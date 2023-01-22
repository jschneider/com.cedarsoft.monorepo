plugins {
  kotlinxSerialization
  kotlinMultiPlatform
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
        implementation(project(Projects.open_commons_concurrent_coroutines))
        implementation(project(Projects.open_commons_feature_flags))
        implementation(KotlinX.coroutines.core)

        implementation(project(Projects.open_commons_i18n))
        implementation(project(Projects.open_commons_uuid))
        implementation(project(Projects.open_commons_kotlin_lang))
        implementation(project(Projects.open_commons_kotlin_collections))

        implementation(project(Projects.open_commons_logging))

        implementation(Libs.uuid)
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
        api(project(Projects.dependencies_sets_js_kotlin))
        implementation(project(Projects.open_commons_kotlin_react))

        //BOM for dependencies

        implementation(Libs.kotlin_react)
        implementation(Libs.kotlin_react_dom)
        implementation(Libs.kotlin_styled)

        implementation(Libs.kotlin_redux)
        implementation(Libs.kotlin_react_redux)

        implementation(npm("react", "_"))
      }
    }
    js().compilations["test"].defaultSourceSet {
      dependencies {
        implementation(Libs.kotlin_test_js)
      }
    }

    jvm().compilations["main"].defaultSourceSet {
      dependencies {
        //implementation(Libs.kotlinx_coroutines_core)

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

dependencies {
  // Workaround since Kotlin Multiplatform does not support enforcedPlatform
  // https://youtrack.jetbrains.com/issue/KT-40489
  "jsMainImplementation"(enforcedPlatform(Libs.kotlin_wrappers_bom))
}



tasks.withType<GenerateModuleMetadata> {
  suppressedValidationErrors.add("enforced-platform")
}

