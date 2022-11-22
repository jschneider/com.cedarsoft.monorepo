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
  implementation(project(Projects.open_commons_kotlin_js))
  implementation(project(Projects.open_commons_kotlin_collections))

  implementation(Libs.uuid)

  //BOM for dependencies
  implementation(enforcedPlatform(Libs.kotlin_wrappers_bom))

  implementation(Libs.kotlin_react)
  implementation(Libs.kotlin_react_dom)
  implementation(Libs.kotlin_styled)

  implementation(npm("react", "_"))
  implementation(npm("toastr", "_"))


  testImplementation(project(Projects.dependencies_sets_js_kotlin_test))
}

tasks.withType<GenerateModuleMetadata> {
  suppressedValidationErrors.add("enforced-platform")
}

