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
    val build = SAXBuilder().build(File("/home/johannes/projects/com.cedarsoft.monorepo/pom.xml"))


    println("root: ${build.rootElement}")

    val depManagement = build.rootElement
      .getChild("dependencyManagement", build.rootElement.namespace)
      .getChild("dependencies", build.rootElement.namespace)

    for (dependency in depManagement.children) {
      val groupId = dependency.getChild("groupId", build.rootElement.namespace).text
      val artifactId = dependency.getChild("artifactId", build.rootElement.namespace).text
      val version = dependency.getChild("version", build.rootElement.namespace).text

      if (version == "\${project.version}") {
        continue
      }

      printLine(groupId, artifactId, version)
    }
  }

  private fun printLine(groupId: String, artifactId: String, version: String? = null) {
    if (!groupId.startsWith("com.cedarsoft")) {
      val versionSuffix = if (version != null) ":$version" else ""

      println("api(\"$groupId:$artifactId$versionSuffix\")")
      return
    }

    //We have special case for cedarsoft projects

    val matchingProject =
      when ("$groupId.$artifactId") {
        "com.cedarsoft.annotations"                   -> ":open:annotations:annotations"
        "com.cedarsoft.unit"                          -> ":open:unit:unit"
        "com.cedarsoft.charting.algorithms"           -> null
        "com.cedarsoft.commons.javafx"                -> ":open:commons:javafx"
        "com.cedarsoft.commons.javafxkt"              -> ":open:commons:javafxkt"
        "com.cedarsoft.commons.test-utils"            -> ":open:commons:test-utils"
        "com.cedarsoft.dependencies-sets.kotlin-test" -> ":dependencies-sets:kotlin-test"
        "com.cedarsoft.dependencies-sets.kotlin"      -> ":dependencies-sets:kotlin"

        else                                          -> throw IllegalArgumentException("Unsupported project <$groupId.$artifactId>")
      } ?: return

    println("api(project(\"$matchingProject\"))")
  }

  @Test
  internal fun printDependencies() {
    val build = SAXBuilder().build(File("/home/johannes/projects/com.cedarsoft.monorepo/closed/charting/chartingfx/pom.xml"))

    val depManagement = build.rootElement
      .getChild("dependencies", build.rootElement.namespace)


    println("#\n# Compile Deps\n#")

    for (dependency in depManagement.children) {
      val groupId = dependency.getChild("groupId", build.rootElement.namespace).text
      val artifactId = dependency.getChild("artifactId", build.rootElement.namespace).text
      val scope = dependency.getChild("scope", build.rootElement.namespace)?.text

      if (isTestScope(scope)) {
        continue
      }

      printLine(groupId, artifactId)
    }

    println("\n\n#\n# Test Deps\n#")

    for (dependency in depManagement.children) {
      val groupId = dependency.getChild("groupId", build.rootElement.namespace).text
      val artifactId = dependency.getChild("artifactId", build.rootElement.namespace).text
      val scope = dependency.getChild("scope", build.rootElement.namespace)?.text

      if (isTestScope(scope)) {
        printLine(groupId, artifactId)
      }
    }
  }

  private fun isTestScope(scope: String?) = scope != null && scope == "test"
}
