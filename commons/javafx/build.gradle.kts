description = """JavaFX"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlinJvm
}

val coroutinesFeatureName: String = "coroutines"

java {
  registerFeature(coroutinesFeatureName) {
    usingSourceSet(sourceSets["main"])
  }
}

dependencies {
  api(project(Projects.dependencies_sets_jvm_kotlin))

  compileOnlyApi(project(Projects.open_annotations))

  api(project(Projects.open_commons_concurrent))
  api(project(Projects.open_commons_disposable))
  api(project(Projects.open_commons_kotlin_collections))
  api(project(Projects.open_commons_i18n))
  api(project(Projects.open_unit_unit))

  implementation(Libs.guava)
  implementation(Libs.commons_lang3)

  api(project(Projects.open_commons_time))
  api(project(Projects.open_unit_unit))
  api(project(Projects.open_commons_concurrent))


  "${coroutinesFeatureName}Api"(KotlinX.coroutines.core)
  "${coroutinesFeatureName}Api"(KotlinX.coroutines.javaFx)

  testImplementation(project(Projects.dependencies_sets_jvm_kotlin_test))
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
