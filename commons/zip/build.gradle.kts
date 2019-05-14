plugins {
}

dependencies {
  api(project(":dependencies-sets:annotations"))
  api("commons-io:commons-io")
  api("org.apache.commons:commons-compress")

  testImplementation(project(":dependencies-sets:test-basics"))
  testImplementation(project(":open:commons:test-utils"))
}
