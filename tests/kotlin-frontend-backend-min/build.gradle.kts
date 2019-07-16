import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
    tasks.withType<KotlinCompile> {
      kotlinOptions.languageVersion = "1.3"
      kotlinOptions.jvmTarget = "1.8"
      kotlinOptions.allWarningsAsErrors = false
      kotlinOptions.verbose = true
      kotlinOptions.javaParameters = true
    }
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
  //Ensure the browser webpack is run first - only then the JS files are available
  this.dependsOn.add("jsBrowserWebpack")

  //Add the java script file to be able to deploy it
  from(resolveJsResult()) {
    into("js")
  }
}

/**
 * Runs the server
 */
task("run", type = JavaExec::class) {
  dependsOn += "jvmJar"
  main = "com.cedarsoft.open.kfbmin.server.MinimalKtorServerKt"

  //add the jvm jar file to the classpath
  classpath(configurations["jvmRuntimeClasspath"], tasks.named("jvmJar"))

  //Prints the class path before starting
  doFirst {
    println("Printing classpath")
    classpath.forEach {
      println("\t$it")
    }
  }
}

tasks.withType<ShadowJar> {
  archiveBaseName.set("server")

  from(project.configurations["jvmRuntimeClasspath"])
  from(file("build/classes/kotlin/jvm/main"))
  from(file("build/processedResources/jvm/main"))

  manifest {
    attributes(mapOf("Main-Class" to "com.cedarsoft.open.kfbmin.server.MinimalKtorServerKt"))
  }

  //Ensure the browser webpack is run first
  this.dependsOn.add("jsBrowserWebpack")
  this.dependsOn.add("jvmJar")

  //Add the java script file to be able to deploy it
  from(resolveJsResult()) {
    into("js")
  }
}

/**
 * Returns the JS file created by the task "jsBrowserWebpack"
 */
fun resolveJsResult(): File {
  val jsBrowserWebpackProvider = tasks.named("jsBrowserWebpack", type = KotlinWebpack::class)
  val webpackTask = jsBrowserWebpackProvider.get()
  return file(
    "${
    webpackTask.outputPath}/${webpackTask.entry.name}"
  )
}

fun Task.printInputsOutputs() {
  println("Inputs / outputs for ${this.name}")
  println("\tInput Files")
  inputs.files.forEach {
    println("\t\t $it")
  }

  println("\tInput source files")
  inputs.sourceFiles.forEach {
    println("\t\t $it")
  }

  println("\tOutput Files $this.name")
  outputs.files.forEach {
    println("\t\t $it")
  }
}
