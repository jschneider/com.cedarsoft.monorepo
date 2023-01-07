description = """Kotlin JS"""

plugins {
  kotlinJs
}


dependencies {
  api(project(Projects.dependencies_sets_js_kotlin))
  api(project(Projects.open_commons_kotlin_lang))

  api(Libs.kotlinx_serialization_json)
  api(Libs.kotlin_js)

  implementation(enforcedPlatform(Libs.kotlin_wrappers_bom))

  testImplementation(project(Projects.dependencies_sets_js_kotlin_test))
}


kotlin {
  js {

    browser {
      binaries.executable()
      configureJsKarma()
    }
  }
}
