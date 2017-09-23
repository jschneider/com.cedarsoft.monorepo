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

import org.junit.rules.*;
import org.junit.runner.*;
import org.junit.runners.model.*;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.test.TestGraphDatabaseFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Rule that provides neo4j databases
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@Deprecated
public class Neo4jRule implements TestRule {
  @Nonnull
  private final TemporaryFolder tmp = new TemporaryFolder();

  @Override
  public Statement apply( final Statement base, Description description ) {
    return tmp.apply( new Statement() {
      @Override
      public void evaluate() throws Throwable {
        GraphDatabaseService db = createDb();
        try {
          base.evaluate();
        } catch ( Throwable e ) {
          dump(db);
          throw e;
        } finally {
          after();
        }
      }
    }, description );
  }

  public static void dump( @Nonnull GraphDatabaseService db ) {
    if ( "true".equalsIgnoreCase( System.getProperty( "neo4j.dumpOnError" ) ) ) {
      try {
        System.err.println( dumpToText(db) );
        Graphviz.toPng( db );
      } catch ( Exception e ) {
        e.printStackTrace();
      }
    }
  }

  @Nonnull
  public static String dumpToText( @Nonnull GraphDatabaseService db ) {
    try ( Transaction tx = db.beginTx() ) {
      Result result = db.execute("MATCH (n)\n" + "RETURN n;");
      return result.resultAsString();
    }
  }

  @Nonnull
  private final List<GraphDatabaseService> dbs = new ArrayList<>();

  @Nonnull
  public GraphDatabaseService createDb() throws IOException {
    GraphDatabaseService db = new TestGraphDatabaseFactory().newImpermanentDatabase(tmp.newFolder());
    dbs.add( db );
    return db;
  }

  private void after() {
    for ( GraphDatabaseService db : dbs ) {
      db.shutdown();
    }
  }
}
