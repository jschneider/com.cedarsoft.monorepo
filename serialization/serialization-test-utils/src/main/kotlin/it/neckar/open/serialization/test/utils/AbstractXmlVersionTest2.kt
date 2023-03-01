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
import it.neckar.open.serialization.Serializer
import it.neckar.open.version.Version
import org.apache.commons.io.IOUtils
import org.xml.sax.SAXException
import java.io.IOException
import java.net.URL
import java.nio.charset.StandardCharsets

abstract class AbstractXmlVersionTest2<T : Any> : AbstractVersionTest2<T>() {
  class XmlVersionEntry(
    override val version: Version, private val xml: ByteArray
  ) : VersionEntry {

    constructor(version: Version, xml: String) : this(version, xml.toByteArray(StandardCharsets.UTF_8)) {}

    @Throws(Exception::class)
    override fun getSerialized(serializer: Serializer<*, *, *>): ByteArray {
      return processXml(xml, version, serializer as AbstractXmlSerializer<*, *, *>)
    }
  }

  companion object {
    /**
     * Converts the xml string to a byte array used to deserialize.
     * This method automatically adds the namespace containing the version.
     *
     * @param xml        the xml
     * @param version    the version
     * @param serializer the serializer
     * @return the byte array using the xml string
     */
    @JvmStatic
    @Throws(Exception::class)
    protected fun processXml(xml: String, version: Version, serializer: AbstractXmlSerializer<*, *, *>): ByteArray {
      return processXml(xml, serializer.createNameSpace(version))
    }

    @JvmStatic
    @Throws(Exception::class)
    protected fun processXml(xml: ByteArray, version: Version, serializer: AbstractXmlSerializer<*, *, *>): ByteArray {
      return processXml(xml, serializer.createNameSpace(version))
    }

    @JvmStatic
    @Throws(IOException::class, SAXException::class)
    protected fun processXml(xml: String, nameSpace: String): ByteArray {
      return AbstractXmlSerializerTest2.addNameSpace(nameSpace, xml.toByteArray(StandardCharsets.UTF_8)).toByteArray(StandardCharsets.UTF_8)
    }

    @JvmStatic
    @Throws(IOException::class, SAXException::class)
    protected fun processXml(xml: ByteArray, nameSpace: String): ByteArray {
      return AbstractXmlSerializerTest2.addNameSpace(nameSpace, xml).toByteArray(StandardCharsets.UTF_8)
    }

    @JvmStatic
    protected fun create(version: Version, xml: String): VersionEntry {
      return XmlVersionEntry(version, xml)
    }

    @JvmStatic
    protected fun create(version: Version, expected: URL): VersionEntry {
      return try {
        XmlVersionEntry(version, IOUtils.toByteArray(expected.openStream()))
      } catch (e: IOException) {
        throw RuntimeException(e)
      }
    }
  }
}
