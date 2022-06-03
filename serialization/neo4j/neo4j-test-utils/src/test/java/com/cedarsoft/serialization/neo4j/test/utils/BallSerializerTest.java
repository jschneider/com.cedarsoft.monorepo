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
package com.cedarsoft.serialization.neo4j.test.utils;

import static org.junit.Assert.*;

import javax.annotation.Nonnull;

import org.junit.jupiter.api.*;
import org.neo4j.graphdb.Node;

import com.cedarsoft.serialization.SerializingStrategy;
import com.cedarsoft.serialization.ToString;
import com.cedarsoft.serialization.test.utils.Entry;
import com.cedarsoft.serialization.ui.VersionMappingsVisualizer;
import com.cedarsoft.version.Version;

/**
 */
public class BallSerializerTest extends AbstractNeo4jSerializerTest2<Ball> {
  @Nonnull
  @Override
  protected BallSerializer getSerializer() throws Exception {
    return new BallSerializer();
  }

  public static final Entry<?> ENTRY1 = create(
    new Ball.TennisBall( 7 ), BallSerializerTest.class.getResource( "ball1_2.cypher" )
  );

  public static final Entry<?> ENTRY2 = create(
    new Ball.BasketBall( "asdf" ), BallSerializerTest.class.getResource( "ball2_2.cypher" ) );


  @Test
  public void testAscii() throws Exception {
    assertEquals( 2, getSerializer().getSerializingStrategySupport().getVersionMappings().getMappings().size() );
    assertEquals( "         -->  basketBa  tennisBa\n" +
                    "--------------------------------\n" +
                    "   1.0.0 -->     2.0.0     1.5.0\n" +
                    "   1.1.0 -->     2.0.1     1.5.1\n" +
                    "--------------------------------\n", VersionMappingsVisualizer.toString( getSerializer().getSerializingStrategySupport().getVersionMappings(), new ToString<SerializingStrategy<? extends Ball, Node, Node, Node, Node>>() {
      @Nonnull
      @Override
      public String convert(@Nonnull SerializingStrategy<? extends Ball, Node, Node, Node, Node> toConvert) {
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
