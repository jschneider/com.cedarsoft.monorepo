package com.cedarsoft.commons.javafx

import com.sun.javafx.tk.Toolkit
import javafx.animation.Animation
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.application.Platform
import javafx.util.Duration
import java.util.concurrent.Callable
import java.util.concurrent.FutureTask


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
    val timeline = Timeline(KeyFrame(delay, { run.run() }))
    timeline.play()
    return timeline
  }

  /**
   * Calls the given lambda after the delay once
   */
  @JvmStatic
  fun delay(delay: Duration, run: () -> Unit): Timeline {
    if (delay.toMillis() == 0.0) {
      Platform.runLater(run)
      return Timeline() //return an empty timeline that is stopped
    }

    val timeline = Timeline(KeyFrame(delay, { run() }))
    timeline.play()
    return timeline
  }

  /**
   * Returns the time line or null if the action has been scheduled immediately
   */
  fun delay(delay: kotlin.time.Duration, run: () -> Unit): Timeline {
    return delay(delay.toJavaFx(), run)
  }

  fun delay(delay: kotlin.time.Duration, run: Runnable): Timeline {
    return delay(delay, run::run)
  }

  @JvmStatic
  fun delay(delay: Duration, run: Runnable): Timeline {
    return delay(delay, run::run)
  }

  @JvmStatic
  fun repeat(delay: Duration, run: Runnable): Timeline {
    val timeline = Timeline(KeyFrame(delay, { run.run() }))
    timeline.cycleCount = Animation.INDEFINITE
    timeline.play()
    return timeline
  }

  /**
   * Repeats the given lambda every [delay]
   */
  fun repeat(delay: Duration, run: () -> Unit): Timeline {
    require(delay.toMillis() >= 16.0) {
      "A delay of at least 16 ms is required because of limitations of the JavaFX timer resolution"
    }

    val timeline = Timeline(KeyFrame(delay, { run() }))
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

  /**
   * Similar to SwingUtilities#invokeAndWait
   */
  fun runAndWait(runnable: Runnable) {
    runAndWait(runnable::run)
  }

  fun runAndWait(runnable: () -> Unit) {
    if (Platform.isFxApplicationThread()) {
      //run inline in fx thread
      runnable()
      return
    }

    FutureTask<Void?>(runnable, null).also {
      Platform.runLater(it)
    }.get()
  }

  /**
   * Similar to SwingUtilities#invokeAndWait
   *
   * @return the result of the [callable]
   */
  fun <V> runAndWait(callable: Callable<V>): V {
    if (Platform.isFxApplicationThread()) {
      return callable.call()
    }

    return FutureTask(callable).also {
      Platform.runLater(it)
    }.get()
  }

  /**
   * Waits until the next paint pulse has been fired
   */
  fun waitForPaintPulse() {
    runAndWait { Toolkit.getToolkit().firePulse() }
  }
}


/**
 * Converts a Kotlin duration to a JavaFX duration
 */
fun kotlin.time.Duration.toJavaFx(): Duration = Duration.millis(inMilliseconds)
