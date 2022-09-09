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

  implementation(project(Projects.open_commons_kotlin_react))

  implementation(Libs.uuid)

  //BOM for dependencies
  implementation(enforcedPlatform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:_"))

  implementation("org.jetbrains.kotlin-wrappers:kotlin-react")
  implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom")

  implementation("org.jetbrains.kotlin-wrappers:kotlin-styled")

  implementation("org.jetbrains.kotlin-wrappers:kotlin-redux")
  implementation("org.jetbrains.kotlin-wrappers:kotlin-react-redux")

  implementation("org.jetbrains.kotlin-wrappers:kotlin-react-router-dom")

  implementation(npm("react", "_"))
}

tasks.withType<GenerateModuleMetadata> {
  suppressedValidationErrors.add("enforced-platform")
}

