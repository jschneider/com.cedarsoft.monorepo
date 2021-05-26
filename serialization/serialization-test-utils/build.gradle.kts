import Libs.commons_io
import Libs.jackson_databind

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

  api(Libs.staxmate)
  api(Libs.woodstox_core_asl)
  api(Libs.commons_lang3)
  api(commons_io)
  api(Libs.mockito_core)
  api(Libs.easymockclassextension)
  api(Libs.easymock)
  api(Libs.assertj_core)
  api(Libs.fest_reflect)
  api(Libs.junit)
  api(jackson_databind)
  api(Libs.xmlunit)
}
