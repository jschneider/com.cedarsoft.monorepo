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

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.annotation.Nonnull;

import org.apache.commons.io.IOUtils;
import org.junit.experimental.theories.*;
import org.junit.jupiter.api.*;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.xml.sax.SAXException;

import com.cedarsoft.serialization.Serializer;
import com.cedarsoft.serialization.neo4j.AbstractNeo4jSerializer;
import com.cedarsoft.serialization.test.utils.VersionEntry;
import com.cedarsoft.version.Version;
import com.google.common.base.Charsets;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@WithNeo4j
public abstract class AbstractNeo4jVersionTest2<T> {

  private GraphDatabaseService db;

  @BeforeEach
  public void setup(@Nonnull GraphDatabaseService dbService) {
    this.db = dbService;
  }

  /**
   * This method checks old serialized objects
   *
   * @throws IOException if there is an io problem
   * @throws SAXException if there is an xml parsing problem
   */
  @Theory
  public void testVersion( @Nonnull VersionEntry entry ) throws Exception {
    AbstractNeo4jSerializer<T> serializer = getSerializer();

    Version version = entry.getVersion();
    String serialized = new String( entry.getSerialized( serializer ), Charsets.UTF_8 );

    T deserialized = deserialize( serializer, serialized );
    verifyDeserialized( deserialized, version );
  }

  @Nonnull
  private T deserialize( @Nonnull AbstractNeo4jSerializer<T> serializer, @Nonnull String serialized ) throws IOException {
    //Fill the db initially
    try ( Transaction tx = db.beginTx() ) {
      Result result = db.execute(serialized );
      tx.success();
    }

    try (Transaction tx = db.beginTx()) {
      return serializer.deserialize( db.getNodeById( 0 ) );
    }
  }

  /**
   * Returns the serializer
   *
   * @return the serializer
   */
  @Nonnull
  protected abstract AbstractNeo4jSerializer<T> getSerializer() throws Exception;

  /**
   * Verifies the deserialized object.
   *
   * @param deserialized the deserialized object
   * @param version      the version
   */
  protected abstract void verifyDeserialized( @Nonnull T deserialized, @Nonnull Version version ) throws Exception;


  @Nonnull
  protected static VersionEntry create( @Nonnull Version version, @Nonnull String json ) {
    return new Neo4jVersionEntry( version, json );
  }

  @Nonnull
  protected static VersionEntry create( @Nonnull Version version, @Nonnull URL expected ) {
    try {
      return new Neo4jVersionEntry( version, IOUtils.toByteArray( expected.openStream() ) );
    } catch ( IOException e ) {
      throw new RuntimeException( e );
    }
  }

  public static class Neo4jVersionEntry implements VersionEntry {
    @Nonnull
    private final Version version;
    @Nonnull
    private final byte[] cypher;

    public Neo4jVersionEntry( @Nonnull Version version, @Nonnull String cypher ) {
      this( version, cypher.getBytes(StandardCharsets.UTF_8) );
    }

    public Neo4jVersionEntry( @Nonnull Version version, @Nonnull byte[] cypher ) {
      this.version = version;
      //noinspection AssignmentToCollectionOrArrayFieldFromParameter
      this.cypher = cypher;
    }

    @Nonnull
    @Override
    public Version getVersion() {
      return version;
    }

    @Nonnull
    @Override
    public byte[] getSerialized( @Nonnull Serializer<?, ?, ?> serializer ) throws Exception {
      return cypher;
    }
  }
}
