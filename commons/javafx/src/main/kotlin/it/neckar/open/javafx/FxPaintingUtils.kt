package it.neckar.open.javafx

import it.neckar.open.unit.other.px
import javafx.scene.SnapshotParameters
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.Image
import javafx.scene.paint.Color
import javafx.scene.paint.ImagePattern
import kotlin.math.ceil

/**
 * Helper methods for fx painting
 *
 */
object FxPaintingUtils {

  /**
   * Returns the *lower* value if snapToPixel is set to true
   */
  @JvmOverloads
  fun snapSize(value: @px Double, snapToPixel: Boolean = true): @px Double {
    return if (!snapToPixel) {
      value
    } else ceil(value)
  }

  /**
   * Rounds the position
   */
  @JvmOverloads
  fun snapPosition(value: @px Double, snapToPixel: Boolean = true): @px Double {
    return if (!snapToPixel) {
      value
    } else Math.round(value).toDouble()
  }

  fun createHatch(stroke: Color, lineWidth: Double, width: @px Double, height: @px Double): ImagePattern {
    val canvas = Canvas(width, height)
    val gc = canvas.graphicsContext2D
    gc.lineWidth = lineWidth
    gc.stroke = stroke
    hatch1(gc, width, height)
    val patternImage: Image = canvas.snapshot(SnapshotParameters(), null)
    return ImagePattern(patternImage, 0.0, 0.0, width, height, false)
  }

  fun createCrossed(stroke: Color, lineWidth: Double, width: @px Double, height: @px Double): ImagePattern {
    val canvas = Canvas(width, height)
    val gc = canvas.graphicsContext2D
    gc.lineWidth = lineWidth
    gc.stroke = stroke
    hatch1(gc, width, height)
    hatch2(gc, width, height)
    val patternImage: Image = canvas.snapshot(SnapshotParameters(), null)
    return ImagePattern(patternImage, 0.0, 0.0, width, height, false)
  }

  private fun hatch1(gc: GraphicsContext, width: @px Double, height: @px Double) {
    gc.beginPath()
    gc.moveTo(0.0, 0.0)
    gc.lineTo(width, height)
    gc.stroke()
    gc.beginPath()
    gc.moveTo(width / 2.0, -height / 2.0)
    gc.lineTo(width * 1.5, height / 2.0)
    gc.stroke()
    gc.beginPath()
    gc.moveTo(-width / 2.0, height / 2.0)
    gc.lineTo(width / 2.0, height * 1.5)
    gc.stroke()
  }

  private fun hatch2(gc: GraphicsContext, width: @px Double, height: @px Double) {
    gc.beginPath()
    gc.moveTo(width, 0.0)
    gc.lineTo(0.0, height)
    gc.stroke()
    gc.beginPath()
    gc.moveTo(-width / 2.0, height / 2.0)
    gc.lineTo(width / 2.0, -height / 2.0)
    gc.stroke()
    gc.beginPath()
    gc.moveTo(width / 2.0, height * 1.5)
    gc.lineTo(width * 1.5, height / 2.0)
    gc.stroke()
  }
}
