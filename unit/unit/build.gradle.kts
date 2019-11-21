description = """Unit"""

group = "com.cedarsoft"

plugins {
  // Apply the java-library plugin to add support for Java Library
  kotlinMultiPlatform
  dokka
}

kotlin {
  jvm() {
    mavenPublication {
      //artifact("dokkaJar") //TODO add as soon as dokka is working
    }
  }

  js() {
    val main by compilations.getting {
      kotlinOptions {
        main = "call"

        metaInfo = true
        sourceMap = true

        sourceMapEmbedSources = "always"
        //moduleKind = "commonjs"
      }
    }

    nodejs {
      // target and run tests on node.js
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
        freeCompilerArgs = listOf("-XXLanguage:+InlineClasses")
      }
    }
  }
}
