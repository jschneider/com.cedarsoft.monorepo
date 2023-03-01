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

import org.w3c.dom.Document
import org.w3c.dom.NamedNodeMap
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.util.ArrayDeque
import java.util.Deque
import javax.annotation.Nonnull

/**
 * XML Namespace translator
 */
class XmlNamespaceTranslator {
  private val translations: MutableMap<Key<String?>, Value<String>> = HashMap()

  fun addTranslation(fromNamespaceURI: String?, @Nonnull toNamespaceURI: String): XmlNamespaceTranslator {
    val key = Key(fromNamespaceURI)
    val value = Value(toNamespaceURI)
    translations[key] = value
    return this
  }

  fun translateNamespaces(@Nonnull xmlDoc: Document, addNsToAttributes: Boolean) {
    val nodes: Deque<Node> = ArrayDeque()
    nodes.push(xmlDoc.documentElement)
    while (!nodes.isEmpty()) {
      var node = nodes.pop()
      when (node.nodeType) {
        Node.ATTRIBUTE_NODE, Node.ELEMENT_NODE -> {
          val value = translations[Key(node.namespaceURI)]
          if (value != null) {
            // the reassignment to node is very important. as per javadoc renameNode will
            // try to modify node (first parameter) in place. If that is not possible it
            // will replace that node for a new created one and return it to the caller.
            // if we did not reassign node we will get no childs in the loop below.
            node = xmlDoc.renameNode(node, value.value, node.nodeName)
          }
        }
      }
      if (addNsToAttributes) {
        // for attributes of this node
        val attributes: NamedNodeMap? = node.attributes
        if (!(attributes == null || attributes.length == 0)) {
          var i = 0
          val count = attributes.length
          while (i < count) {
            val attribute = attributes.item(i)
            if (attribute != null) {
              nodes.push(attribute)
            }
            ++i
          }
        }
      }

      // for child nodes of this node
      val childNodes: NodeList? = node.childNodes
      if (!(childNodes == null || childNodes.length == 0)) {
        var i = 0
        val count = childNodes.length
        while (i < count) {
          val childNode = childNodes.item(i)
          if (childNode != null) {
            nodes.push(childNode)
          }
          ++i
        }
      }
    }
  }

  // these will allow null values to be stored on a map so that we can distinguish
  // from values being on the map or not. map implementation returns null if the there
  // is no map element with a given key. If the value is null there is no way to
  // distinguish from value not being on the map or value being null. these classes
  // remove ambiguity.
  private open class Holder<T>(val value: T?) {
    override fun hashCode(): Int {
      val prime = 31
      var result = 1
      result = prime * result + if (value == null) 0 else value.hashCode()
      return result
    }

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other == null) return false
      if (javaClass != other.javaClass) return false
      val otherHolder = other as Holder<*>
      if (value == null) {
        if (otherHolder.value != null) return false
      } else if (value != otherHolder.value) return false
      return true
    }
  }

  private class Key<T>(value: T) : Holder<T>(value)
  private class Value<T>(value: T) : Holder<T>(value)
}
