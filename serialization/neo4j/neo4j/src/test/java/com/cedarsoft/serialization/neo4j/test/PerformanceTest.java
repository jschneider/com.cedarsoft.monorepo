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
package com.cedarsoft.serialization.neo4j.test;

import com.ecyrd.speed4j.StopWatch;
import org.junit.*;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.test.TestGraphDatabaseFactory;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class PerformanceTest {
  private GraphDatabaseService graphDb;

  @Before
  public void prepareTestDatabase() {
    graphDb = new TestGraphDatabaseFactory().newImpermanentDatabase();
  }

  @After
  public void destroyTestDatabase() {
    graphDb.shutdown();
  }

  @Ignore
  @Test
  public void testName() throws Exception {
    int nodes = 50000;
    int nodesPerTransaction = 30;
    int transactionCount = nodes / nodesPerTransaction;

    //Warm up
    for ( int i = 0; i < 5; i++ ) {
      run( transactionCount, nodesPerTransaction );
    }

    //Now live
    for ( int i = 0; i < 5; i++ ) {
      StopWatch stopWatch = new StopWatch( getClass().getName() );

      run( transactionCount, nodesPerTransaction );
      stopWatch.stop( "created " + transactionCount * nodesPerTransaction + " nodes" );
      System.out.println( stopWatch.toString( transactionCount * nodesPerTransaction ) );
    }
  }

  public void run( int transactionCount, int nodesPerTransaction ) throws Exception {
    Node root;
    try ( Transaction tx = graphDb.beginTx() ) {
      root = graphDb.createNode();
      tx.success();
    }

    for ( int i = 0; i < transactionCount; i++ ) {
      try ( Transaction tx = graphDb.beginTx() ) {
        for ( int j = 0; j < nodesPerTransaction; j++ ) {
          Node node = graphDb.createNode();
          node.setProperty( "name", 17);
          node.setProperty( "name2", 12.4 );
          root.createRelationshipTo( node, Relations.MARRIED );
        }
        tx.success();
      }
    }
  }
}
