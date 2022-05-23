@file:Suppress("UNUSED_VARIABLE")

import Libs.commons_io

description = """Version-Info for the repository"""

plugins {
  kotlinMultiPlatform
}

repositories {
  mavenCentral()
}

val createVersionConstantsTasks: Task = task("createVersionConstants") {
  group = "Build"
  description = "Create the version constants file"

  //Define inputs/outputs to support incremental builds
  inputs.property("version", version)
  inputs.property("branch", branch)
  inputs.property("gitDescribe", gitDescribe)
  inputs.property("gitCommit", gitCommit)
  inputs.property("gitCommitDate", gitCommitDate)
  inputs.property("buildDateDay", buildDateDay)

  val generatedSourcesDir = layout.buildDirectory.dir("generated/sources/$name/main/kotlin")
  val targetFileProvider = layout.buildDirectory.file("generated/sources/$name/main/kotlin/versionInfo/VersionConstants.kt")
  outputs.dir(generatedSourcesDir)

  doLast {
    val targetFile = targetFileProvider.get().asFile
    targetFile.parentFile.mkdirs()

    targetFile.writeText(
      """
      package com.cedarsoft.version

      object VersionConstants{
        const val monorepoVersion: String = "$version"
        const val buildDateDay: String = "$buildDateDay"
        const val branch: String = "$branch"
        const val gitDescribe: String = "$gitDescribe"
        const val gitCommit: String = "$gitCommit"
      }
      """.trimIndent()
    )

    println("Wrote version info to : ${targetFile.absolutePath}")
  }
}


kotlin {
  jvm()
  js() {
    nodejs()
  }

  sourceSets {
    val versionInformationSources by creating {
      kotlin.srcDir(createVersionConstantsTasks)
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
        api(project(Projects.dependencies_sets_js_kotlin))
      }
    }
    js().compilations["test"].defaultSourceSet {
      dependencies {
        implementation(Libs.kotlin_test_js)
      }
    }
  }
}

//Ensure createVersionConstants is added as dependency
tasks.findByName("jvmSourcesJar")?.dependsOn("createVersionConstants")
tasks.findByName("compileKotlinMetadata")?.dependsOn("createVersionConstants")
