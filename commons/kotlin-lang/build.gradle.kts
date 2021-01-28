description = """Kotlinx Lang - Multiplatform"""

plugins {
  kotlinMultiPlatform
}

repositories {
  mavenCentral()
  jcenter()
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
        api(project(Projects.open_unit_unit))
      }
    }

    val commonTest by getting {
      dependencies {
        implementation(Libs.kotlinTestCommon)
        implementation(Libs.kotlinTestAnnotationsCommon)
      }
    }

    jvm().compilations["main"].defaultSourceSet {
      dependencies {
        api(project(Projects.open_annotations))
      }
    }

    jvm().compilations["test"].defaultSourceSet {
      dependencies {
        implementation(Libs.kotlinTest)
        implementation(Libs.kotlinTestJunit)

        implementation(project(Projects.dependencies_sets_kotlin_test))
        implementation(project(Projects.open_commons_test_utils))
        implementation(project(Projects.open_commons_javafx_test_utils))
      }
    }
  }
}
