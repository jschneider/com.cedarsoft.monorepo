import Libs.commons_io

description = """Version-Info for the repository"""

plugins {
  kotlinMultiPlatform
}

repositories {
  mavenCentral()
}

kotlin {
  jvm()
  js() {
    nodejs()
  }

  sourceSets {
    val versionInformationSources by creating {
      kotlin.srcDir("build/generated/sources/versionInfo/")
    }

    val commonMain by getting {
      dependsOn(versionInformationSources)

      dependencies {
        api(Kotlin.stdlib.common)
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
        api(Kotlin.stdlib.jdk8)
        //implementation(Libs.kotlinx_coroutines_core)

        api(project(Projects.dependencies_sets_annotations))
        api(project(Projects.dependencies_sets_kotlin_jvm))
        api(project(Projects.open_annotations))
      }
    }

    jvm().compilations["test"].defaultSourceSet {
      dependencies {
        implementation(Libs.kotlin_test)
        implementation(Libs.kotlin_test_junit)

        implementation(project(Projects.dependencies_sets_kotlin_test))

        implementation(commons_io)
        implementation(Libs.mockito_core)
        implementation(Libs.assertj_core)
        implementation(Libs.commons_math3)
        implementation(Libs.mockito_kotlin)
        implementation(Libs.logback_classic)
        implementation(Libs.awaitility)
      }
    }

    js().compilations["main"].defaultSourceSet {
      dependencies {
        api(project(Projects.dependencies_sets_kotlin_js))
      }
    }
    js().compilations["test"].defaultSourceSet {
      dependencies {
        implementation(Libs.kotlin_test_js)
      }
    }
  }
}

task("createVersionConstants") {
  group = "Build"
  description = "Create the version constants file"

  tasks.findByName("compileKotlinJs")?.dependsOn?.add(this)
  tasks.findByName("compileKotlinJvm")?.dependsOn?.add(this)

  //Define inputs/outputs to support incremental builds
  inputs.property("version", version)
  inputs.property("branch", branch)
  inputs.property("gitDescribe", gitDescribe)
  inputs.property("gitCommit", gitCommit)
  inputs.property("gitCommitDate", gitCommitDate)
  inputs.property("buildDateDay", buildDateDay)

  val targetFile = file("build/generated/sources/versionInfo/VersionConstants.kt")
  outputs.file(targetFile)

  doLast {
    targetFile.parentFile.mkdirs()

    targetFile.writeText(
      """
      package com.cedarsoft.version

      object VersionConstants{
        val monorepoVersion: String = "$version"
        val buildDateDay: String = "$buildDateDay"
        val branch: String = "$branch"
        val gitDescribe: String = "$gitDescribe"
        val gitCommit: String = "$gitCommit"
      }
      """.trimIndent()
    )

    println("Wrote version info to : ${targetFile.absolutePath}")
  }
}
