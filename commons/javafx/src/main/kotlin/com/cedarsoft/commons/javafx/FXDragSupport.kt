package com.cedarsoft.commons.javafx

import com.cedarsoft.unit.other.px
import com.cedarsoft.unit.si.ns
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.scene.Node
import javafx.scene.input.MouseEvent

/**
 * Helper class that offers dragging related calculations
 */
class FXDragSupport<T : Node>(
  private val node: T,
) {

  var isDragging: Boolean = false
    private set

  /**
   * The current location of the mouse pointer.
   * Is updated on every drag
   */
  private var mouseX: @px Double = 0.0
  private var mouseY: @px Double = 0.0

  /**
   * The time when the mouse coordinates have been updated
   */
  private var mouseTime: @ns Long = 0

  /**
   * If set to false, dragging is not allowed.
   * Only relevant when dragging is started
   */
  private val draggingAllowed: BooleanProperty = SimpleBooleanProperty(true)

  /**
   * Used to calculate the mouse speeds
   */
  private val mouseDragSpeedCalculator = MouseDragSpeedCalculator()

  fun install(handler: Handler<T>) {
    //Listen for dragging event
    node.addEventHandler(MouseEvent.MOUSE_DRAGGED) { event: MouseEvent ->
      if (!isDragging) {
        if (!draggingAllowed.get()) {
          //dragging currently not allowed
          return@addEventHandler
        }
        isDragging = true
        updateMouseLocation(event)
        handler.dragStartDetected(node, mouseX, mouseY)
      }
      val deltaX: @px Double = event.sceneX - mouseX
      val deltaY: @px Double = event.sceneY - mouseY
      val deltaTime: @ns Long = updateMouseLocation(event)
      mouseDragSpeedCalculator.add(deltaTime, deltaX, deltaY)
      handler.dragged(node, deltaX, deltaY)
      event.consume()
    }

    //Disable dragging on mouse released
    node.addEventHandler(MouseEvent.MOUSE_RELEASED) { event: MouseEvent ->
      if (!isDragging) {
        return@addEventHandler
      }
      isDragging = false
      handler.dragFinished(node, event.x, event.y)
      event.consume()
    }
  }

  private fun updateMouseLocation(event: MouseEvent): @ns Long {
    mouseX = event.sceneX
    mouseY = event.sceneY
    val now: @ns Long = System.nanoTime()
    val delta: @ns Long = now - mouseTime
    mouseTime = now
    return delta
  }

  fun isDraggingAllowed(): Boolean {
    return draggingAllowed.get()
  }

  fun draggingAllowedProperty(): BooleanProperty {
    return draggingAllowed
  }

  /**
   * Callback that is registered at the drag support
   */
  interface Handler<T> {
    /**
     * The start of drag has been detected
     *
     * @param mouseX the x location within the dragged node
     * @param mouseY the y location within the dragged node
     */
    fun dragStartDetected(draggedNode: T, mouseX: @px Double, mouseY: @px Double)

    /**
     * Is called if a drag has been detected
     *
     * @param draggedNode the node that has been detected
     */
    fun dragged(draggedNode: T, deltaX: @px Double, deltaY: @px Double)

    /**
     * Is called if a drag has been finished
     */
    fun dragFinished(draggedNode: T, x: @px Double, y: @px Double)
  }
}
