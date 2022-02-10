package com.cedarsoft.commons.javafx

import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.layout.Region


/**
 * Calls the [callback] for each node. Visits all children
 */
fun Node.visitSceneGraph(callback: (node: Node, level: Int) -> Unit) {
  this.visitSceneGraph(0, callback)
}

/**
 * Calls the [callback] for each node. Visits all children
 */
fun Node.visitSceneGraph(level: Int = 0, callback: (node: Node, level: Int) -> Unit) {
  callback(this, level)

  if (this is Parent) {
    this.childrenUnmodifiable.forEach { it.visitSceneGraph(level + 1, callback) }
  }
}

/**
 * Dumps the scene graph as string
 */
fun Node.dumpSceneGraph(): String {
  return buildString {
    visitSceneGraph { node, level ->
      val indents = " ".repeat(level)

      append("$indents${node.javaClass.name} -StyleClass: ${node.styleClass} -Style: ${node.style} -LayoutBounds: ${node.layoutBounds} -LayoutBoundsInParent: ${node.boundsInParent}")

      if (node is Region) {
        val insets = node.insets
        val padding = node.padding
        append(" -Insets: $insets -Padding: $padding")
      }

      appendLine()
    }
  }
}


