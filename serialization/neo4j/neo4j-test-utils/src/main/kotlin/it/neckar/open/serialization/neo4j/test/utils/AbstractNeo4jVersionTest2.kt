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

import it.neckar.open.serialization.Serializer
import it.neckar.open.serialization.neo4j.AbstractNeo4jSerializer
import it.neckar.open.serialization.test.utils.VersionEntry
import it.neckar.open.version.Version
import com.google.common.base.Charsets
import org.apache.commons.io.IOUtils
import org.junit.experimental.theories.Theory
import org.junit.jupiter.api.BeforeEach
import org.neo4j.graphdb.GraphDatabaseService
import java.io.IOException
import java.net.URL
import java.nio.charset.StandardCharsets
import javax.annotation.Nonnull

/**
 *
 */
@WithNeo4j
abstract class AbstractNeo4jVersionTest2<T : Any> {
  private lateinit var db: GraphDatabaseService

  @BeforeEach
  fun setup(@Nonnull dbService: GraphDatabaseService) {
    db = dbService
  }

  /**
   * This method checks old serialized objects
   *
   * @throws IOException if there is an io problem
   * @throws SAXException if there is an xml parsing problem
   */
  @Theory
  @Throws(Exception::class)
  fun testVersion(entry: VersionEntry) {
    val serializer = getSerializer()
    val version = entry.version
    val serialized = String(entry.getSerialized(serializer), Charsets.UTF_8)
    val deserialized = deserialize(serializer, serialized)
    verifyDeserialized(deserialized, version)
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

  /**
   * Returns the serializer
   *
   * @return the serializer
   */
  @Throws(Exception::class)
  protected abstract fun getSerializer(): AbstractNeo4jSerializer<T>

  /**
   * Verifies the deserialized object.
   *
   * @param deserialized the deserialized object
   * @param version      the version
   */
  @Throws(Exception::class)
  protected abstract fun verifyDeserialized(deserialized: T, version: Version)

  class Neo4jVersionEntry(
    override val version: Version,
    private val cypher: ByteArray
  ) : VersionEntry {

    constructor(version: Version, cypher: String) : this(version, cypher.toByteArray(StandardCharsets.UTF_8)) {}

    @Throws(Exception::class)
    override fun getSerialized(@Nonnull serializer: Serializer<*, *, *>): ByteArray {
      return cypher
    }
  }

  companion object {
    @JvmStatic
    protected fun create(version: Version, json: String): VersionEntry {
      return Neo4jVersionEntry(version, json)
    }

    @JvmStatic
    protected fun create(version: Version, expected: URL): VersionEntry {
      return try {
        Neo4jVersionEntry(version, IOUtils.toByteArray(expected.openStream()))
      } catch (e: IOException) {
        throw RuntimeException(e)
      }
    }
  }
}
