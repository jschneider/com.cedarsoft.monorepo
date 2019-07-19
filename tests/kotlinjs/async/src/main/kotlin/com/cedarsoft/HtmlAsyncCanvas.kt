package com.cedarsoft

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.document
import kotlin.browser.window
import kotlin.js.Date
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round
import kotlin.random.Random

fun startPaintingAsync() {
  AsyncPainter().start()
}

class AsyncPainter {
  private val offscreenCanvases: MutableList<HTMLCanvasElement> = mutableListOf()
  private val canvas = document.getElementById("myCanvas") as HTMLCanvasElement
  private var pendingUpdates = 0
  private var lastTimestampInMillis = 0.0

  init {
    for (i in 1..TILE_COUNT) {
      val offscreenCanvas = document.createElement("CANVAS") as HTMLCanvasElement
      this.offscreenCanvases.add(offscreenCanvas)
    }

    println("Canvas found: $canvas with size: ${canvas.width}/${canvas.height}")
    val context2D = getContext2D(canvas)
    context2D.fillStyle = "#ff00ff"
    context2D.strokeStyle = "#CC00CC"
    context2D.fillRect(0.0, 0.0, canvas.width.toDouble() - 1, canvas.height.toDouble() - 1)
  }

  fun start() {
    window.requestAnimationFrame { timestamp -> paintCanvas(timestamp) }
  }

  private fun paintCanvas(highResTimestampInMillis: Double) {
    val context2D = getContext2D(canvas)

    val timePassedInMillis = highResTimestampInMillis - lastTimestampInMillis
    lastTimestampInMillis = highResTimestampInMillis
    val framesPerSecond = round(1000 / timePassedInMillis) as Int

    context2D.save()
    for ((index, offscreenCanvas) in offscreenCanvases.withIndex()) {
      context2D.drawImage(offscreenCanvas, (index % TILE_COLS) * TILE_WIDTH, (index / 4) * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT)
    }
    context2D.fillStyle = "#ff0000"
    context2D.font = "30px Arial"
    context2D.fillText("${getTimestamp()}, pending=$pendingUpdates, FPS=$framesPerSecond", 100.0, 100.0)
    context2D.restore()

    if (pendingUpdates <= 0) {
      pendingUpdates = TILE_COUNT
      GlobalScope.launch {
        paintOffscreenCanvases()
      }
    }

    window.requestAnimationFrame { timestamp -> paintCanvas(timestamp) }
  }

  private suspend fun paintOffscreenCanvases() = coroutineScope {
    for (offscreenCanvas in offscreenCanvases) {
      launch {
        consumeCpuTime()

        val context2D = getContext2D(offscreenCanvas)
        context2D.save()
        context2D.fillStyle = getRandomFillStyle()
        context2D.strokeStyle = "#CC00CC"
        context2D.fillRect(0.0, 0.0, TILE_WIDTH, TILE_HEIGHT)
        context2D.fillStyle = "#ffffff"
        context2D.fillText("${getTimestamp()}", 10.0, 40.0)

        context2D.restore()

        pendingUpdates--
      }
    }
  }

  companion object {
    const val TILE_COLS = 4
    const val TILE_ROWS = 3
    const val TILE_COUNT = TILE_COLS * TILE_ROWS
    const val TILE_WIDTH = 400.0
    const val TILE_HEIGHT = 200.0

    fun getContext2D(canvas: HTMLCanvasElement): CanvasRenderingContext2D {
      val context = canvas.getContext("2d") ?: throw IllegalStateException("context not found")
      return context as CanvasRenderingContext2D
    }

    fun getRandomFillStyle(): String {
      val red = Random.nextInt(256).toString(16).substring(0..1)
      val green = Random.nextInt(256).toString(16).substring(0..1)
      val blue = Random.nextInt(256).toString(16).substring(0..1)
      return "#$red$green$blue"
    }

    fun getTimestamp(): String {
      val now = Date()
      return "${now.getMinutes()}:${now.getSeconds()}.${now.getMilliseconds()}"
    }

    fun consumeCpuTime() {
      var min = Double.MAX_VALUE
      var max = Double.MIN_VALUE
      for (i in 1..10_000_000) {
        min = min(min, Random.nextDouble())
        max = max(max, Random.nextDouble())
      }
    }
  }

}
