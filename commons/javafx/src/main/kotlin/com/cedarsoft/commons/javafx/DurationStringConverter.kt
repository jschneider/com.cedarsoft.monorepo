package com.cedarsoft.commons.javafx

import com.cedarsoft.commons.time.DateUtils
import javafx.util.StringConverter
import java.time.Duration

/**
 * Returns null instead of throwing an exception
 */
class DurationStringConverter : StringConverter<Duration>() {
  override fun toString(duration: Duration?): String {
    if (duration == null) {
      return ""
    }

    return DateUtils.formatDurationHHmm(duration)
  }

  override fun fromString(string: String?): Duration {
    if (string == null) {
      return Duration.ZERO
    }

    return try {
      DateUtils.parseDurationHHmm(string)
    } catch (ignore: Exception) {
      Duration.ZERO
    }
  }
}
