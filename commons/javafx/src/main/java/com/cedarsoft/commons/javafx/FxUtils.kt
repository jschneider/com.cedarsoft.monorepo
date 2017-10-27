package com.cedarsoft.commons.javafx

import com.google.common.base.Strings
import com.sun.javafx.stage.StageHelper
import javafx.scene.Node
import javafx.scene.Parent
import javafx.stage.Stage
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

  /**
   * Returns the currently focused stage, or the one open stage or null if there is no stage or there are multiple stages
   */
  fun getStageSafe(): Stage? {
    val stages = StageHelper.getStages()
    for (stage in stages) {
      if (stage.isFocused) {
        return stage;
      }
    }

    //we have no focused stage, therefore return the one stage if there is only one

    if (stages.size == 1) {
      return stages.get(0)
    }

    //no stage found
    return null;
  }
}
