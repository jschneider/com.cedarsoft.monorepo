import org.jetbrains.kotlin.gradle.dsl.KotlinJsDce
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile

description = """Performance Tests"""

repositories {
  jcenter()
}

plugins {
  id("kotlin2js")
  id("kotlin-dce-js")
  id("org.jetbrains.kotlin.frontend")
}

dependencies {
  implementation(project(":dependencies-sets:basics"))

  compile("org.jetbrains.kotlin:kotlin-stdlib-js")
  compile("org.jetbrains.kotlin:kotlin-test-js")
  compile("org.jetbrains.kotlinx:kotlinx-coroutines-core-js")

  //compile(project(":performance"))
}

tasks.withType<Kotlin2JsCompile> {
  kotlinOptions {
    moduleKind = "umd"
    sourceMap = true
    sourceMapEmbedSources = "always"
  }
}

tasks.withType<KotlinJsDce> {
  keep += "profiling.com.cedarsoft.startProfiling"
}

kotlinFrontend {
  downloadNodeJsVersion = "6.10.0"
  sourceMaps = true
}
