package com.cedarsoft.common.time

import com.cedarsoft.common.kotlin.lang.toIntFloor
import kotlin.time.Duration

/**
 *
 */

/**
 * Formats the duration as "hours:minutes"
 */
fun Duration.formatHourAndMinutesShort(): String {
  val minutes = inWholeMinutes
  val hours = (minutes / 60.0).toIntFloor()
  val remainingMinutes = minutes - hours * 60

  return "${hours}:${remainingMinutes.toString().padStart(2, '0')}"
}

/**
 * Formats the duration as "17h 12min"
 */
fun Duration.formatHourAndMinutes(): String {
  val minutes = inWholeMinutes
  val hours = (minutes / 60.0).toIntFloor()
  val remainingMinutes = minutes - hours * 60

  return "${hours}h ${remainingMinutes.toString().padStart(2, '0')}min"
}

fun Duration.formatMinutes(): String {
  return "$inWholeMinutes min"
}
