package com.cedarsoft.commons.javafx.axis

import com.cedarsoft.unit.other.px
import kotlin.math.ceil

/**
 * Calculates the ticks
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
class RoundedAxisTickCalculator(
  val lower: Double,
  val upper: Double
) {

  /**
   * Calculates the tick values for the given max tick count
   */
  fun calculateTickValues(maxTickCount: Int): List<Double> {
    val tickDistance = calculateTickDistance(maxTickCount)
    val tickBase = calculateTickBase(tickDistance)

    val ticks = mutableListOf<Double>()
    var current = tickBase
    while (current <= upper) {
      ticks.add(current)
      current += tickDistance
    }

    ensureContainsLowerUpper(ticks)

    return ticks
  }

  /**
   * This method ensures the lower and upper value are within the list
   */
  private fun ensureContainsLowerUpper(ticks: MutableList<Double>) {
    if (ticks.isEmpty()) {
      return
    }

    ticks[0] = lower
    ticks[ticks.size - 1] = upper
  }

  val delta: Double
    get() = upper - lower


  fun calculateTickDistance(): Double {
    return Math.pow(10.0, Math.floor(Math.log10(delta * 0.25)))
  }

  fun calculateTickDistance(maxTickCount: Int): Double {
    //The minimal distance between ticks. This value ensures the max ticks count is not exceeded
    @px val minTickDistance = delta / maxTickCount
    val tickDistance = Math.pow(10.0, Math.floor(Math.log10(minTickDistance * 10)))

    val tickCount = delta / tickDistance
    if (tickCount > maxTickCount) {
      throw IllegalStateException("Invalid tick count: $tickCount")
    }


    if (tickDistance / minTickDistance > 10) {
      return tickDistance / 10.0
    }

    if (tickDistance / minTickDistance > 2) {
      return tickDistance / 2.0
    }

    return tickDistance
  }

  fun calculateTickBase(): Double {
    return calculateTickBase(calculateTickDistance())
  }

  /**
   * Calculates the first tick
   */
  private fun calculateTickBase(tickDistance: Double): Double {
    return ceil(lower / tickDistance) * tickDistance
  }
}
