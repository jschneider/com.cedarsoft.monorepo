package it.neckar.open.javafx

import it.neckar.open.annotations.JavaFriendly
import javafx.scene.Node

/**
 * Helper class with styles
 */
object StyleHelper {
  @JavaFriendly
  @JvmStatic
  fun <T : Node> withStyleClass(node: T, styleClass: String): T {
    return node.withStyleClass(styleClass)
  }
}

fun <T : Node> T.withStyleClass(styleClass: String): T {
  this.styleClass.add(styleClass)
  return this
}
