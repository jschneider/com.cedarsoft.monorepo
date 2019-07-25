package com.cedarsoft.commons.javafx

import com.google.common.base.Strings
import javafx.application.Platform
import javafx.scene.Node
import javafx.scene.Parent
import javafx.stage.Screen
import javafx.stage.Stage
import javafx.stage.Window
import java.io.PrintStream


/**
 * Fx utility methods
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
object FxUtilsKotlin {
  @JvmStatic
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
  @JvmStatic
  fun getStage(): Stage {
    return getStageSafe() ?: throw IllegalStateException("No stage found")
  }

  /**
   * returns the stage (or null) if there is no stage
   */
  @JvmStatic
  fun getStageSafe(): Stage? {
    val stages = getStages()

    for (stage in stages) {
      if (stage.isFocused) {
        return stage
      }
    }

    //we have no focused stage, therefore return the one stage if there is only one

    if (stages.size == 1) {
      return stages[0]
    }

    //no stage found
    return null
  }

  /**
   * Returns the available stages
   */
  fun getStages(): List<Stage> {
    if (isJdk8) {
      //Code for JDK 8

      //Use reflection since StageHelper is not accesible with JDK > 8
      //
      // Replaces that code:
      // return StageHelper.getStages()
      val stageHelper = Class.forName("com.sun.javafx.stage.StageHelper")

      @Suppress("UNCHECKED_CAST")
      return stageHelper.getDeclaredMethod("getStages").invoke(null) as List<Stage>
    }

    //Code for JDK 11
    val windows: List<Window>

    //Use reflection since the method does not exist in JDK < 9
    //
    // Replaces that code:
    //windows = Stage.getWindows()

    @Suppress("UNCHECKED_CAST")
    windows = Stage::class.java.getMethod("getWindows").invoke(null) as List<Window>

    return windows.filter { it is Stage }.map { it as Stage }

  }

  /**
   * Center the stage on the given coordinates
   */
  @JvmStatic
  fun centerStage(stage: Stage, width: Double, height: Double) {
    val screenBounds = Screen.getPrimary().getVisualBounds()
    stage.x = (screenBounds.getWidth() - width) / 2
    stage.y = (screenBounds.getHeight() - height) / 2
  }

  /**
   * Used for reflection workaround
   */
  private val isJdk8: Boolean = System.getProperty("java.version").contains("1.8")

  /**
   * Throws an exception if the current thread is not the JavaFX UI Thread
   */
  @Throws(IllegalThreadStateException::class)
  fun ensureFxThread() {
    if (!Platform.isFxApplicationThread()) {
      throw IllegalThreadStateException("Expected JavaFX Application thread but was <${Thread.currentThread().name}>")
    }
  }
}
