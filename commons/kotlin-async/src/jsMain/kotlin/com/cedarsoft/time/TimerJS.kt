package com.cedarsoft.time

import com.cedarsoft.dispose.Disposable
import kotlinx.browser.window
import kotlin.time.Duration

/**
 * Sets a timer which executes a function once the timer expires.
 */
actual fun delay(delay: Duration, callback: () -> Unit): Disposable {
  return TimerId(window.setTimeout(callback, delay.inMilliseconds.toInt()))
}

/**
 * Repeats the given lambda every [delay] on the main thread
 */
actual fun repeat(delay: Duration, callback: () -> Unit): Disposable {
  return TimerId(window.setInterval(callback, delay.inMilliseconds.toInt()))
}

/**
 * Represents a timer id which may be canceled
 */
data class TimerId(val id: Int) : Disposable {
  override fun dispose() {
    window.clearTimeout(id)
  }
}
