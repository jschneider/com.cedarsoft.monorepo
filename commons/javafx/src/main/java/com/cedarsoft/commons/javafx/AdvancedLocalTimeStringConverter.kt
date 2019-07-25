package com.cedarsoft.commons.javafx

import javafx.util.converter.LocalTimeStringConverter
import java.time.LocalTime

/**
 * Returns null instead of throwing an exception
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
class AdvancedLocalTimeStringConverter : LocalTimeStringConverter() {
  override fun fromString(value: String?): LocalTime? {
    return try {
      super.fromString(value)
    } catch (e: Exception) {
      null
    }
  }
}
