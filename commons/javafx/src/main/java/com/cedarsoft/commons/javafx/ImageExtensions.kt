package com.cedarsoft.commons.javafx

import javafx.embed.swing.SwingFXUtils
import javafx.scene.image.WritableImage
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import javax.imageio.ImageIO

/**
 *
 */

/**
 * Converts a writable image to png representation
 */
fun WritableImage.toPng(): ByteArray {
  val out = ByteArrayOutputStream()
  toPng(out)
  return out.toByteArray()
}

/**
 * Writes the image as png to the given stream
 */
fun WritableImage.toPng(out: OutputStream) {
  ImageIO.write(SwingFXUtils.fromFXImage(this, null), "png", out).also {
    require(it) { "Writing did not succeed" }
  }
}
