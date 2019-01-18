package com.cedarsoft.serialization.neo4j.test.utils;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.io.IOUtils;
import org.fest.reflect.core.Reflection;
import org.junit.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.neo4j.cypher.export.DatabaseSubGraph;
import org.neo4j.cypher.export.SubGraph;
import org.neo4j.cypher.export.SubGraphExporter;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

import com.cedarsoft.serialization.Serializer;
import com.cedarsoft.serialization.neo4j.AbstractNeo4jSerializer;
import com.cedarsoft.serialization.test.utils.Entry;
import com.cedarsoft.serialization.test.utils.ReflectionEquals;
import com.cedarsoft.test.utils.ByTypeSource;
import com.google.common.base.Charsets;

/**
 * Abstract base class for neo4j based serializers.
 * <p>
 * <p>
 * Attention: it is necessary to define at least one DataPoint:
 * <p>
 * <pre>&#064;DataPoint<br>public static final Entry&lt;?&gt; ENTRY1 = create(<br> new DomainObject(),<br> &quot;{}&quot; );</pre>
 *
 * @param <T> the type of the serialized object
 */
@WithNeo4j
public abstract class AbstractNeo4jSerializerTest2<T> {
  private GraphDatabaseService db;

  @BeforeEach
  void setUp(@Nonnull GraphDatabaseService db) {
    this.db = db;
  }

  @Nonnull
  public static <T> Entry<? extends T> create( @Nonnull T object, @Nonnull byte[] expected ) {
    return new Entry<T>( object, expected );
  }

  @Nonnull
  public static <T> Entry<? extends T> create( @Nonnull T object, @Nullable URL expected ) {
    if ( expected == null ) {
      throw new IllegalStateException( "No resource found for <" + object + ">" );
    }

    try {
      return new Entry<T>( object, IOUtils.toByteArray( expected.openStream() ) );
    } catch ( IOException e ) {
      throw new RuntimeException( e );
    }
  }

  @Nonnull
  public static <T> Entry<? extends T> create( @Nonnull T object, @Nonnull InputStream expected ) {
    try {
      return new Entry<>( object, IOUtils.toByteArray( expected ) );
    } catch ( IOException e ) {
      throw new RuntimeException( e );
    }
  }

  protected void verify( @Nonnull String currentCypher, @Nonnull String expectedCypher ) throws Exception {
    assertThat( currentCypher ).isEqualTo( expectedCypher );
  }

  @Nonnull
  protected static String getType( @Nonnull Serializer<?, ?, ?> serializer ) {
    return Reflection.method( "getType" ).withReturnType( String.class ).in( serializer ).invoke();
  }

  protected void verifySerialized( @Nonnull Entry<T> entry, @Nonnull String serialized ) throws Exception {
    verify(serialized.trim().replaceAll("\r", ""), new String(entry.getExpected(), Charsets.UTF_8).trim());
  }

  @Nonnull
  protected static <T> Entry<? extends T> create( @Nonnull T object, @Nonnull String expected ) {
    return new Entry<T>( object, expected.getBytes(StandardCharsets.UTF_8) );
  }

  @ParameterizedTest
  @ByTypeSource(type = Entry.class)
  public void testSerializer( @Nonnull Entry<?> entry ) throws Exception {
    AbstractNeo4jSerializer<T> serializer = getSerializer();

    //Serialize
    @SuppressWarnings("unchecked")
    T object = (T) entry.getObject();
    String serialized = serialize(serializer, object);

    //Verify
    @SuppressWarnings("unchecked")
    Entry<T> castEntry = (Entry<T>) entry;
    verifySerialized(castEntry, serialized );

    verifyDeserialized(deserialize( serializer, serialized ), object);
  }

  @Nonnull
  private T deserialize( @Nonnull AbstractNeo4jSerializer<T> serializer, @Nonnull String serialized ) throws IOException {
    //Fill the db initially
    try ( Transaction tx = db.beginTx() ) {
      Result  result = db.execute( serialized );
      tx.success();
    }

    try ( Transaction tx = db.beginTx() ) {
      return serializer.deserialize( db.getNodeById( 0 ) );
    }
  }

  @Nonnull
  protected String serialize( @Nonnull Serializer<T, Node, Node> serializer, @Nonnull T objectToSerialize ) throws IOException {
    try ( Transaction tx = db.beginTx() ) {
      Node rootNode = db.createNode();
      serializer.serialize( objectToSerialize, rootNode );

      tx.success();
    }

    //Creates the cyper representation for this database
    StringWriter out = new StringWriter();
    try ( Transaction tx = db.beginTx() ) {
      final SubGraph graph = DatabaseSubGraph.from( db );
      new SubGraphExporter( graph ).export( new PrintWriter( out ) );
      tx.success();
    }
    return out.toString();
  }

  /**
   * Returns the serializer
   *
   * @return the serializer
   */
  @Nonnull
  protected abstract AbstractNeo4jSerializer<T> getSerializer() throws Exception;

  /**
   * Verifies the deserialized object
   *
   * @param deserialized the deserialized object
   * @param original     the original
   */
  protected void verifyDeserialized( @Nonnull T deserialized, @Nonnull T original ) {
    assertEquals( original, deserialized );
    Assert.assertThat(deserialized, is(new ReflectionEquals(original)));
  }
}
