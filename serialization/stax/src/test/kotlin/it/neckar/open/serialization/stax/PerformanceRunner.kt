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
package it.neckar.open.serialization.stax

import it.neckar.open.serialization.stax.AbstractStaxSerializer.Companion.wrapWithIndent
import it.neckar.open.version.Version
import it.neckar.open.version.VersionException
import it.neckar.open.version.VersionRange.Companion.single
import com.google.common.io.ByteStreams
import it.neckar.open.serialization.stax.AbstractStaxSerializer
import it.neckar.open.serialization.stax.StaxSupport
import java.io.IOException
import javax.annotation.Nonnull
import javax.xml.stream.XMLStreamException
import javax.xml.stream.XMLStreamReader
import javax.xml.stream.XMLStreamWriter

/**
 */
object PerformanceRunner {
  const val COUNT = 100000

  @Throws(Exception::class)
  @JvmStatic
  fun main(args: Array<String>) {
    val serializer: AbstractStaxSerializer<Int> = IntegerAbstractStaxSerializer()
    val out = ByteStreams.nullOutputStream()
    run {
      val start = System.currentTimeMillis()
      for (i in 0 until COUNT) {
        val xmlOutputFactory = StaxSupport.getXmlOutputFactory()
        val writer = xmlOutputFactory.createXMLStreamWriter(out) ?: throw IllegalStateException()
        writer.close()
      }
      println("Took: " + (System.currentTimeMillis() - start))
    }
    run {
      val start = System.currentTimeMillis()
      for (i in 0 until COUNT) {
        val xmlOutputFactory = StaxSupport.getXmlOutputFactory()
        val writer = wrapWithIndent(xmlOutputFactory.createXMLStreamWriter(out)) ?: throw IllegalStateException()
        writer.close()
      }
      println("Took: " + (System.currentTimeMillis() - start))
    }
    run {
      val start = System.currentTimeMillis()
      for (i in 0 until COUNT) {
        val xmlOutputFactory = StaxSupport.getXmlOutputFactory()
        val indentingType = Class.forName("com.sun.xml.internal.txw2.output.IndentingXMLStreamWriter")
        val constructor = indentingType.getConstructor(XMLStreamWriter::class.java)
        val writer = constructor.newInstance(xmlOutputFactory.createXMLStreamWriter(out)) as XMLStreamWriter ?: throw IllegalStateException()
        writer.close()
      }
      println("Took: " + (System.currentTimeMillis() - start))
    }
  }

  internal class IntegerAbstractStaxSerializer : AbstractStaxSerializer<Int>("asdf", "asdfasdf", single(1, 0, 0)) {
    override fun serialize(serializeTo: XMLStreamWriter, objectToSerialize: Int, formatVersion: Version) {
      throw UnsupportedOperationException()
    }

    @Nonnull
    @Throws(IOException::class, VersionException::class, XMLStreamException::class)
    override fun deserialize(@Nonnull deserializeFrom: XMLStreamReader, @Nonnull formatVersion: Version): Int {
      throw UnsupportedOperationException()
    }
  }
}
