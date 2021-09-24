package com.cedarsoft.i18n

import com.cedarsoft.time.TimeZone

/**
 * Provides the default timeZone
 */
expect class SystemTimeZoneProvider() {
  /**
   * Returns the system timeZone (from the browser or os)
   */
  val systemTimeZone: TimeZone
}
