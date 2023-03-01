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
package it.neckar.open.serialization.test.utils

import it.neckar.open.serialization.AbstractXmlSerializer
import it.neckar.open.serialization.StreamSerializer
import it.neckar.open.test.utils.AssertUtils
import it.neckar.open.xml.XmlCommons
import org.xml.sax.SAXException
import java.io.IOException
import java.io.StringWriter
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

/**
 * Abstract base class for XML based serializers.
 *
 *
 *
 *
 * Attention: it is necessary to define at least one DataPoint:
 *
 *
 * <pre>&#064;DataPoint<br></br>public static final Entry&lt;?&gt; ENTRY1 = create(<br></br> new DomainObject(),<br></br> &quot;&lt;xml/&gt;&quot; );</pre>
 *
 * @param <T> the type of the serialized object
</T> */
abstract class AbstractXmlSerializerTest2<T : Any> : AbstractSerializerTest2<T>() {
  @Throws(Exception::class)
  abstract override fun getSerializer(): StreamSerializer<T>


  @Throws(Exception::class)
  protected fun verify(current: ByteArray, expectedXml: ByteArray) {
    if (addNameSpace()) {
      val expectedWithNamespace: String = try {
        addNameSpace(getSerializer() as AbstractXmlSerializer<*, *, *>, expectedXml)
      } catch (ignore: SAXException) {
        String(expectedXml, StandardCharsets.UTF_8)
      }
      AssertUtils.assertXMLEquals(expectedWithNamespace, String(current, encoding))
    } else {
      AssertUtils.assertXMLEquals(String(expectedXml, StandardCharsets.UTF_8), String(current, encoding))
    }
  }

  protected val encoding: Charset
    get() = StandardCharsets.UTF_8

  @Throws(Exception::class)
  override fun verifySerialized(entry: Entry<T>, serialized: ByteArray) {
    verify(serialized, entry.expected)
  }

  protected open fun addNameSpace(): Boolean {
    return true
  }

  companion object {
    @JvmStatic
    @Throws(Exception::class)
    fun addNameSpace(serializer: AbstractXmlSerializer<*, *, *>, xmlBytes: ByteArray): String {
      return addNameSpace(serializer.createNameSpace(serializer.formatVersion), xmlBytes)
    }


    @JvmStatic
    @Throws(IOException::class, SAXException::class)
    fun addNameSpace(nameSpaceUri: String, xml: ByteArray): String {
      val document = XmlCommons.parse(xml)
      XmlNamespaceTranslator()
        .addTranslation(null, nameSpaceUri)
        .translateNamespaces(document, false)
      val out = StringWriter()
      XmlCommons.out(document, out)
      return out.toString()
    }

    @JvmStatic
    protected fun <T> create(objectToSerialize: T, expected: String): Entry<out T> {
      return Entry(objectToSerialize, expected.toByteArray(StandardCharsets.UTF_8))
    }
  }
}
