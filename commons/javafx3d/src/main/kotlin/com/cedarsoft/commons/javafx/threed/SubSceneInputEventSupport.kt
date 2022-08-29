package com.cedarsoft.commons.javafx.threed

import com.cedarsoft.unit.other.deg
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.SubScene
import javafx.scene.input.GestureEvent
import javafx.scene.input.MouseEvent
import javafx.scene.input.ScrollEvent
import javafx.scene.transform.Rotate
import javafx.scene.transform.Translate

/**
 * A utility class that converts [MouseEvent] events and [ScrollEvent] events
 * into rotations of a [Node] which must be a child of a [SubScene].
 */
class SubSceneInputEventSupport(subScene: SubScene) {
  private var mousePosX = 0.0
  private var mousePosY = 0.0
  private var mouseOldX = 0.0
  private var mouseOldY = 0.0

  private val rotateX: Rotate = Rotate(0.0, Rotate.X_AXIS)
  private val rotateY: Rotate = Rotate(0.0, Rotate.Y_AXIS)
  private val rotateZ: Rotate = Rotate(0.0, Rotate.Z_AXIS)

  private val cameraTranslate: Translate

  init {
    val camera = subScene.camera
    cameraTranslate = Translate(0.0, 0.0, 0.0)
    camera.transforms.add(cameraTranslate)
    subScene.onScroll = EventHandler { event: ScrollEvent ->
      val scale = getTranslateZScale(event)
      val oldZ = cameraTranslate.z
      val newZ = oldZ + event.deltaY * scale
      cameraTranslate.z = newZ
    }
    subScene.onMousePressed = EventHandler { event: MouseEvent ->
      mousePosX = event.sceneX
      mousePosY = event.sceneY
      mouseOldX = mousePosX
      mouseOldY = mousePosY
      if (event.clickCount == 2) {
        resetAll()
      }
    }
    subScene.onMouseDragged = EventHandler { event: MouseEvent ->
      mouseOldX = mousePosX
      mouseOldY = mousePosY
      mousePosX = event.sceneX
      mousePosY = event.sceneY
      val mouseDeltaX = mousePosX - mouseOldX
      val mouseDeltaY = mousePosY - mouseOldY
      if (event.isPrimaryButtonDown) {
        if (event.isAltDown) { //roll
          if (Math.abs(mouseDeltaX) > Math.abs(mouseDeltaY)) {
            rotateZ.angle = computeNewAngle(rotateZ.angle, mouseDeltaX * getRotateZScale(event))
          } else {
            rotateZ.angle = computeNewAngle(rotateZ.angle, mouseDeltaY * getRotateZScale(event))
          }
        } else {
          val scale = getRotateXYScale(event)
          rotateY.angle = computeNewAngle(rotateY.angle, mouseDeltaX * scale)
          rotateX.angle = computeNewAngle(rotateX.angle, -mouseDeltaY * scale)
        }
      } else {
        val oldX = cameraTranslate.x
        val newX = oldX - mouseDeltaX * getTranslateXYScale(event)
        cameraTranslate.x = newX
        val oldY = cameraTranslate.y
        val newY = oldY - mouseDeltaY * getTranslateXYScale(event)
        cameraTranslate.y = newY
      }
    }
  }

  fun install(node: Node) {
    node.transforms.addAll(rotateX, rotateY, rotateZ)
  }

  fun resetRotation() {
    rotateX.angle = 0.0
    rotateY.angle = 0.0
    rotateZ.angle = 0.0
  }

  fun resetTranslation() {
    cameraTranslate.x = 0.0
    cameraTranslate.y = 0.0
    cameraTranslate.z = 0.0
  }

  fun resetAll() {
    resetTranslation()
    resetRotation()
  }

  companion object {
    private const val ANGLE_FULL_CIRCLE: @deg Int = 360

    private fun computeNewAngle(currentAngle: Double, offset: Double): Double {
      return (currentAngle + offset) % ANGLE_FULL_CIRCLE
    }

    private fun getRotateXYScale(event: MouseEvent): Double {
      var scale = 0.75
      if (event.isControlDown) {
        scale = 0.1
      }
      if (event.isShiftDown) {
        scale = 2.0
      }
      return scale
    }

    private fun getRotateZScale(event: MouseEvent): Double {
      var scale = 2.0
      if (event.isControlDown) {
        scale = 1.0
      }
      if (event.isShiftDown) {
        scale = 5.0
      }
      return scale
    }

    private fun getTranslateXYScale(event: MouseEvent): Double {
      var scale = 2.0
      if (event.isControlDown) {
        scale = 0.5
      }
      if (event.isShiftDown) {
        scale = 5.0
      }
      return scale
    }

    private fun getTranslateZScale(event: GestureEvent): Double {
      var scale = 25.0
      if (event.isControlDown) {
        scale = 2.5
      }
      if (event.isShiftDown) {
        scale = 75.0
      }
      return scale
    }
  }
}
