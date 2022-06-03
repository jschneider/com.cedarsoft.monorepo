package com.cedarsoft.commons.javafx

import javafx.util.converter.LocalTimeStringConverter
import java.time.LocalTime

/**
 * Returns null instead of throwing an exception
 */
class AdvancedLocalTimeStringConverter : LocalTimeStringConverter() {
  override fun fromString(value: String?): LocalTime? {
    return try {
      super.fromString(value)
    } catch (ignore: Exception) {
      null
    }
  }
}
