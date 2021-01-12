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

import com.cedarsoft.serialization.SerializationException
import com.cedarsoft.serialization.SerializingStrategy
import com.cedarsoft.serialization.SerializingStrategySupport
import com.cedarsoft.serialization.VersionMapping
import com.cedarsoft.version.Version
import com.cedarsoft.version.VersionRange
import org.neo4j.graphdb.Node
import org.neo4j.graphdb.NotFoundException
import java.io.IOException

/**
 * Abstract base class for all neo4j serializers
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
open class AbstractDelegatingNeo4jSerializer<T : Any>
protected constructor(
  nameSpaceUriBase: String,
  formatVersionRange: VersionRange
) : AbstractNeo4jSerializer<T>(nameSpaceUriBase, formatVersionRange) {

  val serializingStrategySupport: SerializingStrategySupport<T, Node, Node, Node, Node> = SerializingStrategySupport(formatVersionRange)

  val strategies: Collection<SerializingStrategy<out T, Node, Node, Node, Node>>
    get() = serializingStrategySupport.getStrategies()

  /** @noinspection RefusedBequest
   */
  override fun addVersion(serializeTo: Node) {
    serializeTo.setProperty(PROPERTY_DELEGATING_FORMAT_VERSION, formatVersion.toString())
  }

  /** @noinspection RefusedBequest
   */
  override fun readVersion(inNode: Node): Version {
    return Version.parse(inNode.getProperty(PROPERTY_DELEGATING_FORMAT_VERSION) as String)
  }

  @Throws(IOException::class)
  override fun serializeInternal(serializeTo: Node, objectToSerialize: T, formatVersion: Version) {
    assert(isVersionWritable(formatVersion))

    val strategy = serializingStrategySupport.findStrategy(objectToSerialize)
    val resolvedVersion = serializingStrategySupport.resolveVersion(strategy, formatVersion)

    serializeTo.setProperty(PROPERTY_SUB_TYPE, strategy.id)

    strategy.serialize(serializeTo, objectToSerialize, resolvedVersion)
  }

  @Throws(Exception::class)
  override fun deserialize(deserializeFrom: Node, formatVersion: Version): T {
    assert(isVersionReadable(formatVersion))

    try {
      val subType = deserializeFrom.getProperty(PROPERTY_SUB_TYPE) as String
      val strategy = serializingStrategySupport.findStrategy(subType)
      val resolvedVersion = serializingStrategySupport.resolveVersion(strategy, formatVersion)
      return strategy.deserialize(deserializeFrom, resolvedVersion)

    } catch (e: NotFoundException) {
      throw SerializationException(e, PROPERTY_SUB_TYPE, SerializationException.Details.INVALID_NODE, deserializeFrom.id, deserializeFrom.propertyKeys)
    }
  }

  fun addStrategy(strategy: SerializingStrategy<out T, Node, Node, Node, Node>): VersionMapping {
    return serializingStrategySupport.addStrategy(strategy)
  }

  companion object {
    const val PROPERTY_DELEGATING_FORMAT_VERSION: String = "delegatingFormatVersion"

    /**
     * Used for delegating serializers
     */
    const val PROPERTY_SUB_TYPE: String = "subtype"
  }
}
