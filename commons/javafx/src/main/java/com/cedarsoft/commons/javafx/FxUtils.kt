package com.cedarsoft.commons.javafx

import com.cedarsoft.unit.other.px
import com.google.common.base.Strings
import com.google.common.collect.ImmutableList
import javafx.animation.Interpolator
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.application.Platform
import javafx.beans.property.BooleanProperty
import javafx.geometry.Point2D
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.control.ScrollPane
import javafx.scene.paint.Color
import javafx.stage.Screen
import javafx.stage.Stage
import javafx.stage.Window
import javafx.util.Duration
import java.io.PrintStream
import java.util.Locale
import java.util.Optional
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
object FxUtils {
  /**
   * The amount of pixels that is added to ensure the node is not exactly at the edge
   */
  @px
  internal const val SCROLL_TO_VISIBLE_MARGIN: Int = 10

  /**
   * Used for reflection workaround
   */
  private val isJdk8: Boolean = System.getProperty("java.version").contains("1.8")

  /**
   * Returns the currently focused stage, or the one open stage or null if there is no stage or there are multiple stages
   */
  val stage: Stage
    @JvmStatic
    get() = this.stageSafe ?: throw IllegalStateException("No stage found")

  /**
   * returns the stage (or null) if there is no stage
   */
  //Find the focused stage
  //we have no focused stage, therefore return the one stage if there is only one
  //no stage found
  @JvmStatic
  val stageSafe: Stage?
    get() {
      val stages = stages
      for (stage in stages) {
        if (stage.isFocused) {
          return stage
        }
      }

      return if (stages.size == 1) {
        stages[0]
      } else null
    }

  /**
   * Returns the available stages
   */
  //Code for JDK 8
  //Use reflection since StageHelper is not accesible with JDK > 8
  //
  // Replaces that code:
  // return StageHelper.getStages()
  //Code for JDK 11
  //Use reflection since the method does not exist in JDK < 9
  //
  // Replaces that code:
  //windows = Stage.getWindows()
  @JvmStatic
  val stages: List<Stage>
    get() {
      if (isJdk8) {
        val stageHelper = Class.forName("com.sun.javafx.stage.StageHelper")
        return stageHelper.getDeclaredMethod("getStages").invoke(null) as List<Stage>
      }

      //Code for JDK 11
      val windows = Stage::class.java.getMethod("getWindows").invoke(null) as List<Window>

      return windows.stream()
        .filter { window -> window is Stage }
        .map { window -> window as Stage }
        .collect(ImmutableList.toImmutableList())
    }

  /**
   * Center the stage on the given coordinates
   */
  @JvmStatic
  fun centerStage(stage: Stage, width: Double, height: Double) {
    val screenBounds = Screen.getPrimary().visualBounds
    stage.x = (screenBounds.width - width) / 2
    stage.y = (screenBounds.height - height) / 2
  }

  /**
   * Throws an exception if the current thread is not the JavaFX UI Thread
   */
  fun ensureFxThread() {
    if (!Platform.isFxApplicationThread()) {
      throw IllegalThreadStateException("Expected JavaFX Application thread but was <${Thread.currentThread().name}>")
    }
  }

  @JvmStatic
  fun dump(node: Node, out: PrintStream) {
    dump(node, out, 0)
  }

  private fun dump(node: Node, out: PrintStream, depth: Int) {
    out.println(Strings.repeat("  ", depth) + node + " #" + node.id)

    if (node is Parent) {
      val parent = node as Parent
      parent.childrenUnmodifiable
        .forEach { child -> dump(child, out, depth + 1) }
    }
  }

  /**
   * Scrolls the given value to visible using an animation
   */
  @JvmStatic
  internal fun scrollToVisible(scrollPane: ScrollPane, targetVValue: Double, animated: Boolean) {
    if (!animated) {
      scrollPane.vvalueProperty().value = targetVValue
      return
    }

    val timeline = Timeline()
    timeline.keyFrames.add(
      KeyFrame(
        Duration.millis(200.0),
        KeyValue(scrollPane.vvalueProperty(), targetVValue, Interpolator.EASE_BOTH)
      )
    )
    timeline.play()
  }

  /**
   * Scrolls the given node to visible within the first scroll pane that is found as ancestor
   */
  @JvmStatic
  fun scrollToVisible(node: Node) {
    val scrollPane = findParent(ScrollPane::class.java, node).orElseThrow { IllegalStateException("No parent scroll pane found") }
    scrollToVisible(scrollPane, node)
  }

  /**
   * Scrolls scroll pane to make the given node visible
   */
  @JvmStatic
  fun scrollToVisible(scrollPane: ScrollPane, node: Node) {
    val positionInViewPort = localToAncestor(node, scrollPane.content, 0.0, 0.0)
    @px val nodeHeight = node.layoutBounds.height

    scrollToVisible(scrollPane, positionInViewPort.y, nodeHeight)
  }

  /**
   * Scrolls the bar so that the target y is in the center of the visible area
   */
  @JvmStatic
  @JvmOverloads
  fun scrollToCenter(scrollPane: ScrollPane, @px targetY: Double, animated: Boolean = true) {
    @px val height = scrollPane.viewportBounds.height
    scrollToVisible(scrollPane, targetY - height / 2.0, height, 0.0, animated)
  }

  /**
   * Scrolls the given scroll pane by the given distance
   */
  @JvmStatic
  fun scroll(scrollPane: ScrollPane, @px deltaY: Double): Double {
    @px val visibleBottomBeforeScroll = scrollPane.currentlyVisibleBottom()

    //The bottom y for the target area
    scrollToVisible(scrollPane, visibleBottomBeforeScroll + deltaY, 0.0, scrollMargin = 0.0, animated = false)

    val visibleBottomAfterScroll = scrollPane.currentlyVisibleBottom()

    return visibleBottomAfterScroll - visibleBottomBeforeScroll
  }

  /**
   * Returns the currently visible bottom of the scroll pane
   */
  @px
  private fun ScrollPane.currentlyVisibleBottom(): Double {
    //The height of the view port
    @px val viewportHeight = viewportBounds.height
    //The height of the content
    @px val contentHeight = content.layoutBounds.height
    //The height that is always hidden
    @px val hiddenHeight = contentHeight - viewportHeight

    //The current min/max y values that are visible
    @px val currentlyVisibleTop = vvalue * hiddenHeight
    return currentlyVisibleTop + viewportHeight
  }

  /**
   * Scrolls an area to visible
   *
   * @param scrollPane   the pane that is moved
   * @param targetY      the target y that should be visible
   * @param targetHeight the target height that should be visible
   */
  @JvmStatic
  @JvmOverloads
  fun scrollToVisible(scrollPane: ScrollPane, @px targetY: Double, @px targetHeight: Double, @px scrollMargin: Double = SCROLL_TO_VISIBLE_MARGIN.toDouble(), animated: Boolean = true) {
    //The height of the view port
    @px val viewportHeight = scrollPane.viewportBounds.height
    //The height of the content
    @px val contentHeight = scrollPane.content.layoutBounds.height
    //The height that is always hidden
    @px val hiddenHeight = contentHeight - viewportHeight

    //The current min/max y values that are visible
    @px val currentlyVisibleTop = scrollPane.vvalue * hiddenHeight
    @px val currentlyVisibleBottom = currentlyVisibleTop + viewportHeight


    //The bottom y for the target area
    @px val targetYBottom = targetY + targetHeight


    //Check for large target heights
    if (targetHeight > viewportHeight) {
      if (targetY < currentlyVisibleTop && targetYBottom > currentlyVisibleBottom) {
        //Do nothing, since some parts are visible
        return
      }
    }


    if (targetY < currentlyVisibleTop) {
      //target y is above the currently visible area, we have to scroll
      val newVValue = minMax(0.0, 1.0, 1.0 / hiddenHeight * (targetY - scrollMargin))
      scrollToVisible(scrollPane, newVValue, animated)
      return
    }


    if (targetYBottom > currentlyVisibleBottom) {
      //The target height is *not* larger than the viewport height, Just scroll
      val newVValue = minMax(0.0, 1.0, 1.0 / hiddenHeight * (targetYBottom - viewportHeight + scrollMargin))
      scrollToVisible(scrollPane, newVValue, animated)


      return
    }

    //Currently visible, do nothing
  }

  /**
   * Ensures the given values is within the given min/max values
   */
  private fun minMax(min: Double, max: Double, current: Double): Double {
    return Math.max(min, Math.min(max, current))
  }

  /**
   * Returns the first parent of the given type
   */
  @JvmStatic
  fun <T : Node> findParent(type: Class<T>, node: Node): Optional<T> {
    var currentNode: Node? = node

    while (currentNode != null) {
      if (type.isAssignableFrom(currentNode.javaClass)) {
        return Optional.of(type.cast(currentNode))
      }
      currentNode = currentNode.parent
    }

    return Optional.empty()
  }

  /**
   * Calculates the location relative to the ancestor (parent or parent of parent or parent of...)
   */
  @JvmStatic
  fun localToAncestor(node: Node, ancestor: Node, x: Double, y: Double): Point2D {
    var point2D = Point2D(x, y)

    var currentNode: Node? = node
    while (currentNode != null) {
      if (currentNode === ancestor) {
        return point2D
      }

      point2D = currentNode.localToParent(point2D)

      currentNode = currentNode.parent
    }
    throw IllegalStateException("Invalid ancestor <$ancestor> for <$node>")
  }

  /**
   * Finds all parents between the given node and the last parent.
   * The list does *not* contains the node, but *does* contain the last parent
   */
  @JvmStatic
  fun getParents(child: Node, lastParent: Node): List<Node> {
    val parentsBuilder = ImmutableList.builder<Node>()

    var parent: Node? = child.parent
    while (parent != null) {
      parentsBuilder.add(parent)
      if (parent === lastParent) {
        return parentsBuilder.build()
      }

      parent = parent.parent
    }

    throw IllegalStateException("Could not find ancestor <$lastParent> for <$child>")
  }
}

/**
 * Converts a color to an RGB hex string
 */
fun Color.toRGBHex(): String {
  if (opacity != 1.0) {
    return toRGBAHex()
  }

  return String.format(
    Locale.ENGLISH,
    "#%02X%02X%02X",
    (red * 255).toInt(),
    (green * 255).toInt(),
    (blue * 255).toInt()
  )
}

fun Color.toRGBAHex(): String {
  return String.format(
    Locale.ENGLISH,
    "#%02X%02X%02X%02X",
    (red * 255).toInt(),
    (green * 255).toInt(),
    (blue * 255).toInt(),
    (opacity * 255).toInt()
  )
}

/**
 * Returns the stage for the given node
 */
val Node.stage: Stage?
  get() {
    return window as? Stage
  }

val Node.window: Window?
  get() = scene?.window

/**
 * Returns the root
 */
fun Node.root(): Node {
  return parent?.let {
    return@let it.root()
  } ?: this
}

/**
 * Toggles the value of the boolean property
 */
fun BooleanProperty.toggle() {
  this.value = !this.value
}

/**
 * This method returns true if the JavaFX platform has been initialized, false otherwise.
 *
 * Attention! Might return true while [isFxPlatformStopped] might *also* return true
 */
fun isFxPlatformInitialized(): Boolean {
  val field = com.sun.javafx.application.PlatformImpl::class.java.getDeclaredField("initialized")
  field.isAccessible = true
  return (field.get(null) as AtomicBoolean).get()
}

fun isFxPlatformStopped(): Boolean {
  val field = com.sun.javafx.application.PlatformImpl::class.java.getDeclaredField("platformExit")
  field.isAccessible = true
  return (field.get(null) as AtomicBoolean).get()
}

/**
 * Returns true if JavaFX platform has been initialized but *not* been stopped
 */
fun isFxPlatformActive(): Boolean {
  return isFxPlatformInitialized() && !isFxPlatformStopped()
}

/**
 * Returns true if the JavaFX application thread is running
 */
fun isFxApplicationThreadRunning(): Boolean {
  val activeThreads = Thread.getAllStackTraces().keys
  return activeThreads.any {
    it.name == "JavaFX Application Thread"
  }
}
