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
        api(project(Projects.dependencies_sets_annotations))
        api(project(Projects.dependencies_sets_kotlin))
        api(project(Projects.open_annotations))
      }
    }

    named("jvmTest") {
      dependencies {
        implementation(Libs.kotlinTest)
        implementation(Libs.kotlinTestJunit)

        implementation(project(Projects.dependencies_sets_kotlin_test))
        implementation(project(Projects.open_commons_test_utils))

        implementation(Libs.commonsIo)
        implementation(Libs.assertjCore)

      }
    }
    named("jsMain") {
      dependencies {
      }
    }
    //named("jsTest") {
    //  dependencies {
    //    implementation(Libs.kotlin_test_js)
    //  }
    //}
  }
}
