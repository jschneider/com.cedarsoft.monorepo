package it.neckar.open.time

import it.neckar.open.unit.si.ms

object TimeConstants {
  /**
   * A timestamp that may serve as a reference point in time.
   *
   * Sun Sep 09 2001 03:46:40 GMT+0200 (Central European standard time)
   */
  const val referenceTimestamp: @ms Double = 1000000000000.0 //Beware that changing this constant may break pixel-related regression tests!

  /**
   * Milli seconds per second
   */
  const val millisPerSecond: Double = 1000.0

  /**
   * Number of milliseconds in a standard minute.
   */
  const val millisPerMinute: Double = 60 * millisPerSecond

  /**
   * Number of milliseconds in a standard hour.
   */
  const val millisPerHour: Double = 60 * millisPerMinute

  /**
   * Number of milliseconds in a standard day.
   */
  const val millisPerDay: Double = 24 * millisPerHour
}
