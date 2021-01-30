description = """Serialization - Test Utils"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlinJvm
}

dependencies {
  //
  // Compile Deps
  //
  api(project(Projects.dependencies_sets_kotlin_test))
  api(project(Projects.open_serialization_serialization))
  api(project(Projects.open_commons_test_utils))

  api(Libs.staxMate)
  api(Libs.woodstoxCoreAsl)
  api(Libs.commonsLang3)
  api(Libs.commonsIo)
  api(Libs.mockitoCore)
  api(Libs.easyMockClassExtension)
  api(Libs.easymock)
  api(Libs.assertjCore)
  api(Libs.festReflect)
  api(Libs.junit)
  api(Libs.jackson.databind)
  api(Libs.xmlunit)
}
