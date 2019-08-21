description = """Unit"""

group = "com.cedarsoft"

plugins {
  // Apply the java-library plugin to add support for Java Library
  kotlinMultiPlatform
  //dokka //TODO add dokka as soon as 0.9.19 is released
}

kotlin {
  jvm() {
    mavenPublication {
      //artifact("dokkaJar") //TODO add as soon as dokka is working
    }
  }
  js() {
    configure(listOf(compilations["main"], compilations["test"])) {
      tasks.getByName(compileKotlinTaskName) {
        kotlinOptions {
          languageVersion = "1.3"
          metaInfo = true
          sourceMap = true
          sourceMapEmbedSources = "always"
          moduleKind = "umd"
        }
      }
    }

    configure(listOf(compilations["main"])) {
      tasks.getByName(compileKotlinTaskName) {
        kotlinOptions {
          main = "call"
        }
      }
    }
  }

  sourceSets {
    commonMain {
      dependencies {
        implementation(Libs.kotlin_stdlib_common)
      }
    }
    commonTest {
      dependencies {
        implementation(Libs.kotlin_test_common)
        implementation(Libs.kotlin_test_annotations_common)
      }
    }

    named("jvmMain") {
      dependencies {
        implementation(Libs.kotlin_stdlib_jdk8)

        api(project(Projects.dependencies_sets_annotations))
        api(project(Projects.dependencies_sets_kotlin))
        api(project(Projects.open_annotations))
      }
    }

    named("jvmTest") {
      dependencies {
        implementation(Libs.kotlin_test)
        implementation(Libs.kotlin_test_junit)

        implementation(project(Projects.dependencies_sets_kotlin_test))
        implementation(project(Projects.open_commons_test_utils))

        implementation(Libs.commons_io)
        implementation(Libs.assertj_core)

      }
    }
    named("jsMain") {
      dependencies {
        implementation(Libs.kotlin_stdlib_js)
        implementation(Libs.kotlinx_coroutines_core_js)
      }
    }
    named("jsTest") {
      dependencies {
        implementation(Libs.kotlin_test_js)
      }
    }
  }

  targets.all {
    compilations.all {
      kotlinOptions {
        languageVersion = "1.3"
        allWarningsAsErrors = false
        verbose = true
        this.freeCompilerArgs
      }
    }
  }
}
