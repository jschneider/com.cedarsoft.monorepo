/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.cedarsoft.org/gpl3ce
 * (GPL 3 with Classpath Exception)
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
package it.neckar.open.serialization.neo4j.test.utils

import it.neckar.open.serialization.SerializationException
import com.ecyrd.speed4j.StopWatch
import com.google.common.collect.ImmutableList
import org.assertj.core.api.Assertions
import org.assertj.core.api.Fail
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.neo4j.graphdb.Label
import org.neo4j.graphdb.Node
import javax.annotation.Nonnull

/**
 */
class SampleNeo4jSerializerTest : AbstractNeo4JTest() {
  @Test
  @Throws(Exception::class)
  fun testIt() {
    val person = Person("Martha Musterfrau", Address("Musterstraße 7", "Musterstadt"), ImmutableList.of(Email("1"), Email("2"), Email("3")))
    val node = serialize(person)
    graphDb.beginTx().use { tx ->
      val deserialized = deserialize(node)
      Assertions.assertThat(deserialized.name).isEqualTo(person.name)
      Assertions.assertThat(deserialized.address.street).isEqualTo(person.address.street)
      val strings = listOf("a", "b", "c")
      Assertions.assertThat(strings).hasSize(3)
      Assertions.assertThat(deserialized.mails).hasSize(3)
      Assertions.assertThat(deserialized.mails as List<*>).containsExactly(Email("1"), Email("2"), Email("3"))
      tx.success()
    }
  }

  @Test
  @Throws(Exception::class)
  fun testWrongLabel() {
    val person = Person("Martha Musterfrau", Address("Musterstraße 7", "Musterstadt"), ImmutableList.of(Email("1"), Email("2"), Email("3")))
    var node: Node
    graphDb.beginTx().use { tx ->
      node = serialize(person)

      //remove label
      node.removeLabel(personSerializer.typeLabel)
      node.addLabel(Label.label("lab1"))
      node.addLabel(Label.label("lab2"))
      node.addLabel(Label.label("lab3"))
      tx.success()
    }

    //Now deserialize
    try {
      graphDb.beginTx().use { deserialize(node) }
      Fail.fail<Any>("Where is the Exception")
    } catch (e: SerializationException) {
      Assertions.assertThat(e.message).contains("[INVALID_TYPE] Invalid type. Expected <it.neckar.open.test.person> but was <").contains("lab1").contains("lab2").contains("lab3")
    }
  }

  @Disabled
  @Test
  @Throws(Exception::class)
  fun testPerformanceSerialization() {
    val transactionCount = 10000
    val person = Person("Martha Musterfrau", Address("Musterstraße 7", "Musterstadt"), ImmutableList.of(Email("1"), Email("2"), Email("3")))
    val stopWatch = StopWatch(javaClass.name)

    //without outer transaction
    for (i in 0 until transactionCount) {
      //First Serialize
      val node = serialize(person)
      Assertions.assertThat(node).isNotNull
    }
    stopWatch.stop("created $transactionCount persons without outer transaction")
    println(stopWatch.toString(transactionCount))
    graphDb.beginTx().use { tx ->
      for (i in 0 until transactionCount) {
        //First Serialize
        val node = serialize(person)
        Assertions.assertThat(node).isNotNull
      }
      tx.success()
    }
    stopWatch.stop("created $transactionCount persons")
    println(stopWatch.toString(transactionCount))
    stopWatch.start()
  }

  @Nonnull
  @Throws(Exception::class)
  private fun deserialize(@Nonnull node: Node): Person {
    return personSerializer.deserialize(node)
  }

  @Nonnull
  private val personSerializer = PersonSerializer()

  @Nonnull
  @Throws(Exception::class)
  private fun serialize(@Nonnull person: Person): Node {
    var node: Node
    graphDb.beginTx().use { tx ->
      node = graphDb.createNode()
      personSerializer.serialize(person, node)
      tx.success()
    }
    return node
  }
}
