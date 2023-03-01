package it.neckar.open.javafx

import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.control.Labeled
import javafx.scene.control.TextInputControl
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

      val propertiesAsString = node.properties
        .filter { it.key != "javafx.scene.control.Tooltip" }
        .map { "${it.key}: ${it.value}" }.joinToString(", ")

      append("$indents${node.javaClass.name}" +
        "\n$indents -StyleClass: ${node.styleClass}" +
        "\n$indents -Style: ${node.style}" +
        "\n$indents -LayoutBounds: ${node.layoutBounds}" +
        "\n$indents -LayoutBoundsInParent: ${node.boundsInParent}")

      if (node is Region) {
        val insets = node.insets
        val padding = node.padding
        append("\n$indents -Insets: \n$insets -Padding: $padding")
      }

      if (node is Labeled) {
        append("\n$indents -Text: ${node.text}")
        append("\n$indents -Font: ${node.font}")
      }

      if(node is TextInputControl){
        append("\n$indents -Text: ${node.text}")
        append("\n$indents -Font: ${node.font}")
      }
      append("\n$indents -Properties: $propertiesAsString")
      appendLine()
    }
  }
}


