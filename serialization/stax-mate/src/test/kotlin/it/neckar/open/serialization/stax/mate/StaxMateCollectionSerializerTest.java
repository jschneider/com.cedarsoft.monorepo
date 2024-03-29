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

package it.neckar.open.serialization.stax.mate;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.codehaus.staxmate.out.SMOutputElement;
import org.junit.jupiter.api.*;

import it.neckar.open.serialization.test.utils.AbstractXmlSerializerTest2;
import it.neckar.open.serialization.test.utils.Entry;

import it.neckar.open.version.Version;
import it.neckar.open.version.VersionRange;

/**
 *
 */
public class StaxMateCollectionSerializerTest extends AbstractXmlSerializerTest2<List<String>> {
  @Nonnull
  @Override
  protected AbstractStaxMateSerializer<List<String>> getSerializer() {
    return new AbstractStaxMateSerializer<List<String>>( "aString", "http://aString", new VersionRange( new Version( 1, 0, 0 ), new Version( 1, 0, 0 ) ) ) {
      @Override
      public void serialize(@Nonnull SMOutputElement serializeTo, @Nonnull List<String> objectToSerialize, @Nonnull Version formatVersion ) throws XMLStreamException {
        assert isVersionWritable( formatVersion );
        for ( String s : objectToSerialize) {
          serializeTo.addElement( serializeTo.getNamespace(), "string" ).addCharacters( s );
        }

        serializeTo.addElement( serializeTo.getNamespace(), "description" ).addCharacters( "descr" );
      }

      @Override
      @Nonnull
      public List<String> deserialize( @Nonnull XMLStreamReader deserializeFrom, @Nonnull Version formatVersion ) throws XMLStreamException, IOException {
        assert isVersionReadable( formatVersion );
        final List<String> strings = new ArrayList<String>();

        final boolean[] called = {false};

        visitChildren( deserializeFrom, new CB() {
          @Override
          public void tagEntered( @Nonnull XMLStreamReader deserializeFrom, @Nonnull String tagName ) throws XMLStreamException, IOException {
            if ( tagName.equals( "description" ) ) {
              assertEquals( "descr", getText( deserializeFrom ) );
              called[0] = true;
            } else {
              strings.add( getText( deserializeFrom ) );
            }
          }
        } );

        assertTrue( called[0] );

        return strings;
      }
    };
  }

  @Nonnull
  public static final Entry<? extends List<String>> ENTRY1 = create(Arrays.asList("1", "2", "3"), "<aString><string>1</string><string>2</string><string>3</string><description>descr</description></aString>");

  @Override
  protected void verifyDeserialized(@Nonnull List<String> deserialized, @Nonnull List<String> original) {
    Assertions.assertEquals(original, deserialized);
  }
}
