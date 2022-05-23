import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension

plugins {
  kotlinxSerialization
  kotlin("js")
}

kotlin {
  js {
    browser()
  }
}

dependencies {
  implementation(project(Projects.open_commons_concurrent_coroutines))
  implementation(project(Projects.open_commons_feature_flags))
  implementation(KotlinX.coroutines.core)

  implementation(project(Projects.open_commons_i18n))
  implementation(project(Projects.open_commons_uuid))
  implementation(project(Projects.open_commons_kotlin_lang))
  implementation(project(Projects.open_commons_kotlin_collections))

  implementation(Libs.uuid)

  //BOM for dependencies
  implementation(enforcedPlatform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:_"))

  implementation("org.jetbrains.kotlin-wrappers:kotlin-react")
  implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom")

  implementation("org.jetbrains.kotlin-wrappers:kotlin-styled")

  //State machine (with undo)
  //implementation("org.jetbrains.kotlin-wrappers:kotlin-redux")
  //
  //implementation("org.jetbrains.kotlin-wrappers:kotlin-react-redux")
  //implementation("org.jetbrains.kotlin-wrappers:kotlin-react-router-dom")
  //
  //implementation("org.jetbrains.kotlin-wrappers:kotlin-css")
  //implementation("org.jetbrains.kotlin-wrappers:kotlin-extensions")

  implementation(npm("react", "_"))
  //implementation(npm("react-dom", "_"))
  //implementation(npm("react-is", "_"))

  //implementation(npm("styled-components", "_"))

  implementation(npm("toastr", "_"))
  //implementation(npm("formik", "_"))
}

/**
 * Workaround for https://github.com/webpack/webpack-cli/issues/2894
 *
 * gradle run produces this error message:
 *
 * [webpack-cli] Invalid configuration object. Object has been initialized using a configuration object that does not match the API schema.
 * - configuration has an unknown property '_assetEmittingWrittenFiles'
 */
afterEvaluate {
  rootProject.extensions.configure<NodeJsRootExtension> {
    versions.webpackDevServer.version = "4.0.0"
  }
}

tasks.withType<GenerateModuleMetadata> {
  suppressedValidationErrors.add("enforced-platform")
}

