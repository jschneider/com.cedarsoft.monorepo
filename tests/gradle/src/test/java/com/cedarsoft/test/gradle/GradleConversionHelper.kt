package com.cedarsoft.test.gradle

import com.google.common.collect.ImmutableList
import org.jdom2.Document
import org.jdom2.Element
import org.jdom2.input.SAXBuilder
import java.io.File
import java.io.IOException
import java.nio.file.Files

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
fun main() {
  GradleConversionHelper.run()
}

object GradleConversionHelper {

  val dependencyManagement: MutableSet<Dependency> = hashSetOf()

  /**
   * Contains the project paths that are added to the gradle settings file
   */
  val settingsPaths: MutableList<String> = mutableListOf()

  fun run() {
    val baseDir = File("/home/johannes/projects/com.cedarsoft.monorepo/closed/autoresultat/ng")
    if (!baseDir.isDirectory) {
      throw IOException("Not a directory <${baseDir.absolutePath}>")
    }

    convertPomInDirectory(baseDir)

    println("##########################")
    println("COMPILE")
    println("##########################")
    dependencyManagement
      .filter { it.version != null }
      .filter { it.version != "\${project.version}" }
      .filter { it.scope == Scope.COMPILE }
      .forEach { println(it.format()) }

    println("##########################")
    println("PROVIDED")
    println("##########################")
    dependencyManagement
      .filter { it.version != null }
      .filter { it.version != "\${project.version}" }
      .filter { it.scope == Scope.PROVIDED }
      .forEach { println(it.format()) }

    println("##########################")
    println("TEST")
    println("##########################")
    dependencyManagement
      .filter { it.version != null }
      .filter { it.version != "\${project.version}" }
      .filter { it.scope == Scope.TEST }
      .forEach { println(it.format()) }

    println("##############")
    println("Append to settings.gradle.kts")
    println("##############")

    settingsPaths
      .map {
        it
          .replace("project(", "")
          .replace(")", "")
      }
      .forEach {
        println("include($it)")
      }
  }

  private fun convertPomInDirectory(dir: File): Boolean {
    val pomFile = File(dir, "pom.xml")
    if (!pomFile.exists()) {
      return false
    }

    convertPom(pomFile)

    dir.listFiles()
      .forEach {
        if (it.isDirectory) {
          convertPomInDirectory(it)
        }
      }

    return true
  }

  private fun convertPom(pomFile: File) {
    val document = SAXBuilder().build(pomFile)
    dependencyManagement.addAll(document.collectDepsForDependenciesManagement())

    if (document.isPackagingPom()) {
      //Do not convert pom files
      return
    }

    println("Converting ${pomFile.absolutePath}")


    settingsPaths.add(ProjectDependenciesHelper.createGradleDep(document.extractDependency()))


    val mavenDeps = document.collectDependencies()

    //Compile deps
    val compileDepLines = mavenDeps
      .filter { it.scope == Scope.COMPILE }
      .map {
        ProjectDependenciesHelper.createGradleDep(it)
      }.toList()

    val testDepLines = mavenDeps
      .filter { it.scope == Scope.TEST }
      .map {
        ProjectDependenciesHelper.createGradleDep(it)
      }.toList()

    val providedDepLines = mavenDeps
      .filter { it.scope == Scope.PROVIDED }
      .map {
        ProjectDependenciesHelper.createGradleDep(it)
      }.toList()


    val stringBuilder = StringBuilder()

    stringBuilder.append(
      """
      plugins {
        `java-library`
        kotlin("jvm")
      }

      dependencies {

    """.trimIndent()
    )

    compileDepLines.forEach {
      stringBuilder.append("api($it)").append("\n")
    }
    providedDepLines.forEach {
      stringBuilder.append("implementation($it)").append("\n")
    }
    testDepLines.forEach {
      stringBuilder.append("testImplementation($it)").append("\n")
    }

    stringBuilder.append(
      """
        testImplementation(project(":dependencies-sets:test-basics"))
        testImplementation(project(":open:commons:test-utils"))
    """
    )
    stringBuilder.append("}")

    val targetFile = File(pomFile.parentFile, "build.gradle.kts")

    if (writeFiles) {
      Files.write(targetFile.toPath(), stringBuilder.toString().toByteArray())
    } else {
      println(stringBuilder.toString())
    }
  }

  private val writeFiles = true

  private fun Document.isPackagingPom(): Boolean {
    val packaging: Element? = document.rootElement.getChild("packaging", document.rootElement.namespace)

    return packaging != null && packaging.text == "pom"
  }
}

private fun Document.extractDependency(): Dependency {
  val namespace = this.rootElement.namespace

  val groupElement: Element? = this.rootElement.getChild("groupId", namespace)

  val groupId: String
  if (groupElement != null) {
    groupId = groupElement.text
  } else {
    val parentElement = this.rootElement.getChild("parent", namespace) ?: throw IllegalArgumentException("No parent element found")
    groupId = parentElement.getChild("groupId", namespace).text
  }

  val artifactId = this.rootElement.getChild("artifactId", namespace).text

  return Dependency(groupId, artifactId!!, null, Scope.COMPILE)
}

/**
 * Returns all dependencies - may or may not contain a version
 */
private fun Document.collectDependencies(): ImmutableList<Dependency> {
  val dependendecies = ImmutableList.builder<Dependency>()

  val dependencies: Element? = this.rootElement
    .getChild("dependencies", this.rootElement.namespace)

  if (dependencies != null) {
    for (dependency in dependencies.children) {
      dependendecies.add(dependency.parseDependency())
    }
  }

  return dependendecies.build()
}


/**
 * Returns all dependencies from "dependenciesManagement" and "dependencies"
 */
private fun Document.collectDepsForDependenciesManagement(): ImmutableList<Dependency> {
  val dependendecies = ImmutableList.builder<Dependency>()

  val depsFromManagement = this.rootElement
    .getChild("dependencyManagement", this.rootElement.namespace)
    ?.getChild("dependencies", this.rootElement.namespace)

  if (depsFromManagement != null) {
    for (dependency in depsFromManagement.children) {
      dependendecies.add(dependency.parseDependency())
    }
  }

  dependendecies.addAll(this.collectDependencies())

  return dependendecies.build()
}

private fun Element.parseDependency(): Dependency {
  val groupId = this.getChild("groupId", this.namespace).text
  val artifactId = this.getChild("artifactId", this.namespace).text
  val version = this.getChild("version", this.namespace)?.text
  val scopeAsString: String? = this.getChild("scope", this.namespace)?.text

  val scope = Scope.parse(scopeAsString)

  return Dependency(groupId, artifactId, version, scope)
}
