description = """Serialization - Test Utils"""


plugins {
  // Apply the java-library plugin to add support for Java Library
  `java-library`
  kotlin("jvm")
}

dependencies {
  //
  // Compile Deps
  //
  api(project(":dependencies-sets:test-basics"))
  api(project(":open:serialization:serialization"))
  api(project(":open:commons:test-utils"))

  api("org.codehaus.staxmate:staxmate")
  api("org.codehaus.woodstox:woodstox-core-asl")
  api("org.apache.commons:commons-lang3")
  api("commons-io:commons-io")
  api("org.mockito:mockito-core")
  api("org.easymock:easymockclassextension")
  api("org.easymock:easymock")
  api("org.assertj:assertj-core")
  api("org.easytesting:fest-reflect")
  api("junit:junit")
  api("com.fasterxml.jackson.core:jackson-databind")
  api("xmlunit:xmlunit")
}
