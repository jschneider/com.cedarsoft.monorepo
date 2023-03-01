package it.neckar.open.serialization.neo4j.test.utils

import it.neckar.open.serialization.Serializer
import it.neckar.open.serialization.neo4j.AbstractNeo4jSerializer
import it.neckar.open.serialization.test.utils.Entry
import it.neckar.open.serialization.test.utils.ReflectionEquals
import it.neckar.open.test.utils.ByTypeSource
import com.google.common.base.Charsets
import org.apache.commons.io.IOUtils
import org.assertj.core.api.Assertions
import org.fest.reflect.core.Reflection
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.neo4j.cypher.export.DatabaseSubGraph
import org.neo4j.cypher.export.SubGraphExporter
import org.neo4j.graphdb.GraphDatabaseService
import org.neo4j.graphdb.Node
import java.io.IOException
import java.io.InputStream
import java.io.PrintWriter
import java.io.StringWriter
import java.net.URL
import java.nio.charset.StandardCharsets
import javax.annotation.Nonnull

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
/**
 * Abstract base class for neo4j based serializers.
 *
 *
 *
 *
 * Attention: it is necessary to define at least one DataPoint:
 *
 *
 * <pre>&#064;DataPoint<br></br>public static final Entry&lt;?&gt; ENTRY1 = create(<br></br> new DomainObject(),<br></br> &quot;{}&quot; );</pre>
 *
 * @param <T> the type of the serialized object
</T> */
@WithNeo4j
abstract class AbstractNeo4jSerializerTest2<T : Any> {
  private lateinit var db: GraphDatabaseService

  @BeforeEach
  fun setUp(@Nonnull db: GraphDatabaseService) {
    this.db = db
  }

  @Throws(Exception::class)
  protected fun verify(currentCypher: String, expectedCypher: String) {
    Assertions.assertThat(currentCypher).isEqualTo(expectedCypher)
  }

  @Throws(Exception::class)
  protected fun verifySerialized(entry: Entry<T>, serialized: String) {
    verify(serialized.trim { it <= ' ' }.replace("\r".toRegex(), ""), String(entry.expected, Charsets.UTF_8).trim { it <= ' ' })
  }

  @ParameterizedTest
  @ByTypeSource(type = Entry::class)
  @Throws(Exception::class)
  fun testSerializer(@Nonnull entry: Entry<*>) {
    val serializer = getSerializer()

    //Serialize
    val objectToSerialize = entry.objectToSerialize as T
    val serialized = serialize(serializer, objectToSerialize)

    //Verify
    val castEntry = entry as Entry<T>
    verifySerialized(castEntry, serialized)
    verifyDeserialized(deserialize(serializer, serialized), objectToSerialize)
  }

  @Throws(Exception::class)
  private fun deserialize(serializer: AbstractNeo4jSerializer<T>, serialized: String): T {
    //Fill the db initially
    db.beginTx().use { tx ->
      val result = db.execute(serialized)
      tx.success()
    }
    db.beginTx().use { tx -> return serializer.deserialize(db.getNodeById(0)) }
  }

  @Throws(Exception::class)
  protected fun serialize(serializer: Serializer<T, Node, Node>, objectToSerialize: T): String {
    db.beginTx().use { tx ->
      val rootNode = db.createNode()
      serializer.serialize(objectToSerialize, rootNode)
      tx.success()
    }

    //Creates the cyper representation for this database
    val out = StringWriter()
    db.beginTx().use { tx ->
      val graph = DatabaseSubGraph.from(db)
      SubGraphExporter(graph).export(PrintWriter(out))
      tx.success()
    }
    return out.toString()
  }

  /**
   * Returns the serializer
   *
   * @return the serializer
   */
  @Throws(Exception::class)
  protected abstract fun getSerializer(): AbstractNeo4jSerializer<T>

  /**
   * Verifies the deserialized object
   *
   * @param deserialized the deserialized object
   * @param original     the original
   */
  protected fun verifyDeserialized(deserialized: T, original: T) {
    Assert.assertEquals(original, deserialized)
    Assert.assertThat(deserialized, CoreMatchers.`is`(ReflectionEquals(original)))
  }

  companion object {
    @JvmStatic
    fun <T> create(objectToSerialize: T, expected: ByteArray?): Entry<out T> {
      return Entry(objectToSerialize, expected!!)
    }

    @JvmStatic
    fun <T> create(objectToSerialize: T, expected: URL?): Entry<out T> {
      checkNotNull(expected) { "No resource found for <$objectToSerialize>" }
      return try {
        Entry(objectToSerialize, IOUtils.toByteArray(expected.openStream()))
      } catch (e: IOException) {
        throw RuntimeException(e)
      }
    }

    @JvmStatic
    fun <T> create(objectToSerialize: T, expected: InputStream): Entry<out T> {
      return try {
        Entry(objectToSerialize, IOUtils.toByteArray(expected))
      } catch (e: IOException) {
        throw RuntimeException(e)
      }
    }

    @JvmStatic
    protected fun getType(serializer: Serializer<*, *, *>): String {
      return Reflection.method("getType").withReturnType(String::class.java).`in`(serializer).invoke()
    }

    @JvmStatic
    protected fun <T> create(objectToSerialize: T, expected: String): Entry<out T> {
      return Entry(objectToSerialize, expected.toByteArray(StandardCharsets.UTF_8))
    }
  }
}
