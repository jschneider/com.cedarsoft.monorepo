description = """JS Sanitize"""

repositories {
}

plugins {
  kotlinJs
}

dependencies {
  //compile( "org.jetbrains.kotlinx:kotlinx-html-js" )
  api(project(Projects.closed_meistercharts_algorithms))
  api(project(Projects.closed_meistercharts_canvas))
  api(project(Projects.closed_meistercharts_custom))
  api(project(Projects.open_unit_unit))

  testImplementation(project(Projects.dependencies_sets_js_kotlin_test))
}


kotlin {
  js {
    binaries.executable()

    browser {
      configureJsKarma()
    }
  }
}
