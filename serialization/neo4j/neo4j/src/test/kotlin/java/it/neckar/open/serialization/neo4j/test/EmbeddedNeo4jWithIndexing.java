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
package it.neckar.open.serialization.neo4j.test;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;

import java.io.File;

public class EmbeddedNeo4jWithIndexing {
  private static final File DB_PATH = new File("neo4j-store");
  private static final String USERNAME_KEY = "username";
  private static GraphDatabaseService graphDb;
  private static Index<Node> nodeIndex;

  public static void main( final String[] args ) {
    // START SNIPPET: startDb
    graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
    registerShutdownHook();
    // END SNIPPET: startDb

    // START SNIPPET: addUsers
    Transaction tx = graphDb.beginTx();
    try {
      nodeIndex = graphDb.index().forNodes( "nodes" );

      // Create some users and index their names with the IndexService
      for ( int id = 0; id < 100; id++ ) {
        Node userNode = createAndIndexUser( idToUserName( id ) );
      }
      // END SNIPPET: addUsers
      System.out.println( "Users created" );

      // Find a user through the search index
      // START SNIPPET: findUser
      int idToFind = 45;
      String userName = idToUserName( idToFind );
      Node foundUser = nodeIndex.get( USERNAME_KEY, userName ).getSingle();
      System.out.println( "The username of user " + idToFind + " is "
                            + foundUser.getProperty( USERNAME_KEY ) );
      // END SNIPPET: findUser

      // Delete the persons and remove them from the index
      for ( Node user : nodeIndex.query( USERNAME_KEY, "*" ) ) {
        nodeIndex.remove( user, USERNAME_KEY,
                          user.getProperty( USERNAME_KEY ) );
        user.delete();
      }
      tx.success();
    } finally {
      tx.close();
    }
    System.out.println( "Shutting down database ..." );
    shutdown();
  }

  private static void shutdown() {
    graphDb.shutdown();
  }

  // START SNIPPET: helperMethods
  private static String idToUserName( final int id ) {
    return "user" + id + "@neo4j.org";
  }

  private static Node createAndIndexUser( final String username ) {
    Node node = graphDb.createNode();
    node.setProperty( USERNAME_KEY, username );
    nodeIndex.add( node, USERNAME_KEY, username );
    return node;
  }
  // END SNIPPET: helperMethods

  private static void registerShutdownHook() {
    // Registers a shutdown hook for the Neo4j and index service instances
    // so that it shuts down nicely when the VM exits (even if you
    // "Ctrl-C" the running example before it's completed)
    Runtime.getRuntime().addShutdownHook( new Thread() {
      @Override
      public void run() {
        shutdown();
      }
    } );
  }
}
