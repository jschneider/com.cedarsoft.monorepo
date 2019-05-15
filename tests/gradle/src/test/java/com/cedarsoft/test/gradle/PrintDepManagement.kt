package com.cedarsoft.test.gradle

import org.jdom2.input.SAXBuilder
import org.junit.jupiter.api.Test
import java.io.File

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
class PrintDepManagement {
  @Test
  internal fun testPrintDependencyManagemtn() {
    val build = SAXBuilder().build(File("/home/johannes/projects/com.cedarsoft.monorepo.gradle/closed/release-helper/pom.xml"))


    println("root: ${build.rootElement}")

    val depsFromManagement = build.rootElement
      .getChild("dependencyManagement", build.rootElement.namespace)
      ?.getChild("dependencies", build.rootElement.namespace)

    if (depsFromManagement != null) {
      for (dependency in depsFromManagement.children) {
        val groupId = dependency.getChild("groupId", build.rootElement.namespace).text
        val artifactId = dependency.getChild("artifactId", build.rootElement.namespace).text
        val version = dependency.getChild("version", build.rootElement.namespace).text

        if (version == "\${project.version}") {
          continue
        }

        printLine(groupId, artifactId, version, "api")
      }
    }

    val deps = build.rootElement
      .getChild("dependencies", build.rootElement.namespace)

    for (dependency in deps.children) {
      val groupId = dependency.getChild("groupId", build.rootElement.namespace).text
      val artifactId = dependency.getChild("artifactId", build.rootElement.namespace).text
      val version = dependency.getChild("version", build.rootElement.namespace)?.text

      if (version == null || version == "\${project.version}") {
        continue
      }

      printLine(groupId, artifactId, version, "api")
    }
  }

  private fun printLine(groupId: String, artifactId: String, version: String? = null, methodName: String = "api") {
    if (!groupId.startsWith("com.cedarsoft")) {
      val versionSuffix = if (version != null) ":$version" else ""

      println("$methodName(\"$groupId:$artifactId$versionSuffix\")")
      return
    }

    //We have special case for cedarsoft projects

    val matchingProject =
      when ("$groupId.$artifactId") {
        "com.cedarsoft.annotations"                   -> ":open:annotations:annotations"
        "com.cedarsoft.unit"                          -> ":open:unit:unit"
        "com.cedarsoft.charting.algorithms"           -> ":open:closed:charting:algorithms"
        "com.cedarsoft.dependencies-sets.openjfx"     -> null
        "com.cedarsoft.commons.app"                   -> ":open:commons:app"
        "com.cedarsoft.commons.javafx"                -> ":open:commons:javafx"
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
      } ?: return

    println("$methodName(project(\"$matchingProject\"))")
  }

  @Test
  internal fun printDependencies() {
    val build = SAXBuilder().build(File("/home/johannes/projects/com.cedarsoft.monorepo.gradle/closed/release-helper/pom.xml"))

    val depManagement = build.rootElement
      .getChild("dependencies", build.rootElement.namespace)


    println("//\n// Compile Deps\n//")

    for (dependency in depManagement.children) {
      val groupId = dependency.getChild("groupId", build.rootElement.namespace).text
      val artifactId = dependency.getChild("artifactId", build.rootElement.namespace).text
      val scope = dependency.getChild("scope", build.rootElement.namespace)?.text

      if (isTestScope(scope)) {
        continue
      }

      printLine(groupId, artifactId, methodName = "api")
    }

    println("\n\n//\n// Test Deps\n//")

    for (dependency in depManagement.children) {
      val groupId = dependency.getChild("groupId", build.rootElement.namespace).text
      val artifactId = dependency.getChild("artifactId", build.rootElement.namespace).text
      val scope = dependency.getChild("scope", build.rootElement.namespace)?.text

      if (isTestScope(scope)) {
        printLine(groupId, artifactId, methodName = "testImplementation")
      }
    }
  }

  private fun isTestScope(scope: String?) = scope != null && scope == "test"
}
