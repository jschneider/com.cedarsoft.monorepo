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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.*;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.schema.IndexDefinition;
import org.neo4j.graphdb.schema.Schema;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class SchemaTest extends AbstractNeo4JTest {
  @Test
  public void testSchemaTest() throws Exception {
    IndexDefinition indexDefinition;

    try (Transaction tx = graphDb.beginTx()) {
      Schema schema = graphDb.schema();
      indexDefinition = schema.indexFor(NodeLabel.USER).on("username").create();
      tx.success();
    }


    try (Transaction tx = graphDb.beginTx()) {
      Schema schema = graphDb.schema();
      schema.awaitIndexOnline(indexDefinition, 10, TimeUnit.SECONDS);
    }

    try (Transaction tx = graphDb.beginTx()) {

      // Create some users
      for (int id = 0; id < 100; id++) {
        Node userNode = graphDb.createNode(NodeLabel.USER);
        userNode.setProperty("username", "user" + id + "@neo4j.org");
      }
      System.out.println("Users created");
      tx.success();
    }


    int idToFind = 45;
    String nameToFind = "user" + idToFind + "@neo4j.org";

    try (Transaction tx = graphDb.beginTx()) {
      ResourceIterator<Node> users = graphDb.findNodes(NodeLabel.USER, "username", nameToFind);
      List<Node> userNodes = new ArrayList<>();
      while (users.hasNext()) {
        userNodes.add(users.next());
      }

      for (Node node : userNodes) {
        System.out.println("The username of user " + idToFind + " is " + node.getProperty("username"));
      }
    }
  }

  public enum NodeLabel implements Label {
    USER
  }
}
