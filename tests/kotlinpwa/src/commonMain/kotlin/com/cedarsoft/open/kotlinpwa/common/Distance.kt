package com.cedarsoft.open.kotlinpwa.common

import kotlinx.serialization.Serializable
import kotlin.math.absoluteValue

/**
 * The distance in meters
 */
@Serializable
data class Distance(
  /**
   * the distance to the north
   */
  val north: Double,
  /**
   * the distance to the east
   */
  val east: Double
) {

  val total: Double
    get() {
      return kotlin.math.sqrt(north.absoluteValue * north.absoluteValue + east.absoluteValue * east.absoluteValue)
    }
}
