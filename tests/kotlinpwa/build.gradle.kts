import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

repositories {
  mavenCentral()
  jcenter()
}

plugins {
  kotlin("multiplatform")
  id("kotlinx-serialization")
  id("com.github.johnrengelman.shadow") version "5.1.0"
  //id("kotlin2js")
  //id("kotlin-dce-js")
  //id("org.jetbrains.kotlin.frontend")
}

kotlin {
  jvm() {
  }

  js() {
    browser {
    }

    configure(listOf(compilations["main"], compilations["test"])) {
      tasks.getByName(compileKotlinTaskName) {
        kotlinOptions {
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
          //Call the main function automatically
          main = "call"
        }
      }
    }
  }

  targets.all {
    compilations.all {
      kotlinOptions {
        languageVersion = "1.3"
        allWarningsAsErrors = false
        verbose = true
      }
    }
  }

  sourceSets {
    commonMain {
      dependencies {
        implementation(project(":dependencies-sets:basics"))

        implementation(kotlin("stdlib-common"))
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common")
      }
    }
    commonTest {
      dependencies {
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
      }
    }

    named("jvmMain") {
      dependencies {
        implementation(project(":dependencies-sets:basics"))
        implementation(project(":dependencies-sets:ktor"))

        implementation(kotlin("stdlib-jdk8"))
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime")

        api(project(":dependencies-sets:annotations"))
        api(project(":dependencies-sets:kotlin"))
        api(project(":open:annotations:annotations"))
        api(project(":open:unit:unit"))

        api(project(":open:unit:unit"))

        api(project(":open:commons:ktor-server"))
        api("io.ktor:ktor-html-builder")
        api("org.slf4j:slf4j-api")
        api("ch.qos.logback:logback-classic")
        api("io.reactivex.rxjava2:rxjava")

        implementation(project(":closed:ktor-http-client"))

      }
    }

    named("jvmTest") {
      dependencies {
        implementation(kotlin("test"))
        implementation(kotlin("test-junit"))

        implementation(project(":dependencies-sets:kotlin-test"))
        implementation(project(":open:commons:test-utils"))

        implementation("commons-io:commons-io")
        implementation("org.easymock:easymockclassextension")
        implementation("org.easymock:easymock")
        implementation("org.mockito:mockito-core")
        implementation("org.assertj:assertj-core")
        implementation("org.apache.commons:commons-math3")

        implementation(project(":closed:ktor-http-client"))
        implementation(project(":dependencies-sets:kotlin-test"))
        implementation("io.ktor:ktor-server-test-host")
        implementation(project(":dependencies-sets:ktor-client"))
      }
    }
    named("jsMain") {
      dependencies {
        implementation(project(":dependencies-sets:basics"))

        implementation(kotlin("stdlib-js"))
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-js")
      }

      //tasks {
      //  "compileKotlin2Js"(Kotlin2JsCompile::class) {
      //    kotlinOptions.metaInfo = true
      //    kotlinOptions.sourceMap = true
      //    kotlinOptions.sourceMapEmbedSources = "always"
      //    kotlinOptions.moduleKind = "commonjs"
      //  }
      //}
    }
    named("jsTest") {
      dependencies {
        implementation(kotlin("test-js"))
      }
    }
  }
}

tasks.named("jvmJar", type = Jar::class) {
  //Ensure the browser webpack is run first
  this.dependsOn.add("jsBrowserWebpack")

  //Add the java script file to be able to deploy it
  val jsBrowserWebpackProvider = tasks.named("jsBrowserWebpack", type = KotlinWebpack::class)
  val webpackTask = jsBrowserWebpackProvider.get()

  from(file("${webpackTask.outputPath}/${webpackTask.entry.name}"))
}

/**
 * Runs the server
 */
task("run", type = JavaExec::class) {
  dependsOn += "jvmJar"
  main = "com.cedarsoft.tests.pwa.server.KotlinPwaServerKt"

  val jvmJarTask = tasks.named("jvmJar")

  classpath(configurations["jvmRuntimeClasspath"], jvmJarTask)
  //classpath(configurations.jvmRuntimeClasspath, jvmJar)

  doLast {
    println("Running run")
    println("Classpath $classpath")
  }
}

tasks.withType<ShadowJar> {
  archiveBaseName.set("vakila")
  archiveClassifier.set("")
  archiveVersion.set("")

  //minimize()

  from(project.configurations["jvmRuntimeClasspath"])
  from(file("build/classes/kotlin/jvm/main"))
  from(file("build/processedResources/jvm/main"))

  manifest {
    attributes(mapOf("Main-Class" to "com.cedarsoft.tests.pwa.server.KotlinPwaServerKt"))
  }

  //Ensure the browser webpack is run first
  this.dependsOn.add("jsBrowserWebpack")
  this.dependsOn.add("jvmJar")

  //Add the java script file to be able to deploy it
  val jsBrowserWebpackProvider = tasks.named("jsBrowserWebpack", type = KotlinWebpack::class)
  val webpackTask = jsBrowserWebpackProvider.get()

  from(file("${webpackTask.outputPath}/${webpackTask.entry.name}"))
}


//
//dependencies {
//  compile("org.jetbrains.kotlin:kotlin-stdlib-js")
//  compile("org.jetbrains.kotlin:kotlin-test-js")
//  compile("org.jetbrains.kotlinx:kotlinx-coroutines-core-js")
//
//  compile(project(":dependencies-sets:basics"))
//}
//
//tasks.withType<Kotlin2JsCompile> {
//  kotlinOptions {
//    //moduleKind = "commonjs"
//    sourceMap = true
//    sourceMapEmbedSources = "always"
//  }
//}
//
//tasks.withType<KotlinJsDce> {
//  //keep += "kotlinpwa.com.cedarsoft.tests.pwa.startPwaDemo"
//}
//
//kotlinFrontend {
//  downloadNodeJsVersion = "11.2.0"
//  sourceMaps = true
//}
//
//tasks {
//  compileKotlin2Js {
//    kotlinOptions {
//      //outputFile = "${sourceSets.main.get().output.resourcesDir}/output.js"
//      sourceMap = true
//      sourceMapEmbedSources = "always"
//    }
//  }
//}
