package com.cedarsoft.common.time

import com.cedarsoft.common.kotlin.lang.toIntFloor
import kotlin.time.Duration

/**
 *
 */
fun Duration.formatHourMinutes(): String {
  val minutes = inWholeMinutes
  val hours = (minutes / 60.0).toIntFloor()
  val remainingMinutes = minutes - hours * 60

  return "${hours}:${remainingMinutes.toString().padStart(2, '0')}"
}

fun Duration.formatHourAndMinutes(): String {
  val minutes = inWholeMinutes
  val hours = (minutes / 60.0).toIntFloor()
  val remainingMinutes = minutes - hours * 60

  return "${hours}h ${remainingMinutes.toString().padStart(2, '0')}m"
}

fun Duration.formatMinutes(): String {
  return "$inWholeMinutes min"
}
