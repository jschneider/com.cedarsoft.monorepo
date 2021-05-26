description = """JavaFX"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlinJvm
}

java {
  registerFeature("coroutines") {
    usingSourceSet(sourceSets["main"])
  }
}

dependencies {
  api(project(Projects.dependencies_sets_kotlin_jvm))

  compileOnlyApi(project(Projects.open_annotations))

  api(project(Projects.open_commons_concurrent))
  api(project(Projects.open_unit_unit))

  api(Libs.guava)

  api(project(Projects.open_commons_time))
  api(project(Projects.open_unit_unit))
  api(project(Projects.open_commons_concurrent))


  "coroutinesApi"(KotlinX.coroutines.core)
  "coroutinesApi"(KotlinX.coroutines.javaFx)

  testImplementation(project(Projects.dependencies_sets_kotlin_test))
  testImplementation(project(Projects.open_commons_test_utils))
  testImplementation(project(Projects.open_commons_javafx_test_utils))
  testImplementation(Libs.miglayout_javafx)
  testImplementation(Libs.jfxtras_all)
  testImplementation(Libs.fxgraphics2d)
  testImplementation(Libs.jfoenix)
  testImplementation(Libs.fontawesomefx)
  testImplementation(Libs.controlsfx)
  testImplementation(Libs.tilesfx)
  testImplementation(Libs.vworkflows_fx)
  testImplementation(KotlinX.coroutines.core)
  testImplementation(KotlinX.coroutines.javaFx)
}
