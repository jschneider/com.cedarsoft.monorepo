package com.cedarsoft.time

import com.cedarsoft.common.time.nanos2millis
import com.cedarsoft.unit.si.ms
import com.cedarsoft.unit.si.ns
import com.cedarsoft.unit.si.s
import java.time.Instant
import java.time.chrono.ChronoZonedDateTime

/**
 * Returns the double millis from the instant
 */
fun Instant.toDoubleMillis(): @ms Double {
  @s val secondsPart = epochSecond * 1_000.0
  @ns val nanosPart = nano.toLong().nanos2millis()

  return secondsPart + nanosPart
}

/**
 * Returns the double millis from a zoned date time
 */
fun ChronoZonedDateTime<*>.toDoubleMillis(): @ms Double {
  return toInstant().toDoubleMillis()
}

/**
 * Returns the milliseconds as long
 */
fun ChronoZonedDateTime<*>.toEpochMillis(): @ms Long {
  return this.toInstant().toEpochMilli()
}
