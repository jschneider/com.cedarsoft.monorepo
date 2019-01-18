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

package com.cedarsoft.serialization.stax.mate;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import javax.annotation.Nonnull;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.codehaus.staxmate.out.SMOutputElement;
import org.junit.jupiter.api.*;

import com.cedarsoft.serialization.SerializationException;
import com.cedarsoft.serialization.test.utils.AbstractXmlSerializerTest2;
import com.cedarsoft.serialization.test.utils.Entry;
import com.cedarsoft.version.Version;
import com.cedarsoft.version.VersionException;
import com.cedarsoft.version.VersionMismatchException;
import com.cedarsoft.version.VersionRange;
import com.cedarsoft.xml.XmlCommons;

/**
 *
 */
public class StaxMateSerializerTest extends AbstractXmlSerializerTest2<String> {
  @Nonnull
  @Override
  protected AbstractStaxMateSerializer<String> getSerializer() {
    return new AbstractStaxMateSerializer<String>( "aString", "http://www.lang.java/String", new VersionRange( new Version( 1, 5, 3 ), new Version( 1, 5, 3 ) ) ) {
      @Override
      public void serialize(@Nonnull SMOutputElement serializeTo, @Nonnull String objectToSerialize, @Nonnull Version formatVersion ) throws XMLStreamException {
        assert isVersionWritable( formatVersion );
        serializeTo.addCharacters(objectToSerialize);
      }

      @Override
      @Nonnull
      public String deserialize( @Nonnull XMLStreamReader deserializeFrom, @Nonnull Version formatVersion ) throws XMLStreamException {
        assert isVersionReadable( formatVersion );
        deserializeFrom.next();
        String text = deserializeFrom.getText();
        closeTag( deserializeFrom );
        return text;
      }
    };
  }

  @Override
  protected void verifySerialized(@Nonnull Entry<String> entry, @Nonnull byte[] serialized) throws Exception {
    super.verifySerialized(entry, serialized);
    assertTrue(XmlCommons.format(new String(serialized, StandardCharsets.UTF_8)), new String(serialized, StandardCharsets.UTF_8).contains("xmlns=\"http://www.lang.java/String/1.5.3\""));
  }

  @Override
  protected void verifyDeserialized(@Nonnull String deserialized, @Nonnull String original) {
    super.verifyDeserialized(deserialized, original);
    assertEquals("asdf", deserialized);
  }

  public static final Entry<? extends String> ENTRY1 = create("asdf", "<aString xmlns=\"http://www.lang.java/String/1.5.3\">asdf</aString>");

  @Test
  public void testNoVersion() {
    Assertions.assertThrows(VersionException.class, () -> {
      getSerializer().deserialize(new ByteArrayInputStream("<aString>asdf</aString>".getBytes(StandardCharsets.UTF_8)));
    });
  }

  @Test
  public void testWrongVersion() {
    Assertions.assertThrows(VersionMismatchException.class, () -> {
      getSerializer().deserialize(new ByteArrayInputStream("<aString xmlns=\"http://www.lang.java/String/0.9.9\">asdf</aString>".getBytes(StandardCharsets.UTF_8)));
    });
  }

  @Test
  public void testWrongNamespaceVersion() {
    Assertions.assertThrows(SerializationException.class, () -> {
      getSerializer().deserialize(new ByteArrayInputStream("<aString xmlns=\"http://www.lang.invalid.java/String/1.5.3\">asdf</aString>".getBytes(StandardCharsets.UTF_8)));
    });
  }
}
