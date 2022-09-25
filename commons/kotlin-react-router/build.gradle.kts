plugins {
  kotlinxSerialization
  kotlinJs
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
  implementation(enforcedPlatform(Libs.kotlin_wrappers_bom))

  implementation(Libs.kotlin_react)
  implementation(Libs.kotlin_react_dom)
  implementation(Libs.kotlin_styled)

  implementation(Libs.kotlin_redux)
  implementation(Libs.kotlin_react_redux)
  implementation(Libs.kotlin_react_router_dom)

  implementation(npm("react", "_"))
}

tasks.withType<GenerateModuleMetadata> {
  suppressedValidationErrors.add("enforced-platform")
}

