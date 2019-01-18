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

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.helpers.collection.Iterators;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class CypherTest extends AbstractNeo4JTest {


  @Test
  public void testCypher1() throws Exception {
    try ( Transaction tx = graphDb.beginTx() ) {
      Node node1 = graphDb.createNode();
      node1.setProperty( "name", "MM" );

      Node node2 = graphDb.createNode();
      Node node3 = graphDb.createNode();
      Node node4 = graphDb.createNode();

      node1.createRelationshipTo( node2, PersonSerializer.Relations.ADDRESS );
      node1.createRelationshipTo( node3, PersonSerializer.Relations.ADDRESS );
      node2.createRelationshipTo( node3, PersonSerializer.Relations.ADDRESS );
      node3.createRelationshipTo( node4, PersonSerializer.Relations.ADDRESS );

      tx.success();
    }

    String query = "start n=node(*) where n.name = 'MM' return n, n.name";
    assertThat( graphDb.execute( query ).resultAsString().trim().replaceAll("\r", "") ).isEqualTo( "+-----------------------------+\n" +
                                                            "| n                  | n.name |\n" +
                                                            "+-----------------------------+\n" +
                                                            "| Node[0]{name:\"MM\"} | \"MM\"   |\n" +
                                                            "+-----------------------------+\n" +
                                                            "1 row" );

    assertThat( graphDb.execute( query ).columns() ).containsExactly( "n", "n.name" );

    boolean called = false;
    ResourceIterator<Node> nodeResourceIterator = graphDb.execute(query).<Node>columnAs("n");
    for ( Node node : Iterators.asIterable(nodeResourceIterator) ) {
      assertThat( node.getProperty( "name" ) ).isEqualTo( "MM" );
      called = true;
    }
    assertThat( called ).isTrue();
  }
}
