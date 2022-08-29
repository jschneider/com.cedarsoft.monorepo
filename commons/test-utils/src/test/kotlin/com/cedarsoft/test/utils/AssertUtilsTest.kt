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

import com.cedarsoft.common.resources.getResourceAsStreamSafe
import com.cedarsoft.common.resources.getResourceSafe
import com.cedarsoft.crypt.Algorithm
import com.cedarsoft.crypt.Hash.Companion.fromHex
import com.cedarsoft.crypt.HashCalculator.calculate
import com.cedarsoft.test.utils.AssertUtils.assertEquals
import com.cedarsoft.test.utils.AssertUtils.assertFileByHash
import com.cedarsoft.test.utils.AssertUtils.assertXMLEquals
import com.cedarsoft.test.utils.AssertUtils.createCopyFile
import com.cedarsoft.test.utils.AssertUtils.createPath
import com.cedarsoft.test.utils.AssertUtils.guessPathFromStackTrace
import com.cedarsoft.test.utils.JsonUtils.assertJsonEquals
import com.cedarsoft.test.utils.JsonUtils.formatJson
import com.google.common.io.ByteStreams
import org.apache.commons.io.FileUtils
import org.assertj.core.api.Assertions
import org.junit.Assert
import org.junit.ComparisonFailure
import org.junit.jupiter.api.Test
import org.opentest4j.AssertionFailedError
import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets
import javax.annotation.Nonnull

/**
 *
 */
class AssertUtilsTest {
  @Test
  @Throws(Exception::class)
  fun testJsonFormat() {
    val separator = System.getProperty("line.separator")
    Assert.assertEquals(
      "{" + separator +
        "  \"id\" : \"asdfasdf\"," + separator +
        "  \"unformated\" : true" + separator +
        "}", formatJson("{\"id\":\"asdfasdf\",   \"unformated\":true}")
    )
    Assert.assertEquals(
      "{" + separator +
        "  \"id\" : \"asdfasdf\"," + separator +
        "  \"unformated\" : true" + separator +
        "}", formatJson("{\"id\":\"asdfasdf\", \"unformated\":true}")
    )
  }

  @Test
  @Throws(Exception::class)
  fun testJsonEquals() {
    assertJsonEquals("{\"id\":\"asdfasdf\",   \"unformated\":true}", "{\"id\":\"asdfasdf\",   \"unformated\":true}")
    assertJsonEquals("{\"id\":\"asdfasdf\",   \"unformated\":true}", "{\"id\":\"asdfasdf\",\"unformated\":true}")
  }

  @Test
  @Throws(Exception::class)
  fun testJsonNotEquals() {
    try {
      assertJsonEquals("{\"id\":\"asdfasdf\",   \"unformated\":true}", "{\"id\":\"asdfasdf\",   \"unformated\":false}")
      Assert.fail("Where is the Exception")
    } catch (e: ComparisonFailure) {
      Assertions.assertThat(e.message).contains("JSON comparison failed")
    }
  }

  @Test
  @Throws(Exception::class)
  fun testJsonNotFormatable() {
    try {
      assertJsonEquals(null as String?, null)
      Assert.fail("Where is the Exception")
    } catch (e: AssertionError) {
      Assertions.assertThat(e.message).contains("Empty actual json")
    }
    try {
      assertJsonEquals("affase", "asdf")
      Assert.fail("Where is the Exception")
    } catch (e: AssertionError) {
      Assertions.assertThat(e.message).contains("JSON parsing error")
    }
  }

  @Test
  @Throws(Exception::class)
  fun testXml() {
    try {
      assertXMLEquals("<xml2/>", "<xml/>")
      throw RuntimeException("expected assertion")
    } catch (e: AssertionError) {
      Assertions.assertThat(e.message).contains("Expected element tag name")
    }
  }

  @Test
  @Throws(IOException::class)
  fun testAssertWithResource() {
    assertEquals(
      javaClass.getResourceSafe("equals.txt"), """
   the content of the file...
   second line!
   third line!
   """.trimIndent()
    )
    try {
      assertEquals(javaClass.getResourceSafe("equals.txt"), "other!")
    } catch (ignore: AssertionError) {
      return
    }
    Assert.fail("Where is the Exception")
  }

  @Test
  @Throws(Exception::class)
  fun testXml2() {
    try {
      assertXMLEquals(javaClass.getResourceSafe("AssertUtilsTest.1.xml"), "<xml/>")
    } catch (e: AssertionError) {
      Assertions.assertThat(e.message).contains("Expected element tag name")
    }
  }

  @Test
  @Throws(Exception::class)
  fun testXml2WhiteSpaces() {
    assertXMLEquals(javaClass.getResourceSafe("AssertUtilsTest.2.xml"), "<xml/>", true)
  }

  @Test
  @Throws(Exception::class)
  fun testXml2WComments() {
    try {
      assertXMLEquals(String(ByteStreams.toByteArray(javaClass.getResourceAsStreamSafe("AssertUtilsTest.2.xml")), StandardCharsets.UTF_8), "<xml><!--comment2--></xml>", true, false)
      throw RuntimeException("expected assertion")
    } catch (e: AssertionError) {
      Assertions.assertThat(e.message).contains("Expected presence")
    }
    assertXMLEquals(String(ByteStreams.toByteArray(javaClass.getResourceAsStreamSafe("AssertUtilsTest.2.xml")), StandardCharsets.UTF_8), "<xml><!--comment2--></xml>", true, true)
  }

  @Test
  @Throws(Exception::class)
  fun testFormat() {
    try {
      assertXMLEquals("<xml2/>", "This is no xml!")
      Assert.fail("Where is the Exception")
    } catch (e: AssertionFailedError) {
      Assertions.assertThat(e.message).contains("XML error")
    }
  }

  @WithTempFiles
  @Test
  @Throws(Exception::class)
  fun testFileByHash(@Nonnull tmp: TemporaryFolder) {
    val file = tmp.newFile("daFile")
    FileUtils.writeStringToFile(file, "daContent", StandardCharsets.UTF_8)
    val expected = calculate(Algorithm.MD5, file)
    Assert.assertEquals("913aa4a45cea16f9714f109e7324159f", expected.valueAsHex)
    assertFileByHash("path", expected, file)
    assertFileByHash("path", calculate(Algorithm.MD5, file), file)
    assertFileByHash("path", calculate(Algorithm.SHA256, file), file)
    assertFileByHash("path", calculate(Algorithm.SHA1, file), file)
    assertFileByHash("path", calculate(Algorithm.SHA512, file), file)
    try {
      assertFileByHash(AssertUtilsTest::class.java, "testFileByHash", fromHex(Algorithm.MD5, "aa"), file)
      throw RuntimeException("expected assertion")
    } catch (e: AssertionError) {
      val copiedFile = createCopyFile(createPath(AssertUtilsTest::class.java, "testFileByHash"), "daFile")
      Assertions.assertThat(e.message).contains(copiedFile.absolutePath)
      Assert.assertTrue(copiedFile.parentFile.exists())
      Assert.assertTrue(copiedFile.exists())
      Assert.assertEquals("daContent", FileUtils.readFileToString(copiedFile, StandardCharsets.UTF_8))
    }
    try {
      assertFileByHash(fromHex(Algorithm.MD5, "aa"), file)
      throw RuntimeException("expected assertion")
    } catch (e: AssertionError) {
      Assertions.assertThat(e.message).contains("Stored questionable file under test at")
    }
  }

  @Test
  @Throws(Exception::class)
  fun testGuessPath() {
    Assert.assertEquals(AssertUtilsTest::class.java.name + File.separator + "testGuessPath", guess())
    Assert.assertEquals("com.cedarsoft.test.utils.AssertUtilsTest" + File.separator + "testGuessPath", guess())
  }

  @Test
  @Throws(Exception::class)
  fun testGuessPath2() {
    Assert.assertEquals(AssertUtilsTest::class.java.name + File.separator + "testGuessPath2", guess())
  }

  //this method is necessary to simulate the call to AssertUtils.assertFileByHash
  private fun guess(): String {
    return guessPathFromStackTrace()
  }
}
