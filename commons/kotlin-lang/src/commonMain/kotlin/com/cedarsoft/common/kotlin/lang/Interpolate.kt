package com.cedarsoft.common.kotlin.lang

import com.cedarsoft.unit.other.pct

/**
 * Returns the interpolated value between start and end values.
 * This is interpreted as percentage.
 */
fun @pct Double.interpolate(start: Double, end: Double): Double = (start + (end - start) * this)

/**
 * Returns the interpolated value between start and end values.
 * This is interpreted as percentage.
 *
 * E.g: `0.5.interpolate(40.0, 60.0)` returns `50.0`
 */
fun Double.interpolate(start: Int, end: Int): Int = (start + (end - start) * this).toInt()

/**
 * Returns in percentage the distance of the value between lowerBound and upperBound
 *
 * E.g: returns 0.5 for 20.0.relativeDistanceBetween(40.0, 80.0)
 * E.g: returns -3.5 for 10.0.relativeDistanceBetween(80.0, 100.0)
 */
fun Double.relativeDistanceBetween(lowerBound: Double, upperBound: Double): @pct Double {
  val delta = upperBound - lowerBound
  require(delta != 0.0) { "lowerBound ($lowerBound) must be different than upperBound ($upperBound)" }

  val relativeThis = this - lowerBound
  return relativeThis / delta
}
