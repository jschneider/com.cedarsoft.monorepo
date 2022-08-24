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

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

/**
 *
 */
class JavaCommandBuilderTest {

  private lateinit var starter: JavaCommandBuilder

  @BeforeEach
  fun setUp() {
    starter = JavaCommandBuilder("mainClass")
  }

  @Test
  fun testClasspath() {
    val classPathElements = arrayOf("a", "b", "", "c")
    starter.setClassPathElements(*classPathElements)
    assertEquals(3, starter.getClassPathElements().size.toLong())
    assertEquals("a" + File.pathSeparator + "b" + File.pathSeparator + "c", starter.classPath)
    starter.addClassPathElement("d")
    assertEquals("a" + File.pathSeparator + "b" + File.pathSeparator + "c" + File.pathSeparator + "d", starter.classPath)
  }

  @Test
  fun testVmProperties() {
    starter.setVmProperties("key=value", "", "key2=value2")
    assertEquals(2, starter.getVmProperties().size.toLong())
    assertEquals("key=value", starter.getVmProperties()[0])
    assertEquals("key2=value2", starter.getVmProperties()[1])
    starter.addVmProperty("d=e")
    assertEquals("d=e", starter.getVmProperties()[2])
  }

  @Test
  fun testArguments() {
    starter.setArguments("a", "b", "", "c")
    assertEquals(3, starter.getArguments().size.toLong())
  }

  @Test
  fun testAll() {
    assertEquals("java mainClass", starter.commandLine)
    starter.setClassPathElements("a", "b")
    assertEquals("java -cp a" + File.pathSeparator + "b mainClass", starter.commandLine)
    starter.setArguments("arg", "arg1")
    assertEquals("java -cp a" + File.pathSeparator + "b mainClass arg arg1", starter.commandLine)
    starter.setVmProperties("prop0", "prop1")
    assertEquals("java -Dprop0 -Dprop1 -cp a" + File.pathSeparator + "b mainClass arg arg1", starter.commandLine)
    val elements = starter.commandLineElements
    assertEquals(8, elements.size.toLong())
    assertEquals("java", elements[0])
    assertEquals("-Dprop0", elements[1])
    assertEquals("-Dprop1", elements[2])
    assertEquals("-cp", elements[3])
    assertEquals("a" + File.pathSeparator + "b", elements[4])
    assertEquals("mainClass", elements[5])
    assertEquals("arg", elements[6])
    assertEquals("arg1", elements[7])
  }
}
