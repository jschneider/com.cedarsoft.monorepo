package com.cedarsoft.commons.javafx

import javafx.animation.Animation
import javafx.animation.KeyFrame
import javafx.animation.Timeline
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
  fun start(delay: Duration, run: Runnable): Timeline {
    val timeline = Timeline(KeyFrame(delay, EventHandler { run.run() }))
    timeline.play()
    return timeline
  }

  /**
   * Calls the given lambda after the delay once
   */
  @JvmStatic
  fun delay(delay: kotlin.time.Duration, run: () -> Unit): Timeline {
    val timeline = Timeline(KeyFrame(delay.toJavaFx(), EventHandler { run() }))
    timeline.play()
    return timeline
  }


  @JvmStatic
  fun repeat(delay: Duration, run: Runnable) {
    val timeline = Timeline(KeyFrame(delay, EventHandler { run.run() }))
    timeline.cycleCount = Animation.INDEFINITE
    timeline.play()
  }

  /**
   * Repeats the given lambda every [delay]
   */
  fun repeat(delay: Duration, run: () -> Unit): Timeline {
    val timeline = Timeline(KeyFrame(delay, EventHandler { run() }))
    timeline.cycleCount = Animation.INDEFINITE
    timeline.play()
    return timeline
  }

  /**
   * Repeats the given lambda every [delay]
   */
  fun repeat(delay: kotlin.time.Duration, run: () -> Unit): Timeline {
    return repeat(delay.toJavaFx(), run)
  }
}


/**
 * Converts a Kotlin duration to a JavaFX duration
 */
fun kotlin.time.Duration.toJavaFx(): Duration = Duration.millis(inMilliseconds)
