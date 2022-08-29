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
package com.cedarsoft.test.utils

import com.cedarsoft.crypt.Algorithm
import com.cedarsoft.crypt.Hash
import com.cedarsoft.crypt.Hash.Companion.fromHex
import com.cedarsoft.crypt.HashCalculator.calculate
import com.cedarsoft.xml.XmlCommons
import com.google.common.base.Charsets
import org.apache.commons.io.FileUtils
import org.custommonkey.xmlunit.XMLAssert
import org.custommonkey.xmlunit.XMLUnit
import org.junit.jupiter.api.Assertions
import org.opentest4j.AssertionFailedError
import org.xml.sax.SAXException
import java.io.File
import java.io.IOException
import java.net.URL
import java.nio.charset.Charset

/**
 *
 * AssertUtils class.
 *
 */
object AssertUtils {
  /**
   *
   * setIgnoreWhitespace
   *
   * @param ignore a boolean.
   */
  @JvmStatic
  fun setIgnoreWhitespace(ignore: Boolean) {
    XMLUnit.setIgnoreWhitespace(ignore)
  }

  @JvmStatic
  fun setIgnoreComments(ignore: Boolean) {
    XMLUnit.setIgnoreComments(ignore)
  }

  @JvmStatic
  @JvmOverloads
  fun assertXMLEquals(control: URL, test: String, charset: Charset = Charsets.UTF_8) {
    assertXMLEquals(control.readText(charset), test)
  }

  @JvmStatic
  @JvmOverloads
  fun assertXMLEquals(control: URL, test: String, ignoreWhiteSpace: Boolean, charset: Charset = Charsets.UTF_8) {
    assertXMLEquals(control.readText(charset), test, ignoreWhiteSpace)
  }

  @JvmStatic
  @JvmOverloads
  fun assertXMLEquals(control: String, test: String, ignoreWhiteSpace: Boolean = true, ignoreComments: Boolean = true) {
    if (test.trim { it <= ' ' }.isEmpty()) {
      throw AssertionFailedError("Empty test xml", formatXml(control).trim { it <= ' ' }, formatXml(test).trim { it <= ' ' })
    }
    if (control.trim { it <= ' ' }.isEmpty()) {
      throw AssertionFailedError("Empty control xml", formatXml(control).trim { it <= ' ' }, formatXml(test).trim { it <= ' ' })
    }
    try {
      setIgnoreWhitespace(ignoreWhiteSpace)
      setIgnoreComments(ignoreComments)
      XMLAssert.assertXMLEqual(control, test)
      setIgnoreWhitespace(false)
    } catch (e: SAXException) {
      throw AssertionFailedError("XML error (" + e.message + ")", formatXml(control).trim { it <= ' ' }, formatXml(test).trim { it <= ' ' }, e)
    } catch (ignore: AssertionFailedError) {
      throw AssertionFailedError("XML comparison failed", formatXml(control).trim { it <= ' ' }, formatXml(test).trim { it <= ' ' })
    }
  }

  private fun formatXml(control: String): String {
    return try {
      XmlCommons.format(control)
    } catch (ignore: Exception) {
      //Do not format if it is not possible...
      control
    }
  }

  /**
   *
   * assertEquals
   *
   * @param expectedResourceUri a URL object.
   * @param actual              a Object object.
   * @throws IOException if any.
   */
  @JvmStatic
  @JvmOverloads
  fun assertEquals(expectedResourceUri: URL, actual: Any, charset: Charset = Charsets.UTF_8) {
    Assertions.assertEquals(expectedResourceUri.readText(charset), actual)
  }

  @JvmStatic
  @Deprecated("use kotlin method", ReplaceWith("expectedResourceUri.readText(charset)"))
  fun toString(expectedResourceUri: URL, charset: Charset = Charsets.UTF_8): String {
    return expectedResourceUri.readText(charset)
  }

  @JvmStatic
  fun assertFileByHashes(fileUnderTest: File, algorithm: Algorithm, vararg expectedHashesAsHex: String) {
    val expectedHashes = Array(expectedHashesAsHex.size) {
      val expectedHashAsHex = expectedHashesAsHex[it]
      fromHex(algorithm, expectedHashAsHex)
    }

    assertFileByHashes(fileUnderTest, *expectedHashes)
  }

  @JvmStatic
  fun assertFileByHashes(fileUnderTest: File, vararg expectedHashes: Hash) {
    require(expectedHashes.isNotEmpty()) { "Need at least on hash" }
    assertFileByHash(guessPathFromStackTrace(), listOf(*expectedHashes), fileUnderTest)
  }

  @JvmStatic
  fun assertFileByHash(expected: Hash, fileUnderTest: File) {
    val path = guessPathFromStackTrace()
    assertFileByHash(path, expected, fileUnderTest)
  }

  @JvmStatic
  fun assertFileByHash(testClass: Class<*>, testMethodName: String, expected: Hash, fileUnderTest: File) {
    assertFileByHash(createPath(testClass, testMethodName), expected, fileUnderTest)
  }

  @JvmStatic
  fun createPath(testClass: Class<*>, testMethodName: String): String {
    return testClass.name + File.separator + testMethodName
  }

  @JvmStatic
  fun assertFileByHash(path: String, expected: Hash, fileUnderTest: File) {
    val actual = calculate(expected.algorithm, fileUnderTest)
    if (expected == actual) {
      return  //everything went fine
    }
    val copy = createCopyFile(path, fileUnderTest.name)
    if (copy.exists()) {
      FileUtils.moveFile(copy, File(copy.parentFile, copy.name + "." + System.nanoTime()))
    }
    FileUtils.copyFile(fileUnderTest, copy)
    org.assertj.core.api.Assertions.assertThat(actual).describedAs(createReason(copy)).isEqualTo(expected)
  }


  private fun createReason(copy: File): String {
    return "Stored questionable file under test at <" + copy.absolutePath + ">"
  }

  @JvmStatic
  fun assertFileByHash(path: String, expectedHashes: Iterable<Hash>, fileUnderTest: File) {
    val actualHashes: MutableCollection<Hash> = ArrayList()
    for (expected in expectedHashes) {
      val actual = calculate(expected.algorithm, fileUnderTest)
      actualHashes.add(actual)
      if (expected == actual) {
        return  //everything went fine
      }
    }
    val copy = createCopyFile(path, fileUnderTest.name)
    FileUtils.copyFile(fileUnderTest, copy)
    org.assertj.core.api.Assertions.assertThat(actualHashes).describedAs(createReason(copy)).isEqualTo(expectedHashes)
  }


  @JvmStatic
  fun createCopyFile(path: String, name: String): File {
    return File(File(FailedFilesDir, path), name)
  }

  /**
   * The directory where the files have been stored
   */
  @JvmStatic
  val FailedFilesDir: File = File(TestUtils.tmpDir, "junit-failed-files-" + System.currentTimeMillis())

  @JvmStatic
  fun guessPathFromStackTrace(): String {
    val elements = Thread.currentThread().stackTrace
    if (elements.size < 4) {
      return "unknown"
    }
    val element = elements[3]
    return element.className + File.separator + element.methodName
  }
}
