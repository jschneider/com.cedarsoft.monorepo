package com.cedarsoft.geolocation

import kotlin.math.cos

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
data class Coords(
  /**
   * Breite (north/south)
   */
  val latitude: Double,
  /**
   * Länge (east/west)
   */
  val longitude: Double
) {
  /**
   * Calculates the distance to the target
   */
  fun calculateDistanceTo(target: Coords): Distance {
    val averageLatitude = average(latitude, target.latitude)
    val longitudeFactor = 111.3 * cos(averageLatitude.toRadians())

    val deltaLatitude = (target.latitude - latitude) * 113.1 * 1_000
    val deltaLongitude = (target.longitude - longitude) * longitudeFactor * 1_000

    return Distance(deltaLatitude, deltaLongitude)
  }
}

/**
 * The distance in meters
 */
data class Distance(
  /**
   * the distance to the north
   */
  val north: Double,
  /**
   * the distance to the east
   */
  val east: Double
)

/**
 * Converts degrees to radians
 */
private fun Double.toRadians(): Double {
  return Math.toRadians(this)
}

private fun average(val0: Double, val1: Double): Double {
  return (val0 + val1) / 2.0
}

