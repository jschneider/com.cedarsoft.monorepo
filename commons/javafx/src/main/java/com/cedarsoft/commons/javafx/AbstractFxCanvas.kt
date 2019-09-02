package com.cedarsoft.commons.javafx

import com.cedarsoft.commons.javafx.properties.*
import com.cedarsoft.unit.other.px
import com.cedarsoft.unit.si.ns
import javafx.animation.AnimationTimer
import javafx.beans.Observable
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.geometry.VPos
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.text.TextAlignment

/**
 * Abstract base class for a canvas that is automatically repainted if necessary
 *
 *
 * Subclasses must call [.paint] whenever the model is updated
 *
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
@Deprecated("Use classes from algorithms instead")
abstract class AbstractFxCanvas protected constructor() : Canvas() {
  /**
   * Is set to true if a repaint is required
   */
  private var repaintRequired: Boolean = false

  /**
   * If set to true, the repaint is disabled.
   * This can be used to avoid unnecessary paintings - e.g. if the canvas is currently not shown
   */
  val repaintDisabledProperty: BooleanProperty = SimpleBooleanProperty()

  /**
   * If set to true the canvas does *not* repaint itself
   */
  val repaintDisabled: Boolean by repaintDisabledProperty

  /**
   * If set to true the x values are snapped to pixel.
   * Should be set to false while the x axis is animated
   */
  private val snapXValuesToPixel = SimpleBooleanProperty(true)

  /**
   * Should be disabled when the y axis is animated
   */
  private val snapYValuesToPixel = SimpleBooleanProperty(true)


  val isSnapXValuesToPixel: Boolean
    get() = snapXValuesToPixel.get()

  val isSnapYValuesToPixel: Boolean
    get() = snapYValuesToPixel.get()

  init {
    //Automatically mark as dirty on resize
    registerDirtyListener(widthProperty())
    registerDirtyListener(heightProperty())

    //Repaint when snap has changed
    registerDirtyListener(snapYValuesToPixel)
    registerDirtyListener(snapXValuesToPixel)

    repaintDisabledProperty.addListener { _, _, newValue ->
      if (newValue!!) {
        //Paint painting disabled text
        paintRepaintDisabled(graphicsContext2D)
      } else {
        //A initial repaint is necessary
        repaintRequired = true
      }
    }

    object : AnimationTimer() {
      override fun handle(@ns now: Long) {
        if (scene == null) {
          return
        }
        if (!isVisible) {
          return
        }
        if (repaintDisabled) {
          return
        }

        if (repaintRequired) {
          repaintRequired = false
          paint(graphicsContext2D)
        }
      }
    }.start()
  }

  fun registerDirtyListener(property: Observable) {
    registerDirtyListener(this, property)
  }

  /**
   * Marks the canvas as dirty. It will be repainted on the next occasion
   */
  fun markAsDirty() {
    repaintRequired = true
  }

  /**
   * Paints a template text when paining is disabled.
   * This way it becomes obvious if somebody forgot to set [.isRepaintDisabled] back to false
   */
  protected fun paintRepaintDisabled(g2d: GraphicsContext) {
    clearBackground(g2d)
    g2d.fill = Color.LIGHTGRAY
    g2d.fillRect(0.0, 0.0, width, height)

    //Paint a text
    g2d.textAlign = TextAlignment.LEFT
    g2d.textBaseline = VPos.TOP

    g2d.fill = Color.DARKGRAY
    g2d.fillText("Repainting is disabled", 0.0, 0.0)
  }

  /**
   * Clears the background in a way that avoids memory leaks.
   */
  protected fun clearBackground(gc: GraphicsContext) {
    // Workaround to avoid a memory leak
    gc.clearRect(-1000.0, -1000.0, width + 2000, height + 2000)
  }

  /**
   * Paint the content.
   *
   *
   * Implementations probably want to call [.clearBackground] first
   */
  protected abstract fun paint(gc: GraphicsContext)

  fun snapXValuesToPixelProperty(): BooleanProperty {
    return snapXValuesToPixel
  }

  fun snapYValuesToPixelProperty(): BooleanProperty {
    return snapYValuesToPixel
  }

  @px
  protected fun snapXValue(@px value: Double): Double {
    return FxPaintingUtils.snapPosition(value, isSnapXValuesToPixel)
  }

  @px
  protected fun snapXSize(@px value: Double): Double {
    return FxPaintingUtils.snapSize(value, isSnapXValuesToPixel)
  }

  @px
  protected fun snapYValue(@px value: Double): Double {
    return FxPaintingUtils.snapPosition(value, isSnapYValuesToPixel)
  }

  @px
  protected fun snapYSize(@px value: Double): Double {
    return FxPaintingUtils.snapSize(value, isSnapYValuesToPixel)
  }

  protected fun paintDebugInfo(gc: GraphicsContext) {
    //Around
    gc.beginPath()
    gc.rect(snapXValue(0.0), snapYValue(0.0), snapXSize(width), snapYSize(height))
    gc.closePath()
    gc.stroke = Color.ORANGE
    gc.lineWidth = 3.0
    gc.stroke()

    //cross wire to mark the center
    gc.beginPath()
    gc.moveTo(snapXValue(0.0), snapYValue(height / 2.0))
    gc.lineTo(snapXValue(width), snapYValue(height / 2.0))
    gc.moveTo(snapXValue(width / 2.0), 0.0)
    gc.lineTo(snapXValue(width / 2.0), snapYValue(height))

    gc.closePath()
    gc.lineWidth = 2.0
    gc.stroke()

    //oval in the corners
    //Bottom right
    gc.strokeOval(snapXValue(width - 10), snapYValue(height - 10), snapXSize(20.0), snapYSize(20.0))
    //Top right
    gc.strokeOval(snapXValue(width - 10), snapYValue((0 - 10).toDouble()), snapXSize(20.0), snapYSize(20.0))
    //Bottom left
    gc.strokeOval(snapXValue((0 - 10).toDouble()), snapYValue(height - 10), snapXSize(20.0), snapYSize(20.0))
    //top left
    gc.strokeOval(snapXValue((0 - 10).toDouble()), snapYValue((0 - 10).toDouble()), snapXSize(20.0), snapYSize(20.0))
  }

  companion object {
    @px
    @JvmStatic
    protected fun snapSize(@px value: Double): Double {
      return FxPaintingUtils.snapSize(value, false)
    }

    @px
    @JvmStatic
    protected fun snapPosition(@px value: Double): Double {
      return FxPaintingUtils.snapPosition(value)
    }

    /**
     * Can be used to enable debug output
     */
    protected const val DEBUG_ENABLED: Boolean = false

    @JvmStatic
    fun registerDirtyListener(canvas: AbstractFxCanvas, property: Observable) {
      property.addListener { canvas.markAsDirty() }
    }
  }
}
