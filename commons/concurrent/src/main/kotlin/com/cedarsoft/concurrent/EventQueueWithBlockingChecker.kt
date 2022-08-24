/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.cedarsoft.org/gpl3ce
 * (GPL 3 with Classpath Exception)
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */
package com.cedarsoft.concurrent

import java.awt.AWTEvent
import java.awt.EventQueue
import java.awt.Toolkit
import java.lang.ref.WeakReference
import java.util.Timer
import java.util.TimerTask

/**
 * Event queue that checks if the EDT is blocked
 *
 */
class EventQueueWithBlockingChecker
private constructor() : EventQueue() {
  // Main timer
  private val timer = Timer("EventQueue With EDT Blocking Checker", true)

  private val eventChangeLock = Any()
  @Volatile
  private var eventDispatchingStart: Long = -1

  @Volatile
  private var event: AWTEvent? = null

  private fun kill() {
    timer.cancel()
  }

  /**
   * Record the event and continue with usual dispatching.
   */
  override fun dispatchEvent(event: AWTEvent) {
    val startTime = System.currentTimeMillis()
    setEventDispatchingStart(event, startTime)
    super.dispatchEvent(event)
    setEventDispatchingStart(null, -1)
  }

  /**
   * Register event and dispatching start time.
   *
   * @param anEvent   event.
   * @param timestamp dispatching start time.
   */
  private fun setEventDispatchingStart(anEvent: AWTEvent?, timestamp: Long) {
    synchronized(eventChangeLock) {
      event = anEvent
      eventDispatchingStart = timestamp
    }
  }

  /**
   * Add watchdog timer. Timer will trigger `listener`
   * if the queue dispatching event longer than specified
   * `maxProcessingTime`. If the timer is
   * `repetitive` then it will trigger additional
   * events if the processing 2x, 3x and further longer than
   * `maxProcessingTime`.
   *
   * @param maxProcessingTime maximum processing time.
   * @param listener          listener for events. The listener
   * will receive `AWTEvent`
   * as source of event.
   * @param repetitive        TRUE to trigger consequent events
   * for 2x, 3x and further periods.
   */
  fun addWatchdog(maxProcessingTime: Long, listener: Listener, repetitive: Boolean) {
    val checker = Watchdog(maxProcessingTime, listener, repetitive)
    timer.schedule(checker, maxProcessingTime, maxProcessingTime)
  }

  /**
   * Checks if the processing of the event is longer than the
   * specified `maxProcessingTime`. If so then
   * listener is notified.
   */
  private inner class Watchdog
  /**
   * Creates timer.
   *
   * @param maxProcessingTime maximum event processing time
   * before listener is notified.
   * @param listener          listener to notify.
   * @param repetitive        TRUE to allow consequent
   * notifications for the same event
   */
  constructor(
    private val maxProcessingTime: Long,
    private val listener: Listener?,
    private val repetitive: Boolean
  ) : TimerTask() {

    // Event reported as "lengthy" for the last time. Used to
    // prevent repetitive behaviour in non-repeatitive timers.
    private var lastReportedEvent = WeakReference<AWTEvent>(null)

    init {
      if (listener == null) {
        throw IllegalArgumentException("Listener cannot be null.")
      }
      if (maxProcessingTime < 0) {
        throw IllegalArgumentException("Max locking period should be greater than zero")
      }
    }

    override fun run() {
      val time: Long
      val currentEvent: AWTEvent?

      // Get current event requisites
      synchronized(eventChangeLock) {
        time = eventDispatchingStart
        currentEvent = event
      }

      if (currentEvent == null) {
        return
      }

      val currentTime = System.currentTimeMillis()

      // Check if event is being processed longer than allowed
      if (time == -1L) {
        return
      }

      val delta = currentTime - time
      if (delta <= maxProcessingTime) {
        return
      }

      if (repetitive || currentEvent !== lastReportedEvent.get()) {
        listener?.edtBlocked(delta, currentEvent)
        lastReportedEvent = WeakReference(currentEvent)
      }
    }
  }

  /**
   * Is notified when the edt has been blocked
   */
  interface Listener {
    /**
     * Is called if the edt is blocked
     *
     * @param blockedTime  the blocked time
     * @param currentEvent the current event
     */
    fun edtBlocked(blockedTime: Long, currentEvent: AWTEvent)
  }

  companion object {

    /**
     * Install alternative queue.
     */
    fun install(): EventQueueWithBlockingChecker {
      val eventQueue = Toolkit.getDefaultToolkit().systemEventQueue
      val newEventQueue = EventQueueWithBlockingChecker()
      eventQueue.push(newEventQueue)
      return newEventQueue
    }

    /**
     * Uninstalls the event queue if necessary
     */
    fun uninstallIfNecessary(): Boolean {
      val eventQueue = Toolkit.getDefaultToolkit().systemEventQueue as? EventQueueWithBlockingChecker ?: return false

      eventQueue.push(EventQueue())
      eventQueue.kill()

      return true
    }

    /**
     * Uninstalls the EventQueueWithChecker
     */
    @Throws(IllegalStateException::class)
    fun uninstall() {
      val eventQueue = Toolkit.getDefaultToolkit().systemEventQueue
      if (eventQueue !is EventQueueWithBlockingChecker) {
        throw IllegalStateException("Invalid eventQueue detected. Was <$eventQueue> but expected instance of EventQueueWithChecker")
      }

      eventQueue.push(EventQueue())
      eventQueue.kill()
    }
  }
}
