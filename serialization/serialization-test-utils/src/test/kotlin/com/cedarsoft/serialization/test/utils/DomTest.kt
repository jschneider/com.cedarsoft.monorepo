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
package com.cedarsoft.serialization.test.utils

import com.cedarsoft.test.utils.AssertUtils
import com.cedarsoft.xml.XmlCommons
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.StringWriter
import java.nio.charset.StandardCharsets
import javax.xml.parsers.DocumentBuilderFactory

/**
 */
class DomTest {
  @Test
  fun testIt() {
    val factory = DocumentBuilderFactory.newInstance()
    factory.isNamespaceAware = true
    val documentBuilder = factory.newDocumentBuilder()
    val doc = documentBuilder.parse(ByteArrayInputStream("<a/>".toByteArray(StandardCharsets.UTF_8)))
    val element = doc.documentElement
    Assertions.assertThat(element).isNotNull
    Assertions.assertThat(element.tagName).isEqualTo("a")
    Assertions.assertThat(element.namespaceURI).isEqualTo(null)
    element.setAttribute("daAttr", "daval")
    element.appendChild(doc.createElementNS("manuallyChangedChildNS", "DaNewChild"))
    element.appendChild(doc.createElement("child2WithoutNS"))
    XmlNamespaceTranslator()
      .addTranslation(null, "MyNS")
      .translateNamespaces(doc, false)
    val out = StringWriter()
    XmlCommons.out(doc, out)
    AssertUtils.assertXMLEquals(
      """<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<a daAttr="daval" xmlns="MyNS">
  <DaNewChild xmlns="manuallyChangedChildNS"/>
  <child2WithoutNS/>
</a>
""", out.toString()
    )
  }
}
