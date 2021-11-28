package com.cedarsoft.commons.javafx

import javafx.scene.image.Image
import javafx.scene.image.ImageView

/**
 * Returns the aspect ratio of an image (width/height)
 */
private val Image.aspectRatio: Double
  get() {
    return width / this.height
  }

/**
 * An image view that can be resized
 */
class ImageViewResizable() : ImageView() {
  init {
    isPreserveRatio = true
  }

  constructor(image: Image) : this() {
    this.image = image
  }

  override fun minWidth(height: Double): Double {
    return 40.0
  }

  override fun prefWidth(height: Double): Double {
    val image = image ?: return minWidth(height)

    if (height <= 0.0) {
      return image.width
    }

    return (height * image.aspectRatio).coerceAtMost(image.width)
  }

  override fun maxWidth(height: Double): Double {
    return 16384.0
  }

  override fun minHeight(width: Double): Double {
    return 40.0
  }

  override fun prefHeight(width: Double): Double {
    val image = image ?: return minHeight(width)

    if (width <= 0.0) {
      return image.height
    }

    return (width / image.aspectRatio).coerceAtMost(image.height)
  }

  override fun maxHeight(width: Double): Double {
    return 16384.0
  }

  override fun isResizable(): Boolean {
    return true
  }

  override fun resize(width: Double, height: Double) {
    fitWidth = width
    fitHeight = height
  }
}
