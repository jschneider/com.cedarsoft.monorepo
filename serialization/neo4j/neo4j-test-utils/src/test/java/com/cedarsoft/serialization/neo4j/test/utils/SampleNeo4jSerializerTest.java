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

import com.cedarsoft.serialization.SerializationException;
import com.ecyrd.speed4j.StopWatch;
import com.google.common.collect.ImmutableList;
import org.assertj.core.api.*;
import org.junit.*;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class SampleNeo4jSerializerTest extends AbstractNeo4JTest {
  @Test
  public void testIt() throws Exception {
    Person person = new Person( "Martha Musterfrau", new Address( "Musterstraße 7", "Musterstadt" ), ImmutableList.of( new Email( "1" ), new Email( "2" ), new Email( "3" ) ) );

    Node node = serialize( person );

    //Now deserialize
    try ( Transaction tx = graphDb.beginTx() ) {
      Person deserialized = deserialize( node );
      assertThat( deserialized.getName() ).isEqualTo( person.getName() );
      assertThat( deserialized.getAddress().getStreet() ).isEqualTo( person.getAddress().getStreet() );

      List<? extends String> strings = Arrays.asList("a", "b", "c");
      assertThat(strings).hasSize(3 );

      assertThat( deserialized.getMails() ).hasSize( 3 );
      assertThat( (List )deserialized.getMails() ).containsExactly( new Email( "1" ), new Email( "2" ), new Email( "3" ) );
      tx.success();
    }
  }

  @Test
  public void testWrongLabel() throws Exception {
    Person person = new Person( "Martha Musterfrau", new Address( "Musterstraße 7", "Musterstadt" ), ImmutableList.of( new Email( "1" ), new Email( "2" ), new Email( "3" ) ) );

    Node node;
    try ( Transaction tx = graphDb.beginTx() ) {
      node = serialize( person );

      //remove label
      node.removeLabel(personSerializer.getTypeLabel());
      node.addLabel(Label.label("lab1"));
      node.addLabel(Label.label("lab2"));
      node.addLabel(Label.label("lab3"));

      tx.success();
    }

    //Now deserialize
    try {
      try ( Transaction tx = graphDb.beginTx() ) {
        deserialize(node);
      }
      fail("Where is the Exception");
    } catch (SerializationException e) {
      assertThat( e ).isNotNull().hasMessage("[INVALID_TYPE] Invalid type. Expected <com.cedarsoft.test.person> but was <[lab1, lab2, lab3]>.");
    }
  }

  @Ignore
  @Test
  public void testPerformanceSerialization() throws Exception {
    int transactionCount = 10000;
    Person person = new Person( "Martha Musterfrau", new Address( "Musterstraße 7", "Musterstadt" ), ImmutableList.of( new Email( "1" ), new Email( "2" ), new Email( "3" ) ) );

    StopWatch stopWatch = new StopWatch( getClass().getName() );

    //without outer transaction
    for ( int i = 0; i < transactionCount; i++ ) {
      //First Serialize
      Node node = serialize( person );
      assertThat( node ).isNotNull();
    }

    stopWatch.stop( "created " + transactionCount + " persons without outer transaction" );
    System.out.println( stopWatch.toString( transactionCount ) );


    try ( Transaction tx = graphDb.beginTx() ) {
      for ( int i = 0; i < transactionCount; i++ ) {
        //First Serialize
        Node node = serialize( person );
        assertThat( node ).isNotNull();
      }
      tx.success();
    }

    stopWatch.stop( "created " + transactionCount + " persons" );
    System.out.println( stopWatch.toString( transactionCount ) );

    stopWatch.start();
  }

  @Nonnull
  private  Person deserialize( @Nonnull Node node ) throws IOException {
    return personSerializer.deserialize( node );
  }

  @Nonnull
  private final PersonSerializer personSerializer = new PersonSerializer();

  @Nonnull
  private Node serialize( @Nonnull Person person ) throws IOException {
    Node node;
    try ( Transaction tx = graphDb.beginTx() ) {
      node = graphDb.createNode();
      personSerializer.serialize( person, node );
      tx.success();
    }
    return node;
  }


}
