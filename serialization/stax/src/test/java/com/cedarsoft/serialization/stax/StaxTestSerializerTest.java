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

import javax.annotation.Nonnull;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import com.cedarsoft.serialization.StreamSerializer;
import com.cedarsoft.serialization.test.utils.AbstractXmlSerializerTest2;
import com.cedarsoft.serialization.test.utils.Entry;
import com.cedarsoft.version.Version;
import com.cedarsoft.version.VersionRange;

/**
 *
 */
public class StaxTestSerializerTest extends AbstractXmlSerializerTest2<Integer> {
  @Nonnull
  @Override
  protected StreamSerializer<Integer> getSerializer() throws Exception {
    return new StaxIntegerSerializer();
  }

  public static final Entry<? extends Integer> ENTRY1 = create(7, "<int  xmlns=\"http://int/1.0.0\">7</int>");

  public static class StaxIntegerSerializer extends AbstractStaxSerializer<Integer> {
    public StaxIntegerSerializer() {
      super( "int", "http://int", new VersionRange( new Version( 1, 0, 0 ), new Version( 1, 0, 0 ) ) );
    }

    @Override
    public void serialize(@Nonnull XMLStreamWriter serializeTo, @Nonnull Integer objectToSerialize, @Nonnull Version formatVersion ) throws IOException, XMLStreamException {
      assert isVersionWritable( formatVersion );
      serializeTo.writeCharacters(objectToSerialize.toString() );
    }

    @Nonnull
    @Override
    public Integer deserialize( @Nonnull XMLStreamReader deserializeFrom, @Nonnull Version formatVersion ) throws IOException, XMLStreamException {
      assert isVersionReadable( formatVersion );
      return Integer.parseInt( getText( deserializeFrom ) );
    }
  }

}
