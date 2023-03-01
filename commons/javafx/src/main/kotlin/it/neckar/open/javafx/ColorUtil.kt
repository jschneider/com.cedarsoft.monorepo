package it.neckar.open.javafx

import javafx.scene.paint.Color
import javax.annotation.Nonnull

/**
 * A utility class around [Color].
 */
object ColorUtil {
  @JvmStatic
  fun toRGB(@Nonnull color: Color): String {
    val r = (color.red * 255).toInt()
    val g = (color.green * 255).toInt()
    val b = (color.blue * 255).toInt()
    return "rgb($r, $g, $b)"
  }

  @JvmStatic
  fun toRGBA(@Nonnull color: Color): String {
    val r = (color.red * 255).toInt()
    val g = (color.green * 255).toInt()
    val b = (color.blue * 255).toInt()
    return "rgb(" + r + ", " + g + ", " + b + ", " + color.opacity + ")"
  }
}
