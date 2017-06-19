/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce
 *         (GPL 3 with Classpath Exception)
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
package com.cedarsoft.concurrent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Event queue that checks if the EDT is blocked
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class EventQueueWithBlockingChecker extends EventQueue {
  // Main timer
  private final Timer timer = new Timer("EventQueue With EDT Blocking Checker", true);

  private final Object eventChangeLock = new Object();
  private volatile long eventDispatchingStart = -1;

  @Nullable
  private volatile AWTEvent event;

  private EventQueueWithBlockingChecker() {
  }

  /**
   * Install alternative queue.
   */
  public static EventQueueWithBlockingChecker install() {
    EventQueue eventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();
    EventQueueWithBlockingChecker newEventQueue = new EventQueueWithBlockingChecker();
    eventQueue.push(newEventQueue);
    return newEventQueue;
  }

  /**
   * Uninstalls the event queue if necessary
   */
  public static boolean uninstallIfNecessary() {
    EventQueue eventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();
    if (!(eventQueue instanceof EventQueueWithBlockingChecker)) {
      return false;
    }

    eventQueue.push(new EventQueue());
    ((EventQueueWithBlockingChecker) eventQueue).kill();

    return true;
  }

  /**
   * Uninstalls the EventQueueWithChecker
   */
  public static void uninstall() throws IllegalStateException {
    EventQueue eventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();
    if (!(eventQueue instanceof EventQueueWithBlockingChecker)) {
      throw new IllegalStateException("Invalid eventQueue detected. Was <" + eventQueue + "> but expected instance of EventQueueWithChecker");
    }

    eventQueue.push(new EventQueue());
    ((EventQueueWithBlockingChecker) eventQueue).kill();
  }

  private void kill() {
    timer.cancel();
  }

  /**
   * Record the event and continue with usual dispatching.
   */
  @Override
  protected void dispatchEvent(AWTEvent event) {
    long startTime = System.currentTimeMillis();
    setEventDispatchingStart(event, startTime);
    super.dispatchEvent(event);
    setEventDispatchingStart(null, -1);
  }

  /**
   * Register event and dispatching start time.
   *
   * @param anEvent   event.
   * @param timestamp dispatching start time.
   */
  private void setEventDispatchingStart(@Nullable AWTEvent anEvent, long timestamp) {
    synchronized (eventChangeLock) {
      event = anEvent;
      eventDispatchingStart = timestamp;
    }
  }

  /**
   * Add watchdog timer. Timer will trigger {@code listener}
   * if the queue dispatching event longer than specified
   * {@code maxProcessingTime}. If the timer is
   * {@code repetitive} then it will trigger additional
   * events if the processing 2x, 3x and further longer than
   * {@code maxProcessingTime}.
   *
   * @param maxProcessingTime maximum processing time.
   * @param listener          listener for events. The listener
   *                          will receive {@code AWTEvent}
   *                          as source of event.
   * @param repetitive        TRUE to trigger consequent events
   *                          for 2x, 3x and further periods.
   */
  public void addWatchdog(long maxProcessingTime, Listener listener, boolean repetitive) {
    Watchdog checker = new Watchdog(maxProcessingTime, listener, repetitive);
    timer.schedule(checker, maxProcessingTime, maxProcessingTime);
  }

  /**
   * Checks if the processing of the event is longer than the
   * specified {@code maxProcessingTime}. If so then
   * listener is notified.
   */
  private class Watchdog extends TimerTask {
    // Settings
    private final long maxProcessingTime;
    private final Listener listener;
    private final boolean repetitive;

    // Event reported as "lengthy" for the last time. Used to
    // prevent repetitive behaviour in non-repeatitive timers.
    @Nonnull
    private WeakReference<AWTEvent> lastReportedEvent = new WeakReference<>(null);

    /**
     * Creates timer.
     *
     * @param maxProcessingTime maximum event processing time
     *                          before listener is notified.
     * @param listener          listener to notify.
     * @param repetitive        TRUE to allow consequent
     *                          notifications for the same event
     */
    private Watchdog(long maxProcessingTime, Listener listener, boolean repetitive) {
      if (listener == null) {
        throw new IllegalArgumentException("Listener cannot be null.");
      }
      if (maxProcessingTime < 0) {
        throw new IllegalArgumentException("Max locking period should be greater than zero");
      }
      this.maxProcessingTime = maxProcessingTime;
      this.listener = listener;
      this.repetitive = repetitive;
    }

    @Override
    public void run() {
      long time;
      AWTEvent currentEvent;

      // Get current event requisites
      synchronized (eventChangeLock) {
        time = eventDispatchingStart;
        currentEvent = event;
      }

      if (currentEvent == null) {
        return;
      }

      long currentTime = System.currentTimeMillis();

      // Check if event is being processed longer than allowed
      if (time == -1) {
        return;
      }

      long delta = currentTime - time;
      if (delta <= maxProcessingTime) {
        return;
      }

      if (repetitive || currentEvent != lastReportedEvent.get()) {
        listener.edtBlocked(delta, currentEvent);
        lastReportedEvent = new WeakReference<>(currentEvent);
      }
    }
  }

  /**
   * Is notified when the edt has been blocked
   */
  public interface Listener {
    /**
     * Is called if the edt is blocked
     *
     * @param blockedTime  the blocked time
     * @param currentEvent the current event
     */
    void edtBlocked(long blockedTime, @Nonnull AWTEvent currentEvent);
  }
}
