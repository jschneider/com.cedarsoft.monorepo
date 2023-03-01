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
package it.neckar.open.serialization.neo4j.test.utils;

import static org.assertj.core.api.Assertions.*;

import java.util.Map;

import javax.annotation.Nonnull;

import org.junit.jupiter.api.*;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.UniqueFactory;

/**
 */
public class UniqueNodesTest extends AbstractNeo4JTest {

  public static final String PROPERTY_LOGIN = "login";

  @Test
  public void testUnique1() throws Exception {
    //org.neo4j.graphdb.GraphDatabaseService
    UniqueFactory<Node> factory = createFactory();

    try ( Transaction tx = graphDb.beginTx() ) {
      Node musterfrau = factory.getOrCreate( PROPERTY_LOGIN, "musterfrau" );
      musterfrau.setProperty( "asdf", 1 );

      Node musterfrau2 = factory.getOrCreate( PROPERTY_LOGIN, "musterfrau" );

      assertThat( musterfrau ).isEqualTo( musterfrau2 );
      assertThat( musterfrau2.getProperty( "asdf" ) ).isEqualTo( 1 );
      tx.success();
    }
  }

  @Nonnull
  private UniqueFactory<Node> createFactory() {
    try ( Transaction tx = graphDb.beginTx() ) {
      UniqueFactory<Node> factory = new UniqueFactory.UniqueNodeFactory( graphDb, "users" ) {
        @Override
        protected void initialize( Node created, Map<String, Object> properties ) {
          created.setProperty( PROPERTY_LOGIN, properties.get( PROPERTY_LOGIN ) );
        }
      };
      tx.success();
      return factory;
    }
  }
}
