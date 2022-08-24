package com.cedarsoft.commons.javafx.busy

import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.beans.property.ReadOnlyBooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.util.Duration
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Contains code that manages the busy state.
 *
 * Listens to changes of the given busy state.
 * To detect cases in which the busy state has already been set to false before [.registerCallback] has
 * been called, an additional check is performed after [.defaultManualCheckDelay].
 *
 * If the busy state is still false after [.defaultTimeoutDelay], the callback is notified about the timeout
 */
class BusySupport @JvmOverloads constructor(
  val busyStatusProperty: ReadOnlyBooleanProperty,
  /**
   * The delay when the first manual check is done.
   */
  val defaultManualCheckDelay: Duration = Duration.millis(500.0),
  val defaultTimeoutDelay: Duration = Duration.seconds(30.0),
) {

  /**
   * Calls the callback as soon as the busy status is false.
   * A manual check is performed after [.defaultManualCheckDelay] to detect cases where the busy state has already been set to false
   */
  @JvmOverloads
  fun registerCallback(callback: FinishedCallback, manualCheckDelay: Duration = defaultManualCheckDelay, timeoutDelay: Duration = defaultTimeoutDelay) {
    //Listener for a change
    val listener: ChangeListener<Boolean> = object : ChangeListener<Boolean> {
      override fun changed(observable: ObservableValue<out Boolean>, oldValue: Boolean, newValue: Boolean) {
        if (oldValue == newValue) {
          //Avoid duplicate notifications
          return
        }
        if (!newValue) {
          //Remove the listener first to avoid multiple calls (if the callback itself sets busy to true again
          observable.removeListener(this)
          callback.finished()
        }
      }
    }
    busyStatusProperty.addListener(listener)
    val finished = AtomicBoolean()

    //Check after (default: 500 millis) to get very fast events
    Timeline(KeyFrame(manualCheckDelay, { event: ActionEvent? ->
      if (!busyStatusProperty.get()) {
        finished.set(true)
        //Remove the listener first to avoid multiple calls (if the callback itself sets busy to true again
        busyStatusProperty.removeListener(listener)
        callback.finished()
      }
    })).play()

    //Timeout check
    Timeline(KeyFrame(timeoutDelay, EventHandler { event: ActionEvent? ->
      if (finished.get()) {
        //Already notified, just return
        return@EventHandler
      }
      if (!busyStatusProperty.get()) {
        finished.set(true)
        busyStatusProperty.removeListener(listener)
        callback.finished()
      } else {
        //Still busy, timeout
        finished.set(true)
        busyStatusProperty.removeListener(listener)
        callback.timedOut()
      }
    })).play()
  }

  companion object {
    /**
     * Creates a busy support that returns after a given timeout
     */
    @JvmStatic
    fun createTimed(seconds: Int): BusySupport {
      return BusySupport(SimpleBooleanProperty(true), Duration.millis(500.0), Duration.seconds(seconds.toDouble()))
    }
  }
}
