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
    val timeline = Timeline(KeyFrame(delay, EventHandler<ActionEvent> { run.run() }))
    timeline.cycleCount = Animation.INDEFINITE
    timeline.play()
  }

  /**
   * Repeats the given lambda every [delay]
   */
  fun repeat(delay: Duration, run: () -> Unit): Timeline {
    val timeline = Timeline(KeyFrame(delay, EventHandler<ActionEvent> { run() }))
    timeline.cycleCount = Animation.INDEFINITE
    timeline.play()
    return timeline
  }

  /**
   * Repeats the given lambda every [delay]
   */
  fun repeat(delay: kotlin.time.Duration, run: () -> Unit): Timeline {
    return repeat(Duration.millis(delay.inMilliseconds), run)
  }
}
