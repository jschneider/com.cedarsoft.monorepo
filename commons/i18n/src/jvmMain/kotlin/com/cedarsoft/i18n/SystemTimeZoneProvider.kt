package com.cedarsoft.i18n

import com.cedarsoft.time.TimeZone
import java.time.ZoneId

/**
 * Provides the default timeZone
 */
actual class SystemTimeZoneProvider actual constructor() {
  /**
   * Returns the default timeZone (from the browser or os)
   */
  actual val systemTimeZone: TimeZone = TimeZone(ZoneId.systemDefault().id)
}
