package com.cedarsoft.time

/**
 * Represents a time zone
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
data class TimeZone(val zoneId: String) {
  init {
    require(zoneId.isNotEmpty()) {
      "Invalid zone id provided <$zoneId>"
    }
  }

  override fun toString(): String {
    return zoneId
  }

  companion object {
    val UTC: TimeZone = TimeZone("UTC")
    val Berlin: TimeZone = TimeZone("Europe/Berlin")
    val NewYork: TimeZone = TimeZone("America/New_York")
    val Tokyo: TimeZone = TimeZone("Asia/Tokyo")
  }
}
