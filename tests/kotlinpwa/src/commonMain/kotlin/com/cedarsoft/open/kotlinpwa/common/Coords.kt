package com.cedarsoft.open.kotlinpwa.common

import kotlinx.serialization.Serializable
import kotlin.math.PI
import kotlin.math.cos

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
@Serializable
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
 * Converts degrees to radians
 */
fun Double.toRadians(): Double {
  return this / 180.0 * PI
}

fun average(val0: Double, val1: Double): Double {
  return (val0 + val1) / 2.0
}
