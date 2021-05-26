/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.cedarsoft.org/gpl3ce
 * (GPL 3 with Classpath Exception)
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */

package com.cedarsoft.execution


import java.io.File
import java.util.Collections
import java.util.Locale

/**
 * Class that is able to start a new java process
 *
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
class JavaCommandBuilder
constructor(
  /**
   *
   * Getter for the field `mainClass`.
   *
   * @return a String object.
   */
  private val mainClass: String
) {

  private val classPathElements = ArrayList<String>()
  private val vmProperties = ArrayList<String>()
  private val arguments = ArrayList<String>()

  /**
   * Returns the classpath
   *
   * @return the classpath
   */
  val classPath: String?
    get() {
      if (classPathElements.isEmpty()) {
        return null
      }
      val stringBuilder = StringBuilder()
      val it = classPathElements.iterator()
      while (it.hasNext()) {
        val classPathElement = it.next()
        if (classPathElement.isEmpty()) {
          continue
        }
        stringBuilder.append(classPathElement)
        if (it.hasNext()) {
          stringBuilder.append(File.pathSeparator)
        }
      }

      val classPath = stringBuilder.toString()

      return if (classPath.contains(" ")) {
        if (!System.getProperty("os.name").toLowerCase(Locale.ENGLISH).contains("windows")) {
          System.err.println("WARNING: Be careful! Added \" to the classpath. May be incompatible!")
        }
        '\"'.toString() + classPath + '\"'.toString()
      } else {
        classPath
      }
    }

  /**
   *
   * getCommandLineElements
   *
   * @return a List object.
   */
  val commandLineElements: List<String>
    get() {
      val elements = ArrayList<String>()
      elements.add(javaBin)

      for (property in getVmProperties()) {
        elements.add("-D$property")
      }

      val classPath = classPath
      if (classPath != null) {
        elements.add("-cp")
        elements.add(classPath)
      }

      elements.add(mainClass)

      for (argument in getArguments()) {
        elements.add(argument)
      }

      return elements
    }

  /**
   * Returns the command line as string. This method should only be used
   * for debugging purposes.
   * For creating a process use [.getCommandLineElements] instead.
   *
   * @return the command line as string
   *
   * @see .getCommandLineElements
   */
  val commandLine: String
    get() {
      val stringBuilder = StringBuilder()
      for (element in commandLineElements) {
        stringBuilder.append(element)
        stringBuilder.append(' ')
      }

      return stringBuilder.toString().trim { it <= ' ' }
    }

  /**
   *
   * Getter for the field `classPathElements`.
   *
   * @return a List object.
   */
  fun getClassPathElements(): List<String> {
    return Collections.unmodifiableList(classPathElements)
  }

  /**
   *
   * Setter for the field `classPathElements`.
   *
   * @param classPathElements a String object.
   */
  fun setClassPathElements(vararg classPathElements: String) {
    this.classPathElements.clear()
    for (element in classPathElements) {
      if (element.isEmpty()) {
        continue
      }
      this.classPathElements.add(element)
    }
  }

  /**
   *
   * addClassPathElement
   *
   * @param classPathElement a String object.
   */
  fun addClassPathElement(classPathElement: String) {
    classPathElements.add(classPathElement)
  }

  /**
   *
   * Getter for the field `vmProperties`.
   *
   * @return a List object.
   */
  fun getVmProperties(): List<String> {
    return Collections.unmodifiableList(vmProperties)
  }

  /**
   *
   * Setter for the field `vmProperties`.
   *
   * @param vmProperties a String object.
   */
  fun setVmProperties(vararg vmProperties: String) {
    this.vmProperties.clear()
    for (vmProperty in vmProperties) {
      if (vmProperty.isEmpty()) {
        continue
      }
      this.vmProperties.add(vmProperty)
    }
  }

  /**
   *
   * addVmProperty
   *
   * @param vmProeprty a String object.
   */
  fun addVmProperty(vmProeprty: String) {
    vmProperties.add(vmProeprty)
  }

  /**
   *
   * addArgument
   *
   * @param argument a String object.
   */
  fun addArgument(argument: String) {
    this.arguments.add(argument)
  }

  /**
   *
   * Setter for the field `arguments`.
   *
   * @param arguments a String object.
   */
  fun setArguments(vararg arguments: String) {
    this.arguments.clear()
    for (argument in arguments) {
      if (argument.isEmpty()) {
        continue
      }
      this.arguments.add(argument)
    }
  }

  /**
   *
   * Getter for the field `arguments`.
   *
   * @return a List object.
   */
  fun getArguments(): List<String> {
    return Collections.unmodifiableList(arguments)
  }

  companion object {
    const val javaBin = "java"
  }
}
