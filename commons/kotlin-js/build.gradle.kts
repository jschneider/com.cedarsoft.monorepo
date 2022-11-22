description = """Kotlin JS"""

plugins {
  kotlinJs
}


dependencies {
  api(project(Projects.dependencies_sets_js_kotlin))
  api(project(Projects.open_commons_kotlin_lang))
  api(Libs.kotlinx_serialization_json)

  implementation(enforcedPlatform(Libs.kotlin_wrappers_bom))
  api(Libs.kotlin_js)

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
