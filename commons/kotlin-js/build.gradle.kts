description = """Kotlin JS"""

plugins {
  kotlinJs
}


dependencies {
  api(project(Projects.dependencies_sets_js_kotlin))
  api(project(Projects.open_commons_kotlin_lang))
  api(Libs.kotlinx_serialization_json)
  api("org.jetbrains.kotlin-wrappers:kotlin-extensions:_")

  testImplementation(Libs.kotlin_test_js)
}


kotlin {
  js {
    browser {
      binaries.executable()
    }
  }
}
