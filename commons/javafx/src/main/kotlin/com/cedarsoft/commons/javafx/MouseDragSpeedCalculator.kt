@file:Suppress("UnstableApiUsage")

package com.cedarsoft.commons.javafx

import it.neckar.open.annotations.UiThread
import com.cedarsoft.unit.other.px
import com.cedarsoft.unit.other.px_ns
import com.cedarsoft.unit.si.ns
import com.google.common.collect.EvictingQueue
import java.util.Collections
import java.util.Locale
import java.util.function.ToDoubleFunction
import javax.annotation.Nonnull

/**
 * Calculates mouse speeds when dragging
 */
class MouseDragSpeedCalculator {
  /**
   * Contains the last speed elements of the last n dragging events
   */
  @UiThread
  private val entries: @px_ns EvictingQueue<Entry> = EvictingQueue.create(SPEED_QUEUE_SIZE)

  @UiThread
  private fun calculateSpeed(getSpeed: ToDoubleFunction<Entry>): @px_ns Double {
    if (entries.isEmpty()) {
      return 0.0
    }

    // The algorithm is taken from http://mortoray.com/2015/04/08/measuring-finger-mouse-velocity-at-release-time/ .
    var totalTime = 0.0
    for (entry in entries) {
      totalTime += entry.deltaTime.toDouble()
    }
    var speed = 0.0
    for (entry in entries) {
      val alpha = 1.0 - Math.pow(Math.E, -entry.deltaTime / totalTime)
      speed = alpha * getSpeed.applyAsDouble(entry) + (1.0 - alpha) * speed
    }
    return speed
  }

  @UiThread
  fun calculateSpeedX(): @px_ns Double {
    return calculateSpeed { obj: Entry -> obj.speedX }
  }

  @UiThread
  fun calculateSpeedY(): @px_ns Double {
    return calculateSpeed { obj: Entry -> obj.speedY }
  }

  /**
   * Adds a new entry
   *
   * @param deltaTime the delta time
   */
  @UiThread
  fun add(deltaTime: @ns Long, deltaX: @px Double, deltaY: @px Double) {
    if (deltaTime <= 0) {
      return
    }
    val now: @ns Long = System.nanoTime()

    //first remove entries that might be too old
    entries.removeIf { entry: Entry -> now - entry.time > MAX_AGE }
    entries.add(Entry(now, deltaTime, deltaX / deltaTime.toDouble(), deltaY / deltaTime.toDouble()))
  }

  /**
   * Clears the speed calculation
   */
  @UiThread
  fun clear() {
    entries.clear()
  }

  @Nonnull
  fun getEntries(): Collection<Entry> {
    return Collections.unmodifiableCollection(entries)
  }

  class Entry(val time: @ns Long, val deltaTime: @ns Long, val speedX: @px_ns Double, val speedY: @px_ns Double) {
    override fun toString(): String {
      return String.format(Locale.US, "Entry{time=%d, deltaTime=%d, speedX=%s, speedY=%s}", time, deltaTime, speedX, speedY)
    }
  }

  companion object {
    const val SPEED_QUEUE_SIZE: Int = 15
    const val MAX_AGE: @ns Int = 400 * 1000 * 1000
  }
}
