package it.neckar.open.javafx

import javafx.scene.text.Font
import javafx.scene.text.FontPosture
import javafx.scene.text.FontWeight
import javax.annotation.Nonnull

/**
 * A builder for [Font]s.
 */
class FontBuilder @JvmOverloads constructor(font: Font = Font.getDefault()) {
  private var family: String
  private var weight: FontWeight
  private var posture: FontPosture
  private var size: Double

  init {
    family = font.family
    size = font.size
    weight = FontWeight.NORMAL
    posture = FontPosture.REGULAR
  }

  fun withFamily(@Nonnull family: String): FontBuilder {
    this.family = family
    return this
  }

  fun withWeight(@Nonnull weight: FontWeight): FontBuilder {
    this.weight = weight
    return this
  }

  fun withPosture(@Nonnull posture: FontPosture): FontBuilder {
    this.posture = posture
    return this
  }

  fun withSize(size: Double): FontBuilder {
    this.size = size
    return this
  }

  fun build(): Font {
    return Font.font(family, weight, posture, size)
  }

  companion object {
    @JvmStatic
    fun create(): FontBuilder {
      return FontBuilder()
    }

    @JvmStatic
    fun createWithFont(@Nonnull font: Font): FontBuilder {
      return FontBuilder(font)
    }
  }
}
