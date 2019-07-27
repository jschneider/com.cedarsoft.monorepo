package com.cedarsoft.commons.javafx

import javafx.animation.Animation
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.util.Duration


/**
 * A class for timers executed on the JavaFX Application Thread.
 *
 * @author Christian Erbelding ([ce@cedarsoft.com](mailto:ce@cedarsoft.com))
 */
object JavaFxTimer {
  /**
   * Calls the given runnable on the JavaFX Application Thread after the given delay.
   */
  @JvmStatic
  fun start(delay: Duration, run: Runnable) {
    Timeline(KeyFrame(delay, EventHandler<ActionEvent> { run.run() })).play()
  }

  @JvmStatic
  fun repeat(delay: Duration, run: Runnable) {
    val timeline = Timeline(KeyFrame(delay, EventHandler<ActionEvent> { _ -> run.run() }))
    timeline.cycleCount = Animation.INDEFINITE
    timeline.play()
  }

  fun repeat(delay: Duration, run: () -> Unit) {
    val timeline = Timeline(KeyFrame(delay, EventHandler<ActionEvent> { _ -> run() }))
    timeline.cycleCount = Animation.INDEFINITE
    timeline.play()
  }
}
