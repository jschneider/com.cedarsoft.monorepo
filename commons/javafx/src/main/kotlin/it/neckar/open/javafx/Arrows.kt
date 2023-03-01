package it.neckar.open.javafx

import it.neckar.open.unit.other.px
import javafx.scene.canvas.GraphicsContext
import javafx.scene.shape.LineTo
import javafx.scene.shape.MoveTo
import javafx.scene.shape.Path

/**
 * Helper class that creates arrows
 */
object Arrows {
  private const val ArrowWidth = 5.0
  private const val ArrowWidthHalf = ArrowWidth / 2.0
  private const val ArrowLength = 5.0

  @JvmStatic
  fun createArrowToTop(lineLength: @px Int): Path {
    return Path().apply {
      styleClass.add("arrow")
      elements.add(MoveTo(0.0, -ArrowLength)) //start @ top of arrow
      elements.add(LineTo(ArrowWidthHalf, 0.0)) //bottom right
      elements.add(LineTo(-ArrowWidthHalf, 0.0)) //bottom left
      elements.add(LineTo(0.0, -ArrowLength)) //back to top of arrow
      elements.add(MoveTo(0.0, 0.0)) //middle bottom
      elements.add(LineTo(0.0, lineLength.toDouble())) //middle bottom
    }
  }

  @JvmStatic
  fun addArrowPath(arrowLength: Double, lineTotalLength: @px Double, gc: GraphicsContext) {
    gc.beginPath()
    gc.moveTo(0.0, 0.0)
    gc.lineTo(arrowLength / 2.0, +arrowLength) //bottom right
    gc.lineTo(-(arrowLength / 2.0), +arrowLength) //bottom left
    gc.lineTo(0.0, 0.0) //back to top of arrow
    gc.lineTo(0.0, +arrowLength) //middle bottom
    gc.lineTo(0.0, lineTotalLength) //middle bottom
    gc.closePath()
  }
}
