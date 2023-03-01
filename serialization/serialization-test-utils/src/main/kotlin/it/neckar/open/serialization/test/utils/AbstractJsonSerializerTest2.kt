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
package it.neckar.open.serialization.test.utils

import it.neckar.open.serialization.Serializer
import it.neckar.open.serialization.StreamSerializer
import it.neckar.open.test.utils.JsonUtils
import it.neckar.open.version.Version
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ContainerNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.TextNode
import org.fest.reflect.core.Reflection
import java.nio.charset.StandardCharsets

/**
 * Abstract base class for JSON based serializers.
 *
 *
 * Attention: it is necessary to define at least one DataPoint:
 *
 *
 * <pre>&#064;DataPoint<br></br>public static final Entry&lt;?&gt; ENTRY1 = create(<br></br> new DomainObject(),<br></br> &quot;{}&quot; );</pre>
 *
 * @param <T> the type of the serialized object
</T> */
abstract class AbstractJsonSerializerTest2<T : Any> : AbstractSerializerTest2<T>() {

  @Throws(Exception::class)
  abstract override fun getSerializer(): StreamSerializer<T>

  @Throws(Exception::class)
  protected fun verify(current: ByteArray, expectedJson: ByteArray) {
    var expectedAsString = String(expectedJson, StandardCharsets.UTF_8)
    if (addTypeInformation()) {
      try {
        expectedAsString = addTypeInformation(expectedJson)
      } catch (e: Exception) {
        System.err.println("WARNING. Could not add type information due to " + e.message)
      }
    }
    JsonUtils.assertJsonEquals(expectedAsString, String(current, StandardCharsets.UTF_8))
  }

  @Throws(Exception::class)
  fun addTypeInformation(expectedJson: ByteArray): String {
    val serializer: Serializer<T, *, *> = getSerializer()
    return addTypeInformation(serializer, expectedJson)
  }

  protected open fun addTypeInformation(): Boolean {
    return true
  }

  @Throws(Exception::class)
  override fun verifySerialized(entry: Entry<T>, serialized: ByteArray) {
    verify(serialized, entry.expected)
  }

  companion object {
    @JvmStatic
    fun getType(serializer: Serializer<*, *, *>): String {
      return Reflection.method("getType").withReturnType(String::class.java).`in`(serializer).invoke()
    }

    @JvmStatic
    @Throws(Exception::class)
    fun addTypeInformation(serializer: Serializer<*, *, *>, expectedJson: ByteArray): String {
      return addTypeInformation(getType(serializer), serializer.formatVersion, expectedJson)
    }

    @JvmStatic
    @Throws(Exception::class)
    fun addTypeInformation(type: String, version: Version, xmlBytes: ByteArray): String {
      val tree = ObjectMapper().readTree(String(xmlBytes, StandardCharsets.UTF_8))
      val newProps: MutableMap<String, JsonNode> = LinkedHashMap()
      newProps["@type"] = TextNode(type)
      newProps["@version"] = TextNode(version.format())
      val nodeIterator = tree.fields()
      while (nodeIterator.hasNext()) {
        val (key, value) = nodeIterator.next()
        newProps[key] = value
      }
      (tree as ContainerNode<*>).removeAll()
      (tree as ObjectNode).setAll<JsonNode>(newProps)
      return tree.toString()
    }

    @JvmStatic
    protected fun <T> create(objectToSerialize: T, expected: String): Entry<out T> {
      return Entry(objectToSerialize, expected.toByteArray(StandardCharsets.UTF_8))
    }
  }
}
