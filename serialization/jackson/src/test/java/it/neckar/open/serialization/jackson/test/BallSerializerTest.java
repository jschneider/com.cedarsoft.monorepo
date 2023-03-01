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

package it.neckar.open.serialization.jackson.test;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.Nonnull;

import org.junit.jupiter.api.*;

import it.neckar.open.serialization.SerializingStrategy;
import it.neckar.open.serialization.ToString;
import it.neckar.open.serialization.test.utils.AbstractJsonSerializerTest2;
import it.neckar.open.serialization.test.utils.Entry;
import it.neckar.open.serialization.ui.VersionMappingsVisualizer;

import it.neckar.open.version.Version;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

/**
 */
public class BallSerializerTest extends AbstractJsonSerializerTest2<Ball> {
  @Nonnull
  @Override
  protected BallSerializer getSerializer() throws Exception {
    return new BallSerializer();
  }

  public static final Entry<?> ENTRY1 = create(new Ball.TennisBall( 7 ), "{\"@subtype\" : \"tennisBall\",\"id\" : 7}");

  public static final Entry<?> ENTRY2 = create(new Ball.BasketBall( "asdf" ), "{\"@subtype\" : \"basketBall\",\"theId\" : \"asdf\"}" );


  @Test
  public void testAsccii() throws Exception {
    assertEquals( 2, getSerializer().getSerializingStrategySupport().getVersionMappings().getMappings().size() );
    assertEquals( "         -->  basketBa  tennisBa\n" +
                    "--------------------------------\n" +
                    "   1.0.0 -->     2.0.0     1.5.0\n" +
                    "   1.1.0 -->     2.0.1     1.5.1\n" +
                    "--------------------------------\n", VersionMappingsVisualizer.toString( getSerializer().getSerializingStrategySupport().getVersionMappings(), new ToString<SerializingStrategy<? extends Ball, JsonGenerator, JsonParser, OutputStream, InputStream>>() {
      @Nonnull
      @Override
      public String convert(@Nonnull SerializingStrategy<? extends Ball, JsonGenerator, JsonParser, OutputStream, InputStream> toConvert) {
        return toConvert.getId();
      }
    } ) );
  }

  @Test
  public void testVersion() throws Exception {
    BallSerializer serializer = getSerializer();
    assertTrue( serializer.isVersionReadable( Version.valueOf( 1, 0, 0 ) ) );
    assertFalse( serializer.isVersionReadable( Version.valueOf( 1, 2, 1 ) ) );
    assertFalse( serializer.isVersionReadable( Version.valueOf( 0, 9, 9 ) ) );

    assertTrue( serializer.isVersionWritable( Version.valueOf( 1, 1, 0 ) ) );
    assertFalse( serializer.isVersionWritable( Version.valueOf( 1, 1, 1 ) ) );
    assertFalse( serializer.isVersionWritable( Version.valueOf( 1, 0, 9 ) ) );
  }
}
