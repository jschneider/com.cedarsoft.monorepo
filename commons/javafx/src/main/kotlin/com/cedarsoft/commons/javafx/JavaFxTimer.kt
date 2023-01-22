package com.cedarsoft.commons.javafx

import com.cedarsoft.annotations.NonBlocking
import com.cedarsoft.annotations.JavaFriendly
import com.cedarsoft.common.time.nowMillis
import com.cedarsoft.dispose.Disposable
import com.cedarsoft.unit.si.ms
import com.cedarsoft.unit.si.ns
import com.sun.javafx.tk.Toolkit
import javafx.animation.AnimationTimer
import javafx.application.Platform
import javafx.util.Duration
import java.util.concurrent.Callable
import java.util.concurrent.FutureTask
import kotlin.time.DurationUnit


/**
 * A class for timers executed on the JavaFX Application Thread.
 */
object JavaFxTimer {
  /**
   * Calls the given runnable on the JavaFX Application Thread after the given delay.
   */
  @Deprecated("Use delay instead", ReplaceWith("delay(delay, run)", "com.cedarsoft.commons.javafx.JavaFxTimer.delay"))
  @JvmStatic
  fun start(delay: Duration, run: Runnable): Disposable {
    return delay(delay, run)
  }

  /**
   * Calls the given lambda after the delay once
   */
  @JvmStatic
  fun delay(delay: Duration, run: () -> Unit): Disposable {
    if (delay.toMillis() == 0.0) {
      Platform.runLater(run)
      return Disposable.noop
    }

    return repeatWhile(delay) {
      run()
      Continuation.Stop
    }
  }

  /**
   * Returns the animation timer or null if the action has been scheduled immediately
   */
  fun delay(delay: kotlin.time.Duration, run: () -> Unit): Disposable {
    return delay(delay.toJavaFx(), run)
  }

  @JvmStatic
  @JavaFriendly
  fun delay(delay: kotlin.time.Duration, run: Runnable): Disposable {
    return delay(delay, run::run)
  }

  @JvmStatic
  @JavaFriendly
  fun delay(delay: Duration, run: Runnable): Disposable {
    return delay(delay, run::run)
  }

  /**
   * Repeats the action - as long as true is returned by the callable
   */
  @JvmStatic
  @JavaFriendly
  @NonBlocking
  fun repeatWhile(delay: Duration, run: Callable<Continuation>): Disposable {
    return repeatWhile(delay) {
      run.call()
    }
  }

  /**
   * Repeats the action - as long as true is returned by [run]
   */
  @NonBlocking
  fun repeatWhile(delay: Duration, run: () -> Continuation): Disposable {
    require(delay.toMillis() >= 1.0) {
      "A delay of at least 1 ms is required"
    }

    return object : AnimationTimer() {
      var lastRun: @ms Double = nowMillis() //set initially to now

      override fun handle(_nowNanos: @ns Long) {
        @ms val nowInMillis = nowMillis()
        @ms val elapsedTimeSinceLastRun = nowInMillis - lastRun
        if (elapsedTimeSinceLastRun < delay.toMillis()) {
          return
        }

        lastRun = nowInMillis

        when (run()) {
          Continuation.Continue -> {} //do nothing, just continue
          Continuation.Stop -> stop()
        }
      }
    }.let {
      it.start()
      Disposable {
        it.stop()
      }
    }
  }

  @JavaFriendly
  @JvmStatic
  @NonBlocking
  fun repeat(delay: Duration, run: Runnable): Disposable {
    return repeat(delay) {
      run.run()
    }
  }

  /**
   * Repeats the given lambda every [delay] (if possible).
   * Never executes the actions *before* [delay], but sometimes later
   */
  fun repeat(delay: Duration, run: () -> Unit): Disposable {
    return repeatWhile(delay) {
      run()
      Continuation.Continue
    }
  }

  /**
   * Repeats the given lambda every [delay] (if possible).
   * Never executes the actions *before* [delay], but sometimes later
   */
  fun repeat(delay: kotlin.time.Duration, run: () -> Unit): Disposable {
    return repeat(delay.toJavaFx(), run)
  }

  /**
   * Similar to SwingUtilities#invokeAndWait
   */
  @JvmStatic
  fun runAndWait(runnable: Runnable) {
    runAndWait(runnable::run)
  }

  /**
   * Executes the given lambda within the application thread
   */
  fun <T> runAndWait(runnable: () -> T): T {
    if (Platform.isFxApplicationThread()) {
      //run inline in fx thread
      return runnable()
    }

    return FutureTask<T>(Callable(runnable))
      .also {
        Platform.runLater(it)
      }.get()
  }

  /**
   * Similar to SwingUtilities#invokeAndWait
   *
   * @return the result of the [callable]
   */
  @JvmStatic
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
fun kotlin.time.Duration.toJavaFx(): Duration = Duration.millis(toDouble(DurationUnit.MILLISECONDS))

enum class Continuation {
  Continue,
  Stop;

  companion object {
    @JvmStatic
    fun continueIf(shallContinue: Boolean): Continuation {
      return when (shallContinue) {
        true -> Continue
        false -> Stop
      }
    }

    fun continueIf(shallContinue: () -> Boolean): Continuation {
      return continueIf(shallContinue())
    }

    @JvmStatic
    fun stopIf(shallStop: Boolean): Continuation {
      return when (shallStop) {
        true -> Stop
        false -> Continue
      }
    }
  }
}
