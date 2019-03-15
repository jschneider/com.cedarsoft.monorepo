package com.cedarsoft.commons.javafx.axis

import kotlin.math.ceil

/**
 * Calculates the ticks
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
class RoundedAxisTickCalculator(
  val lower: Double,
  val upper: Double
) {
  fun calculateTickValues(): List<Double> {
    val firstTick = calculateFirstTick()
    val tickUnit = calculateTickUnit()

    val ticks = mutableListOf<Double>()
    var current = firstTick
    while (current <= upper) {
      ticks.add(current)
      current += tickUnit
    }

    return ticks
  }

  val delta: Double
    get() = upper - lower


  fun calculateTickUnit(): Double {
    return Math.pow(10.0, Math.floor(Math.log10(delta * 0.25)))
  }

  fun calculateFirstTick(): Double {
    return calculateFirstTick(calculateTickUnit())
  }

  /**
   * Calculates the first tick
   */
  fun calculateFirstTick(tickUnit: Double): Double {
    return ceil(lower / tickUnit) * tickUnit
  }
}
