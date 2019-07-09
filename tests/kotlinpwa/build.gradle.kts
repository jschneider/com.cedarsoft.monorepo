import org.jetbrains.kotlin.gradle.dsl.KotlinJsDce
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile

plugins {
  id("kotlin2js")
  id("kotlin-dce-js")
  id("org.jetbrains.kotlin.frontend")
}

dependencies {
  compile("org.jetbrains.kotlin:kotlin-stdlib-js")
  compile("org.jetbrains.kotlin:kotlin-test-js")
  compile("org.jetbrains.kotlinx:kotlinx-coroutines-core-js")

  compile(project(":dependencies-sets:basics"))
}

tasks.withType<Kotlin2JsCompile> {
  kotlinOptions {
    //moduleKind = "commonjs"
    sourceMap = true
    sourceMapEmbedSources = "always"
  }
}

tasks.withType<KotlinJsDce> {
  //keep += "kotlinpwa.com.cedarsoft.tests.pwa.startPwaDemo"
}

kotlinFrontend {
  downloadNodeJsVersion = "11.2.0"
  sourceMaps = true
}

tasks {
  compileKotlin2Js {
    kotlinOptions {
      //outputFile = "${sourceSets.main.get().output.resourcesDir}/output.js"
      sourceMap = true
      sourceMapEmbedSources = "always"
    }
  }
}
