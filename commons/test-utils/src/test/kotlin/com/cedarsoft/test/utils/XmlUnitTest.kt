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

import com.cedarsoft.test.utils.AssertUtils.assertXMLEquals
import com.cedarsoft.test.utils.AssertUtils.setIgnoreWhitespace
import org.assertj.core.api.Assertions
import org.custommonkey.xmlunit.XMLAssert
import org.junit.jupiter.api.Test
import javax.annotation.Nonnull

/**
 *
 * XmlUnitTest class.
 *
 */
class XmlUnitTest {
  @Test
  fun testIt() {
    XMLAssert.assertXMLEqual(
      WITH_WHITESPACES_SINGLE_QUOTED,
      WITH_WHITESPACES
    )
    assertXMLEquals(
      WITH_WHITESPACES_SINGLE_QUOTED,
      WITH_WHITESPACES
    )
  }

  @Test
  fun testWhitespaces() {
    setIgnoreWhitespace(true)
    XMLAssert.assertXMLEqual(
      WITHOUT_WHITESPACES,
      WITH_WHITESPACES
    )
    assertXMLEquals(
      WITHOUT_WHITESPACES,
      WITH_WHITESPACES
    )
  }

  @Test
  fun testEmpty() {
    var failed = false
    try {
      assertXMLEquals("", "<xml/>")
      failed = true
    } catch (ignore: AssertionError) {
    }
    Assertions.assertThat(failed).isFalse
    try {
      assertXMLEquals("", "")
      failed = true
    } catch (ignore: AssertionError) {
    }
    Assertions.assertThat(failed).isFalse
    try {
      assertXMLEquals("<xml/>", "")
      failed = true
    } catch (ignore: AssertionError) {
    }
    Assertions.assertThat(failed).isFalse
  }

  companion object {
    @Nonnull
    val WITH_WHITESPACES: String = """<?xml version="1.0" encoding="UTF-8"?>
<fileType dependent="false">
  <id>Canon Raw</id>
  <extension default="true" delimiter=".">cr2</extension>
</fileType>"""

    @Nonnull
    val WITH_WHITESPACES_DIFFERENT: String = """<?xml version="1.0" encoding="UTF-8"?>
<fileType dependent="false">
  <id>Canon Raw</id>
  <extension default="true" delimiter=".">jpg</extension>
</fileType>"""

    @Nonnull
    val WITH_WHITESPACES_SINGLE_QUOTED: String = """<?xml version='1.0' encoding='UTF-8'?>
<fileType dependent="false">
  <id>Canon Raw</id>
  <extension default="true" delimiter=".">cr2</extension>
</fileType>"""

    @Nonnull
    val WITHOUT_WHITESPACES: String = "<?xml version='1.0' encoding='UTF-8'?><fileType dependent=\"false\"><id>Canon Raw</id><extension default=\"true\" delimiter=\".\">cr2</extension></fileType>"
  }
}
