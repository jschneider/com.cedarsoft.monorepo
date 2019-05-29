package com.cedarsoft.test.gradle

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
object ProjectDependenciesHelper {
  /**
   * Converts a maven dependency to a gradle dep line
   */
  fun createGradleDepLine(dependency: Dependency, depType: DepType): String {
    return "${depType.method}(${createGradleDep(dependency)})"
  }

  fun createGradleDep(dependency: Dependency): String {
    val groupId = dependency.groupId
    val artifactId = dependency.artifactId
    val version = dependency.version


    if (!dependency.isMonorepoDep()) {
      //This is a default dependency
      val versionSuffix = if (version != null) ":$version" else ""

      return "\"$groupId:$artifactId$versionSuffix\""
    }

    //We have special case for cedarsoft projects

    val groupAndArtifact = "$groupId.$artifactId"

    if (groupAndArtifact == "de.sick.sopas.all-deps") {
      return "project(\":closed:sick:sick-deps\")"
    }
    if (groupAndArtifact == "de.sick.sopas.ftd2xx-all") {
      return "\"de.sick.sopas:ftd2xx-all\""
    }

    if (groupId.startsWith("de.sick.sdd")) {
      return "project(\":closed:sick:sdd:${artifactId.replace(".", ":")}\")"
    }

    val matchingProject =
      when (groupAndArtifact) {
        "de.sick.commons.commons"                     -> ":closed:sick:commons:commons"
        "de.sick.commons.fastgraph"                   -> ":closed:sick:commons:fastgraph"
        "de.sick.commons.custom-components"           -> ":closed:sick:commons:custom-components"

        "com.cedarsoft.annotations"                   -> ":open:annotations:annotations"
        "com.cedarsoft.unit"                          -> ":open:unit:unit"
        "com.cedarsoft.charting.algorithms"           -> ":closed:charting:algorithms"
        "com.cedarsoft.charting.chartingfx"           -> ":closed:charting:chartingfx"
        "com.cedarsoft.dependencies-sets.openjfx"     -> null
        "com.cedarsoft.commons.app"                   -> ":open:commons:app"
        "com.cedarsoft.commons.javafx"                -> ":open:commons:javafx"
        "com.cedarsoft.commons.javafx3d"              -> ":open:commons:javafx3d"
        "com.cedarsoft.commons.commons"               -> ":open:commons:commons"
        "com.cedarsoft.commons.concurrent"            -> ":open:commons:concurrent"
        "com.cedarsoft.commons.concurrent-kotlin"     -> ":open:commons:concurrent-kotlin"
        "com.cedarsoft.commons.exception-handling"    -> ":open:commons:exception-handling"
        "com.cedarsoft.commons.exceptions"            -> ":open:commons:exceptions"
        "com.cedarsoft.commons.guava"                 -> ":open:commons:guava"
        "com.cedarsoft.commons.file"                  -> ":open:commons:file"
        "com.cedarsoft.commons.image"                 -> ":open:commons:image"
        "com.cedarsoft.commons.crypt"                 -> ":open:commons:crypt"
        "com.cedarsoft.commons.license"               -> ":open:commons:license"
        "com.cedarsoft.commons.swing.common"          -> ":open:commons:swing:common"
        "com.cedarsoft.commons.tornadofx"             -> ":open:commons:tornadofx"
        "com.cedarsoft.commons.execution"             -> ":open:commons:execution"
        "com.cedarsoft.commons.version"               -> ":open:commons:version"
        "com.cedarsoft.commons.javafxkt"              -> ":open:commons:javafxkt"
        "com.cedarsoft.commons.xml-commons"           -> ":open:commons:xml-commons"
        "com.cedarsoft.commons.io"                    -> ":open:commons:io"
        "com.cedarsoft.commons.test-utils"            -> ":open:commons:test-utils"
        "com.cedarsoft.dependencies-sets.kotlin-test" -> ":dependencies-sets:kotlin-test"
        "com.cedarsoft.dependencies-sets.kotlin"      -> ":dependencies-sets:kotlin"
        "com.cedarsoft.dependencies-sets.test-basics" -> ":dependencies-sets:test-basics"
        "com.cedarsoft.dependencies-sets.ktor"        -> ":dependencies-sets:ktor"
        "com.cedarsoft.dependencies-sets.ktor-client" -> ":dependencies-sets:ktor-client"
        "com.cedarsoft.dependencies-sets.tornadofx"   -> ":dependencies-sets:tornadofx"
        "com.cedarsoft.iris"                          -> ":closed:iris"
        "com.cedarsoft.iris-demo-model"               -> ":closed:iris-demo:iris-demo-model"
        "com.cedarsoft.serialization"                 -> ":open:serialization:serialization"
        "com.cedarsoft.serialization.stax"            -> ":open:serialization:stax"
        "com.cedarsoft.serialization.stax-mate"       -> ":open:serialization:stax-mate"
        "com.cedarsoft.serialization.neo4j"           -> ":open:serialization:neo4j:neo4j"
        "com.cedarsoft.serialization.jackson"         -> ":open:serialization:jackson"
        "com.cedarsoft.serialization.test-utils"      -> ":open:serialization:serialization-test-utils"

        else                                          -> throw IllegalArgumentException("Unsupported project <$groupId.$artifactId>")
      }

    return ("project(\"$matchingProject\")")
  }
}


private val projectPrefixes = listOf<String>("com.cedarsoft", "de.sick")


/**
 * Returns true if the dependency is a inner project reference within the mono repo
 */
private fun Dependency.isMonorepoDep(): Boolean {
  projectPrefixes.forEach {
    if (groupId.startsWith(it)) {
      return true
    }
  }

  return false
}

enum class DepType(val method: String) {
  API("api"),
  TEST_IMPLEMENTATION("testImplementation"),
}