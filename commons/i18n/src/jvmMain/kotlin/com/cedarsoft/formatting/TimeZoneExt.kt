package com.cedarsoft.formatting

import com.cedarsoft.time.TimeZone
import java.time.ZoneId

/**
 * Converts to a Java ZoneID
 */
fun TimeZone.toZoneId(): ZoneId {
  return ZoneId.of(this.zoneId)
}
