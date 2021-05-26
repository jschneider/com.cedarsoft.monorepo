/**
 * Copyright (C) cedarsoft GmbH.

 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at

 * http://www.cedarsoft.org/gpl3ce
 * (GPL 3 with Classpath Exception)

 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.

 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).

 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.

 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */
package com.cedarsoft.serialization.neo4j

import com.cedarsoft.serialization.AbstractSerializer
import com.cedarsoft.serialization.SerializationException
import com.cedarsoft.version.Version
import com.cedarsoft.version.VersionRange
import org.neo4j.graphdb.Direction
import org.neo4j.graphdb.Label
import org.neo4j.graphdb.Node
import org.neo4j.graphdb.RelationshipType
import java.io.IOException
import java.util.ArrayList
import java.util.Comparator
import java.util.HashMap

/**
 * Abstract base class for neo4j serializers
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
abstract class AbstractNeo4jSerializer<T : Any>
protected constructor(
  private val type: String,
  formatVersionRange: VersionRange
) : AbstractSerializer<T, Node, Node, Node, Node>(formatVersionRange) {

  /**
   * Returns the type label
   * @return the type label
   */
  val typeLabel: Label
    get() = Label.label(type)

  @Throws(Exception::class)
  override fun serialize(objectToSerialize: T, out: Node) {
    serialize(out, objectToSerialize, formatVersion)
  }

  @Throws(Exception::class)
  override fun serialize(serializeTo: Node, objectToSerialize: T, formatVersion: Version) {
    verifyVersionWritable(formatVersion)

    serializeTo.addLabel(typeLabel)
    addVersion(serializeTo)

    serializeInternal(serializeTo, objectToSerialize, formatVersion)
  }

  /**
   * Adds the type and version
   * @param serializeTo the node the type and version is added to
   */
  protected open fun addVersion(serializeTo: Node) {
    serializeTo.setProperty(PROPERTY_FORMAT_VERSION, formatVersion.toString())
  }

  /**
   * This method must be implemented by sub classes. Serialize the custom fields when necessary.<br></br>
   * This method is called from serialize(Node, Object, Version). The type label and format version have already been added to the node
   * @param serializeTo the node to serialize to
   * @param objectToSerialize the object
   * @param formatVersion the format version
   * @throws IOException if there is an io problem
   */
  @Throws(Exception::class)
  protected abstract fun serializeInternal(serializeTo: Node, objectToSerialize: T, formatVersion: Version)

  @Throws(Exception::class)
  override fun deserialize(deserializeFrom: Node): T {
    verifyType(deserializeFrom)

    val version = readVersion(deserializeFrom)
    verifyVersionReadable(version)

    return deserialize(deserializeFrom, version)
  }

  protected open fun readVersion(inNode: Node): Version {
    return Version.parse(inNode.getProperty(PROPERTY_FORMAT_VERSION) as String)
  }

  private fun verifyType(inNode: Node) {
    if (!inNode.hasLabel(typeLabel)) {
      throw SerializationException(SerializationException.Details.INVALID_TYPE, typeLabel, inNode.labels)
    }
  }

  @Throws(Exception::class)
  fun <A : Any> serializeWithRelationships(objects: Iterable<A>, type: Class<A>, node: Node, relationshipType: RelationshipType, formatVersion: Version) {
    var index = 0
    for (currentObject in objects) {
      serializeWithRelationship(currentObject, type, node, relationshipType, formatVersion, index)
      index++
    }
  }

  /**
   * Serializes the given object using a relation
   * @param object           the object that is serialized
   * @param type             the type
   * @param node             the (current) node that is the start for the relationship
   * @param relationshipType the type of the relationship
   * @param formatVersion    the format version
   * @param <A>              the type
   * @throws IOException if there is an io problem
  </A> */
  @Throws(IOException::class)
  fun <A : Any> serializeWithRelationship(objectToSerialize: A, type: Class<A>, node: Node, relationshipType: RelationshipType, formatVersion: Version) {
    serializeWithRelationship(objectToSerialize, type, node, relationshipType, formatVersion, null)
  }

  /**
   * Serializes with relationship. Adds an optional index
   */
  @Throws(IOException::class)
  protected fun <A : Any> serializeWithRelationship(objectToSerialize: A, type: Class<A>, node: Node, relationshipType: RelationshipType, formatVersion: Version, index: Int?) {
    val targetNode = node.graphDatabase.createNode()
    val relationship = node.createRelationshipTo(targetNode, relationshipType)

    //Add index to ensure order
    if (index != null) {
      relationship.setProperty(PROPERTY_ORDER_INDEX, index)
    }

    serialize(objectToSerialize, type, targetNode, formatVersion)
  }

  @Throws(IOException::class)
  fun <A : Any> deserializeWithRelationship(type: Class<A>, relationshipType: RelationshipType, node: Node, formatVersion: Version): A {
    val relationship = node.getSingleRelationship(relationshipType, Direction.OUTGOING)!!
    return deserialize(type, formatVersion, relationship.endNode)
  }

  @Throws(IOException::class)
  fun <A : Any> deserializeWithRelationships(type: Class<A>, relationshipType: RelationshipType, node: Node, formatVersion: Version): List<A> {
    val deserializedList = ArrayList<A>()
    val indices = HashMap<A, Int>()

    for (relationship in node.getRelationships(relationshipType, Direction.OUTGOING)) {
      val endNode = relationship.endNode
      val deserialized = deserialize(type, formatVersion, endNode)
      deserializedList.add(deserialized)

      val index = relationship.getProperty(PROPERTY_ORDER_INDEX) as Int
      indices.put(deserialized, index)
    }

    if (deserializedList.size > 1 && !indices.isEmpty()) {
      deserializedList.sortWith(Comparator<A> { o1, o2 ->
        val index1 = indices[o1]
        val index2 = indices[o2]

        if (index1 == null) {
          throw IllegalArgumentException("No index found for <$o1>")
        }
        if (index2 == null) {
          throw IllegalArgumentException("No index found for <$o2>")
        }

        index1.compareTo(index2)
      })
    }

    return deserializedList
  }

  companion object {
    /**
     * This property contains the format version for a node
     */
    const val PROPERTY_FORMAT_VERSION: String = "formatVersion"

    /**
     * Property used to identify the order
     */
    private const val PROPERTY_ORDER_INDEX = "orderIndex"
  }
}
