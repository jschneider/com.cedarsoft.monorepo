package com.cedarsoft.commons.javafx

import com.cedarsoft.common.kotlin.lang.ceil
import javafx.scene.SnapshotParameters
import javafx.scene.image.WritableImage
import javafx.scene.layout.Region
import java.io.File
import java.io.OutputStream


/**
 * Saves the screenshot to the given file
 */
fun Region.saveScreenshot(target: File) {
  target.outputStream().use {
    saveScreenshot(it)
  }
}

/**
 * Saves the screenshot to the given output stream as PNG
 */
fun Region.saveScreenshot(out: OutputStream) {
  screenshot().toPng(out)
}

/**
 * Creates a screenshot from the given region (e.g. a node)
 */
fun Region.screenshot(): WritableImage {
  val width = width.ceil().toInt()
  val height = height.ceil().toInt()

  require(width > 0) { "Width must be > 0 but was <$width>" }
  require(height > 0) { "Height must be > 0 but was <$width>" }

  val writableImage = WritableImage(width, height)

  val snapshotParameters = SnapshotParameters()
  //snapshotParameters.transform = Transform.scale(factor, factor)

  snapshot(snapshotParameters, writableImage)
  return writableImage
}
