plugins {
  kotlin("jvm")
}

dependencies {
  api(project(":dependencies-sets:kotlin"))
  testImplementation(project(":dependencies-sets:test-basics"))
}
