package com.cedarsoft.commons.javafx

import com.google.common.base.Strings
import javafx.scene.Node
import javafx.scene.Parent
import java.io.PrintStream

/**
 * Fx utility methods
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
object FxUtils {
  fun dump(node: Node, out: PrintStream) {
    dump(node, out, 0)
  }

  private fun dump(node: Node, out: PrintStream, depth: Int) {
    out.println(Strings.repeat("  ", depth) + node + " #" + node.id)
    (node as? Parent)?.childrenUnmodifiable?.forEach { c -> dump(c, out, depth + 1) }
  }
}
