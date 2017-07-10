/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce
 *         (GPL 3 with Classpath Exception)
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
package com.cedarsoft.serialization.stax;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;

import javax.annotation.Nonnull;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import com.cedarsoft.version.Version;
import com.cedarsoft.version.VersionException;
import com.cedarsoft.version.VersionRange;
import com.google.common.io.ByteStreams;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class PerformanceRunner {

  public static final int COUNT = 100000;

  public static void main(String[] args) throws Exception {
    AbstractStaxSerializer<Integer> serializer = new IntegerAbstractStaxSerializer();


    OutputStream out = ByteStreams.nullOutputStream();

    {
      long start = System.currentTimeMillis();
      for (int i = 0; i < COUNT; i++) {
        XMLOutputFactory xmlOutputFactory = StaxSupport.getXmlOutputFactory();
        XMLStreamWriter writer = xmlOutputFactory.createXMLStreamWriter(out);
        if (writer == null) {
          throw new IllegalStateException();
        }
        writer.close();
      }
      System.out.println("Took: " + (System.currentTimeMillis() - start));
    }

    {
      long start = System.currentTimeMillis();
      for (int i = 0; i < COUNT; i++) {
        XMLOutputFactory xmlOutputFactory = StaxSupport.getXmlOutputFactory();
        XMLStreamWriter writer = AbstractStaxSerializer.wrapWithIndent(xmlOutputFactory.createXMLStreamWriter(out));
        if (writer == null) {
          throw new IllegalStateException();
        }
        writer.close();
      }
      System.out.println("Took: " + (System.currentTimeMillis() - start));
    }

    {
      long start = System.currentTimeMillis();
      for (int i = 0; i < COUNT; i++) {
        XMLOutputFactory xmlOutputFactory = StaxSupport.getXmlOutputFactory();

        Class<?> indentingType = Class.forName("com.sun.xml.internal.txw2.output.IndentingXMLStreamWriter");
        Constructor<?> constructor = indentingType.getConstructor(XMLStreamWriter.class);
        XMLStreamWriter writer = (XMLStreamWriter) constructor.newInstance(xmlOutputFactory.createXMLStreamWriter(out));
        if (writer == null) {
          throw new IllegalStateException();
        }
        writer.close();
      }
      System.out.println("Took: " + (System.currentTimeMillis() - start));
    }
  }

  private static class IntegerAbstractStaxSerializer extends AbstractStaxSerializer<Integer> {
    private IntegerAbstractStaxSerializer() {
      super("asdf", "asdfasdf", VersionRange.single(1, 0, 0));
    }

    @Override
    public void serialize(@Nonnull XMLStreamWriter serializeTo, @Nonnull Integer objectToSerialize, @Nonnull Version formatVersion) throws IOException, VersionException, XMLStreamException {
      throw new UnsupportedOperationException();
    }

    @Nonnull
    @Override
    public Integer deserialize(@Nonnull XMLStreamReader deserializeFrom, @Nonnull Version formatVersion) throws IOException, VersionException, XMLStreamException {
      throw new UnsupportedOperationException();
    }
  }
}
